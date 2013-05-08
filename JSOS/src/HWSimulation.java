// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// HWSimulation.java
//

import java.awt.*;
import javax.swing.*;

//
// HWSimulatedDisk.java
//
// This is a simple disk simulation.  It has its own thread.
// The delay is always 500 ms.  The disk thread's run loop
// just looks for disk requests and services them.
//
class HWSimulatedDisk implements Runnable {

	private boolean disk_is_busy = false;
	private int memory_address;
	private int control_reg;
	private int[] disk_data;

	public HWSimulatedDisk() {
		// Allocate space for the simulated disk
		disk_data = new int[SOSDiskDriver.SizeOfDiskPage
				* SOSDiskDriver.NumberOfDiskPages];
	}

	public synchronized int GetStatusRegister() {
		// There is only one active bit in the disk status register
		if (disk_is_busy)
			return 1 << 31;
		else
			return 0;
	}

	public synchronized void SetAddressRegister(int address) {
		// Just save the address for when we do teh transfer
		memory_address = address;
	}

	public synchronized void SetCommandRegister(int reg_value) {
		if (disk_is_busy)
			return;
		control_reg = reg_value;
		disk_is_busy = true;
		notify();
	}

	public synchronized void WaitForCommand() {
		// Wait until the disk is not busy
		while (!disk_is_busy) {
			SIM.Trace(SIM.TraceHWDisk, "SimulatedDisk: WaitForCommand");
			try {
				wait();
			} catch (InterruptedException e) {
				break;
			}
		}
		// Delay for the seek, latency and transfer.
		// For now, all delays total 500 ms.
		SIM.Trace(SIM.TraceHWDisk, "SimulatedDisk: sleep 500");

		// Delay for seek, 500 ms for now
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
		}
		SIM.Trace(SIM.TraceHWDisk, "SimulatedDisk: wake up!");

		// read or write?
		boolean is_read = (control_reg & (1 << 31)) == 0;
		int disk_block = (control_reg >> 10) & 0xFFFFF;
		int disk_offset = disk_block * SOSDiskDriver.SizeOfDiskPage;
		// This is the simulation part.
		// Move the data between the simulated disk and the simulated memory.
		for (int i = 0; i < SOSDiskDriver.SizeOfDiskPage; ++i) {
			if (is_read) {
				SIM.hw.SetCellUnmapped(memory_address++,
						disk_data[disk_offset++]);
			} else {
				disk_data[disk_offset++] = SIM.hw
						.GetCellUnmappedAsInt(memory_address++);
			}
		}

		// signal an interrupt?
		SIM.Trace(SIM.TraceHWDisk,
				"control_reg=" + Integer.toBinaryString(control_reg));
		if ((control_reg & (1 << 30)) != 0) {
			SIM.Trace(SIM.TraceHWDisk, "Signal disk interrupt");
			// signal a disk interrupt
			SIMIntHandler handler = (SIMIntHandler) SIM.hw
					.GetCellUnmapped(SIM.DiskIntIntVector);
			handler.HandleInterrupt(0);
			SIM.Trace(SIM.TraceHWDisk, "Return from disk interrupt signaling");
		}
		disk_is_busy = false;
	}

	public void run() {
		// Loop and wait for the command
		while (true) {
			WaitForCommand();
		}
	}
}

//
// HWTimer.java
//
// This is the simulated hardware timer. It runs in its own thread.
// It waits for timer requests and counts them down 100ms at a tick.
//
class HWTimer implements Runnable {
	private int time_left;

	public HWTimer() {
		time_left = 0;
	}

	public synchronized void tick(int increment) {
		if (time_left > 0) {
			time_left -= increment;
			if (time_left <= 0) {
				time_left = 0;
				SIM.Trace(SIM.TraceHWTimer, "Signal timer interrupt");
				SIMIntHandler handler = (SIMIntHandler) SIM.hw
						.GetCellUnmapped(SIM.TimerIntVector);
				handler.HandleInterrupt(0);
				SIM.Trace(SIM.TraceHWTimer, "Return from timer interrupt");
			}
		}
	}

	public synchronized int setTimer(int time_to_interrupt) {
		int ret = time_left;
		time_left = time_to_interrupt;
		return ret;
	}

	public synchronized int GetTimeLeft() {
		return time_left;
	}

	public void run() {
		SIM.Trace(SIM.TraceHWTimer, "HardwareTimer starting");
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("TimerSIMIntHandler was interrupted");
			}
			tick(100);
		}
	}
}

class HWSimulation {
	//
	// ***** Object data *****
	//
	static public HWTimer hwTimer;
	static public Thread hwTimerThread;
	static public HWSimulatedDisk theDisk;
	static public Thread theDiskThread;
	static public int MemoryBaseRegister;
	static public int MemoryLimitRegister;

	public Thread[] ProcessThread = new Thread[SOSProcessManager.NumberOfProcesses];
	public DefaultListModel[] ProcessTrace = new DefaultListModel[SOSProcessManager.NumberOfProcesses];

	//
	// ***** Constructor *****
	//
	public HWSimulation() {

		// Create the physical memory array
		memory = new Object[mem_size];

		// Create the hardware timer
		hwTimer = new HWTimer();
		hwTimerThread = new Thread(hwTimer);
		SIM.TheSIM.threadList.addElement(hwTimerThread);
		hwTimerThread.start();

		// Create the sumulated disk
		theDisk = new HWSimulatedDisk();
		theDiskThread = new Thread(theDisk);
		SIM.TheSIM.threadList.addElement(theDiskThread);
		theDiskThread.start();
	}

