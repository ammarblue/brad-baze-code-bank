// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSMem.java
//   Not used in this version of the the SOS
//
class SOSMem {
	// --------------------------------------------------------------------
	// MEMORY SYSTEM
	// --------------------------------------------------------------------
	// --------------------------------------------------------------------
	// MemoryModel
	// The OS can handle memory in various ways
	// --------------------------------------------------------------------
	// static, fixed region for each process id
	static final int StaticNonPaged = 1;
	// dynamically allocated memory blocks of any size
	static final int EndOfFreeList = -1;
	static final int DynamicNonPaged = 2;

	// ********** For dynamic memeory managment only (block list) *********
	// --------------------------------------------------------------------
	// struct Block
	// The structure for block list nodes.
	// Used only in the DynamicNonPaged memory model.
	// --------------------------------------------------------------------
	class Block {
		public int size; // in bytes
		public int isFree; // free or allocated block
		public int start; // where the block starts
		public Block next, prev; // doubly linked list

		// constructor
		public Block(int sz, int isf, int st, Block n, Block p) {
			size = sz;
			isFree = isf;
			start = st;
			next = n;
			prev = p;
		}
	}

	/**************
	 * void SetMemoryModel( String model ) { if( model.equals("StaticNonPaged")
	 * ) { sosData.memoryModel = StaticNonPaged; hw.TheMachine.MemorySystem =
	 * BaseLimit; } else if( model.equals("DynamicNonPaged") ) {
	 * sosData.memoryModel = DynamicNonPaged; hw.TheMachine.MemorySystem =
	 * BaseLimit; } else { System.out.println(model +
	 * " is not a defined memory model."); } }
	 * 
	 * //-------------------------------------------------------------------- //
	 * InitializeMemory // Initialize the memory subsystem. // // Different
	 * initializations depending on the memory model being used.
	 * //--------------------------------------------------------------------
	 * 
	 * void InitializeMemorySystem( int start, int size ) { Trace(
	 * "Initialize memory subsystem, start=0x" & start & ", size=" & size );
	 * switch( sosData.memoryModel ) { case StaticNonPaged: // Nothing to do,
	 * static allocation.already done
	 * Trace("Static memory allocation -- base/limit registers");
	 * Trace("Process i at address i * " & FixedProcessSize); break; case
	 * DynamicNonPaged: // one large free block sosData.BlockList = new
	 * Block(size, True, start, 0, 0); Trace
	 * "Dynamic memory allocation -- base/limit registers" ); break; }
	 * 
	 * //-------------------------------------------------------------------- //
	 * AllocateMemory // Allocate memory to a process. // // Do different things
	 * depending on how memory is managed.
	 * //--------------------------------------------------------------------
	 * 
	 * int AllocateMemory( int pid ) { switch( sosData.memoryModel ) { case
	 * StaticNonPaged: if( pid >= SOSConstants.NumberOfProcesses ) {
	 * System.out.println("ERROR: pid " + pid + ">= max procs " +
	 * SOSConstants.NumberOfProcesses); return 0; } sosData.pd[pid].sa.base =
	 * pid * FixedProcessSize; return True; case DynamicNonPaged: return
	 * AllocateBlock( sosData.pd[pid].sa.limit, sosData.pd[pid].sa.base ); }
	 * return 0; }
	 * 
	 * //-------------------------------------------------------------------- //
	 * FreeMemory // Allocate memory to a process. // // Do different things
	 * depending on how memory is managed.
	 * //--------------------------------------------------------------------
	 * 
	 * void FreeMemory( int pid ) { switch( sosData.memoryModel ) { case
	 * StaticNonPaged: // nothing to do break; case DynamicNonPaged: FreeBlock(
	 * sosData.pd[pid].sa.base ); break; } }
	 * 
	 * //-------------------------------------------------------------------- //
	 * DumpBlocks // Dump out the block list. // // This is for debugging the
	 * block list procedures. A static procedure.
	 * //--------------------------------------------------------------------
	 * 
	 * void DumpBlocks( String msg ) { Block p = sosData.BlockList; while( p !=
	 * 0 ) { System.out.println( msg & ": p=" & p & " size=" & p.size & " free="
	 * & p.isFree & "start=" & p.start & " next=" & p.next & " prev=" & p.prev);
	 * p = p.next; } }
	 * 
	 * //-------------------------------------------------------------------- //
	 * AllocateBlock // Dynamically allocate a block of memory (of any size). //
	 * // Look through the list of all blocks to find the first free block //
	 * that is big enough. Split off any extra space into another // free block.
	 * //--------------------------------------------------------------------
	 * 
	 * int AllocateBlock( int size, char **start ) { Block p =
	 * sosData.BlockList; // go through the list of blocks while( p != 0 ) { if(
	 * p.isFree && p.size >= size ) { // this block is big enough to use, see
	 * what is left over int extra = p.size - size; if( extra != 0 ) { // split
	 * the block into two blocks Block np = new Block( extra, True, p.start +
	 * size, p.next, p ); if( p.next != 0 ) p.next.prev = np; p.next = np; }
	 * p.size = size; p.isFree = False; start = p.start; Trace(
	 * "Allocate block at " & p.start & ", size=" & size ); return True; } p =
	 * p.next; } char msg[100]; Trace( "Allocate block FAILED, size=" & size );
	 * return False; }
	 * 
	 * //-------------------------------------------------------------------- //
	 * FreeBlock // Free a block of dynamic memory. // // Find the block and
	 * free it. Merge it with the next and previous // blocks if they are free
	 * to avoid having two contiguous free blocks.
	 * //--------------------------------------------------------------------
	 * 
	 * void FreeBlock( char * start ) { Block p = sosData.BlockList; // go
	 * through the list of blocks to find this one while( p != 0 ) { if( p.start
	 * == start ) { p.isFree = True; // merge with the next block if it is free
	 * Block nextp = p.next; if( nextp != 0 && nextp.isFree ) { p.size +=
	 * nextp.size; p.next = nextp.next; if( nextp.next != 0 ) nextp.next.prev =
	 * p; delete nextp; } // merge with the previous block if it is free Block
	 * prevp = p.prev; if( prevp != 0 && prevp.isFree ) { prevp.size += p.size;
	 * prevp.next = p.next; if( p.next != 0 ) p.next.prev = prevp; delete p; }
	 * Trace( "Free block at " & start ); return;) } p = p.next; }
	 * System.out.println("ERROR: returned block not found"); }
	 ***************/
}
