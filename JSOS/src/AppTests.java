// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// AppTests.java
//
class AppTests implements Runnable {
	private int app_number;
	private int arg;

	public AppTests(int an, int in_arg) {
		app_number = an;
		arg = in_arg;
	}

	public int MakeSystemCall(int syscall_number, int arg1, int arg2) {
		SIM.hw.SetCell(101, new Integer(syscall_number));
		SIM.hw.SetCell(102, new Integer(arg1));
		SIM.hw.SetCell(103, new Integer(arg2));
		SIM.hw.SystemCall(100);
		// get system call return code
		return SIM.hw.GetCellAsInt(100);
	}

	public void run() {
		int counter = 0;
		int ret = 0;
		int msg_q;
		switch (app_number) {
		default:
		case 1:
			SIM.Trace(SIM.TraceApp, "Starting first app");
			MakeSystemCall(SOSSyscallIntHandler.CreateProcessSystemCall, 0, 0);

			SIM.Trace(SIM.TraceApp, "Starting second app");
			MakeSystemCall(SOSSyscallIntHandler.CreateProcessSystemCall, 0, 0);

			SIM.Trace(SIM.TraceApp, "Exiting initial process");
			MakeSystemCall(SOSSyscallIntHandler.ExitProcessSystemCall, 0, 0);

			SIM.Trace(SIM.TraceApp, "ERROR -- still in inital process!");
			break;
		case 2:
			SIM.Trace(SIM.TraceApp, "Creating the message queue");
			msg_q = MakeSystemCall(
					SOSSyscallIntHandler.CreateMessageQueueSystemCall, 0, 0);
			SIM.Trace(SIM.TraceApp, "Message queue number " + msg_q);

			SIM.Trace(SIM.TraceApp, "Starting message receiver app");
			MakeSystemCall(SOSSyscallIntHandler.CreateProcessSystemCall, 3,
					msg_q);

			SIM.Trace(SIM.TraceApp, "Starting message sending loop");
			while (counter < 5) {
				++counter;
				// Build message (of one word only)
				// Message buffer at cells 200-207
				SIM.hw.SetCell(200, new Integer(counter));
				// Send message system call
				ret = MakeSystemCall(
						SOSSyscallIntHandler.SendMessageSystemCall, 200, msg_q);
				if (ret == 0)
					SIM.Trace(SIM.TraceApp, "SENT message " + counter);
				else {
					SIM.Trace(SIM.TraceApp, "Message Send ERROR, ret=" + ret);
					break;
				}

				// Delay so that we don't use all the message buffers
				try {
					Thread.currentThread().sleep(5000);
				} catch (InterruptedException e) {
				}
			}
			SIM.Trace(SIM.TraceApp, "Exiting message sender app");
			MakeSystemCall(SOSSyscallIntHandler.ExitProcessSystemCall, 0, 0);
			break;
		case 3:
			msg_q = arg;
			SIM.Trace(SIM.TraceApp, "Starting message receiving loop, queue "
					+ msg_q);
			while (true) {
				ret = MakeSystemCall(
						SOSSyscallIntHandler.ReceiveMessageSystemCall, 200,
						msg_q);
				if (ret == 0)
					SIM.Trace(SIM.TraceApp,
							"RECEIVED message " + SIM.hw.GetCellAsInt(200));
				else {
					SIM.Trace(SIM.TraceApp, "Message Receive ERROR, ret=" + ret);
					break;
				}
				++counter;
			}
			SIM.Trace(SIM.TraceApp, "Exiting message receiver app");
			MakeSystemCall(SOSSyscallIntHandler.ExitProcessSystemCall, 0, 0);
			break;
		case 4:
			msg_q = arg;
			SIM.Trace(SIM.TraceApp, "Starting disk write loop");
			counter = 1;
			while (counter < 6) {
				SIM.hw.SetCell(300, counter);
				ret = MakeSystemCall(SOSSyscallIntHandler.DiskWriteSystemCall,
						300, 0);
				if (ret != 0)
					break;
				SIM.Trace(SIM.TraceApp, "Disk write ret=" + ret);
				ret = MakeSystemCall(SOSSyscallIntHandler.DiskReadSystemCall,
						400, 0);
				if (ret != 0)
					break;
				SIM.Trace(SIM.TraceApp, "Disk write ret=" + ret);
				SIM.Trace(
						SIM.TraceApp,
						"wrote " + counter + ", read="
								+ SIM.hw.GetCellAsInt(400));
				++counter;
			}
			SIM.Trace(SIM.TraceApp, "Exiting disk writer/reader app");
			MakeSystemCall(SOSSyscallIntHandler.ExitProcessSystemCall, 0, 0);
			break;
		}
	}
}