	//
	// ***** Memory Simulation *****
	//
	public static final int mem_size = 10000;
	private Object[] memory;

	public Object GetCellUnmapped(int addr) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		if (addr == SIM.DiskStatusRegister) {
			return new Integer(theDisk.GetStatusRegister());
		}
		return memory[addr];
	}

	public int GetCellUnmappedAsInt(int addr) {
		return ((Integer) GetCellUnmapped(addr)).intValue();
	}

	public Object GetCell(int addr) {
		if (addr < MemoryLimitRegister) {
			return GetCellUnmapped(addr + MemoryBaseRegister);
		} else {
			SIM.TheSIM.sosPauser.checkIfPaused();
			// Memory addressing error
			SIMIntHandler handler = (SIMIntHandler) SIM.hw
					.GetCellUnmapped(SIM.ProgErrIntIntVector);
			handler.HandleInterrupt(2);
			return null;
		}
	}

	public int GetCellAsInt(int addr) {
		return ((Integer) GetCell(addr)).intValue();
	}

	public void SetCellUnmapped(int addr, Object obj) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		// Check if this is really a device register
		if (addr == SIM.DiskAddressRegister) {
			theDisk.SetAddressRegister(((Integer) obj).intValue());
			return;
		} else if (addr == SIM.DiskControlRegister) {
			theDisk.SetCommandRegister(((Integer) obj).intValue());
			return;
		}
		memory[addr] = obj;
	}

	public void SetCellUnmapped(int addr, int n) {
		SetCellUnmapped(addr, (Object) new Integer(n));
	}

	public void SetCell(int addr, Object obj) {
		if (addr < MemoryLimitRegister) {
			SetCellUnmapped(addr + MemoryBaseRegister, obj);
		} else {
			SIM.TheSIM.sosPauser.checkIfPaused();
			// Memory addressing error
			SIMIntHandler handler = (SIMIntHandler) SIM.hw
					.GetCellUnmapped(SIM.ProgErrIntIntVector);
			handler.HandleInterrupt(2);
		}
	}

	public void SetCell(int addr, int n) {
		SetCell(addr, (Object) new Integer(n));
	}

	//
	// ***** System Call Simulation *****
	//
	public void SystemCall(int addr) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		SIM.Trace(SIM.TraceHWSim, "HW system call");

		Thread ct = Thread.currentThread();
		ct.setPriority(Thread.MAX_PRIORITY - 1);

		// call the system call handler
		int old_cur_proc = SIM.sosData.current_process;
		SIMIntHandler handler = (SIMIntHandler) GetCellUnmapped(SIM.SyscallIntVector);
		handler.HandleInterrupt(addr);
		int cur_proc = SIM.sosData.current_process;
		if (cur_proc != old_cur_proc)
			ct.suspend();

		SIM.Trace(SIM.TraceHWSim, "HW system call complete: Resuming pid "
				+ cur_proc);
		return;
	}

	//
	// ***** Timer Simulation *****
	//
	public int SetTimer(int time_until_interrupt) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		return hwTimer.setTimer(time_until_interrupt);
	}

	//
	// ***** Process Simulation *****
	//
	public void RunProcess(int pid) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		// Resume the process thread
		ProcessThread[pid].setPriority(Thread.NORM_PRIORITY);
		SIM.Trace(SIM.TraceHWSim,
				"Resuming user thread " + ProcessThread[pid].toString());
		ProcessThread[pid].resume();
	}

	public void WaitForInterrupt() {
		SIM.TheSIM.sosPauser.checkIfPaused();
		SIM.Trace(SIM.TraceHWSim, "No process to run.  Suspending "
				+ Thread.currentThread().toString());
		if (theDisk.GetStatusRegister() == 0 && hwTimer.GetTimeLeft() == 0) {
			SIM.Trace(SIM.TraceAlways,
					"System idle. No ready processes, no timer, no disk IO");
		}
		Thread.currentThread().suspend();
	}

	public void CreateProcess(int app_number, int arg, int pid) {
		SIM.TheSIM.sosPauser.checkIfPaused();
		Thread t;
		switch (app_number) {
		case 0:
			t = new Thread(new AppGUICounter(Thread.currentThread().toString()));
			SIM.TheSIM.threadList.addElement(t);
			SIM.Trace(SIM.TraceHWSim,
					"create and suspend user 0 " + t.toString());
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			t.suspend();
			break;
		default:
			t = new Thread(new AppTests(app_number, arg));
			SIM.TheSIM.threadList.addElement(t);
			SIM.Trace(SIM.TraceHWSim, "create and suspend user " + app_number
					+ " " + t.toString());
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			t.suspend();
			break;
		}
		ProcessThread[pid] = t;

		// Add a tab for this process
		// Panel p = new Panel();
		DefaultListModel pmodel = new DefaultListModel();
		ProcessTrace[pid] = pmodel;
		JList ptrace = new JList(pmodel);
		JScrollPane pscroll = new JScrollPane(ptrace);
		// p.add(pscroll);
		/****
		 * if( SIM.TheFrame != null ) SIM.TheFrame.setVisible(false); else
		 * SIM.TheSIM.stop();
		 ****/
		SIM.TheSIM.proc_tabs.addTab("Proc" + pid, null, pscroll,
				"Trace for process " + pid);
		/****
		 * if( SIM.TheFrame != null ) SIM.TheFrame.setVisible(true); else {
		 * SIM.TheSIM.start(); }
		 ****/
	}
}
