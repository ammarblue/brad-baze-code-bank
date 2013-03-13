/*
The MIT License

 Copyright (c) 2005 - 2010
    1. Distributed Systems Group, University of Portsmouth (2005)
    2. Community Grids Laboratory, Indiana University (2004)
    3. Aamir Shafi (2005 - 2010)
    4. Bryan Carpenter (2005 - 2010)
    5. Jawad Manzoor (2009)


Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * File         : SMPDeviceImpl.java
 * Author       : Sang Lim, Bryan Carpenter, Aamir Shafi 
 * Created      : Wed Nov 13 17:07:15 EST 2002
 * Revision     : $Revision: 1.5 $
 * Updated      : $Date: 2005/06/23 12:43:29 $
 */
package xdev.smpdev;

//Replace these stars with something more understandable ...
import xdev.*;

//import xdev.niodev.ConfigReader ; 
/*import mpjdev.Status;
import mpjdev.Request;
import mpjbuf.*;
import java.nio.ByteBuffer;*/
import mpjbuf.NIOBuffer;
import java.util.HashMap;
import java.util.UUID;


/**
 * The communicator class.  Directly analogous to an MPI communicator.
 */
public class SMPDeviceImpl {

    static int numRegisteredThreads = 0;
    private static boolean initialized = false;  // MPJ initialized
//    public static PrintStream out = null;
    private int size;
    private Thread[] threads;    //Map from node id to Thread.
    private HashMap ids;          //Map from Thread to node id.
//RPC    xdev.ProcessID id = null;
    xdev.ProcessID[] pids = null;
//    static SMPDevProcess smpProcess = null;

    private int context;
    private int barrierCount;
//    private SMPDeviceImpl newSMPDeviceImpl;
    public static final int MODEL_MULTIPROCESS = 0;
    public static final int MODEL_MULTITHREADED = 1;

    public static int getModel() {
        return MODEL_MULTITHREADED;
    }

    /**
     * Number of processes spanned by this communicator.
     */
    public int size() {
        return size;
    }

    /**
     * Id of current process relative to this communicator.
     * Equivalent to MPI_COMM_RANK.
     */
    public xdev.ProcessID id() throws XDevException {

        Thread thisThread = Thread.currentThread();
        ProcessID value = null ; 

	if(thisThread.getThreadGroup() instanceof SMPDevProcess) {
	  value = ((SMPDevProcess)(thisThread.getThreadGroup())).getID();
	} 
	 
        if (value == null) {
            throw new XDevException("SMPDeviceImpl.id() invoked by thread " +
                    "outside communicator group");
        }
        return value;
    }
    /**
     * Create a new communicator the spanning the same
     * set of processes, but with a distinct communication context.    
    public SMPDeviceImpl dup() throws XDevException {
    int [] ids = new int [size] ;
    for(int i = 0 ; i < size ; i++)
    ids [i] = i ;

    return create(ids) ;
    }
     */

    /**
     * Create a new communicator the spanning the set of processes
     * selected by the `ids' array (containing ids are relative to this
     * communicator).
     * The new communicator also has a distinct communication context.
     * Processes that are out side of the group will return null.

    public synchronized SMPDeviceImpl create(int [] ids)  throws XDevException {
    int myId = id() ;
    boolean amInGroup = false ;

    if(barrierCount == 0) {
    newSMPDeviceImpl = new SMPDeviceImpl() ;
    synchronized(SMPDeviceImpl.class) {
    newSMPDeviceImpl.context = nextContext++ ;
    }
    newSMPDeviceImpl.size = ids.length ;
    newSMPDeviceImpl.threads = new Thread [newSMPDeviceImpl.size] ;
    newSMPDeviceImpl.ids = new HashMap() ;
    boolean inGroup [] = new boolean [size] ;

    for(int i = 0 ; i < newSMPDeviceImpl.size ; i++) {
    int id = ids [i] ;
    if(id < 0 || id > size) {
    throw new XDevException("In SMPDeviceImpl.create(), value ids [" +
    i + "] is out of range (" + id + ")") ;
    }

    if(inGroup [id]) {
    throw new XDevException("In SMPDeviceImpl.create(), value " + id +
    "duplicated in ids array") ;
    }
    else
    inGroup [id] = true ;

    if(id == myId)
    amInGroup = true ;

    newSMPDeviceImpl.threads [i] = threads [id] ;
    newSMPDeviceImpl.ids.put(threads [id], new Integer(i)) ;
    }//end for ....

    newSMPDeviceImpl.barrierCount = 0 ;
    }//if barrierCount == 0 ends
    else {
    if(ids.length != newSMPDeviceImpl.size) {
    throw new XDevException("In SMPDeviceImpl.create(), threads " +
    "specify different values for ids array") ;
    }

    for(int i = 0 ; i < newSMPDeviceImpl.size ; i++) {
    int id = ids [i] ;
    if(id == myId)
    amInGroup = true ;
    if(id < 0 || id > size ||
    newSMPDeviceImpl.threads [i] != threads [id]) {
    throw new XDevException("In SMPDeviceImpl.create(), threads " +
    "specify different values for ids array") ;
    }
    }
    }//end else if barrierCount == 0

    barrierCount++ ;
    SMPDeviceImpl result = amInGroup ? newSMPDeviceImpl : null ;

    if(barrierCount == size) {// Reset barrier and wake up other threads.
    barrierCount = 0 ;
    notifyAll() ;
    }

    else {
    try {
    wait() ;
    }
    catch(InterruptedException e) {
    throw new XDevException("In SMPDeviceImpl.create(), unexpected " +
    "interruption during wait()??") ;
    }
    }

    return result ;
    }
     */
    /**
     * Destroy this communicator.    
    public void free() {}
     */
    /**
     * Blocking send ...
     * 
     * Equivalent to MPI_SEND
     */
    public void send(mpjbuf.Buffer buf, ProcessID destID, int tag,
            int context) throws Exception {
        SMPRequest req = (SMPRequest) isend(buf, destID, tag, context);

        if (mpi.MPI.DEBUG && SMPDevice.logger.isDebugEnabled()) {
          SMPDevice.logger.debug("After isend in -- calling iwait "
	                                      + req.hashCode());
	}
        req.iwait();
        if (mpi.MPI.DEBUG && SMPDevice.logger.isDebugEnabled()) {
          System.out.println("After ---- calling iwait "+ req.hashCode());
        }
    }

    /**
     * Blocking receive of message, whose contents are copied to `buf'.
     * The capacity of `buf' must be large enough to accept these
     * contents.  Initializes the `source' and `tag' fields of the
     * returned `Status'.  Equivalent to MPI_RECV.
     */
    public mpjdev.Status recv(mpjbuf.Buffer buf, ProcessID srcID, int tag,
            int context)
            throws XDevException {
         mpjdev.Status status = new mpjdev.Status(srcID.uuid(), tag, -1);
        SMPRequest req = (SMPRequest) irecv(buf, srcID, tag, context, status);
        mpjdev.Status s= req.iwait();
        return s;
    }
    private RecvQueue recvQueue = new RecvQueue();
    private SendQueue sendQueue = new SendQueue();

    /**
     * Non-blocking version of `send'.
     * Equivalent to MPI_ISEND
     */
    public mpjdev.Request isend(mpjbuf.Buffer buf, ProcessID destID,
            int tag, int context) throws Exception {
        if (buf == null) 
        throw new XDevException("In SMPDeviceImpl.isend(), buffer is null.");
        ProcessID myID = id();
        SMPSendRequest send = new SMPSendRequest(buf, context,
                destID, myID, tag);
        SMPRecvRequest matchingRecv = null;
        synchronized (SMPDeviceImpl.class) {
            matchingRecv = recvQueue.rem(send); 
            if (matchingRecv == null) {
            	 send.setPending(true);
                 sendQueue.add(send);
                 return send;
            }
        }
// copy data from `buf' to buffer in `matchingRecv'
// and initialize status field in `matchingRecv'.    
// matchingRecv.buf.copy(buf) ;
                matchingRecv.buffer.setSize(buf.getSize());
                if (buf.getDynamicBuffer() != null) {
                    matchingRecv.buffer.setDynamicBuffer(buf.getDynamicBuffer());
                }
                
               
                ((NIOBuffer) matchingRecv.buffer.getStaticBuffer()).getBuffer().limit(
                        buf.getSize());
                ((NIOBuffer) matchingRecv.buffer.getStaticBuffer()).getBuffer().position(0);
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().limit(buf.getSize());
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().position(0);
                ((NIOBuffer) matchingRecv.buffer.getStaticBuffer()).getBuffer().put(
                        ((NIOBuffer) buf.getStaticBuffer()).getBuffer());
                ((NIOBuffer) matchingRecv.buffer.getStaticBuffer()).getBuffer().flip();
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().clear();
////////////////////////////////////////////////////////////////////

                matchingRecv.status.srcID = myID.uuid();
                matchingRecv.status.tag = tag;
                matchingRecv.setPending(false);
                matchingRecv.status.numEls = send.numEls;
                matchingRecv.status.type = send.type; //temp

                 matchingRecv.numEls = send.numEls; //temp
                 matchingRecv.type = send.type; 

                // Check if anybody is iwait-ing on `matchingRecv'.
                // If so, remove all requests from wait set, and signal 
                // the waiting thread.
                 
                SMPRequest.WaitSet waiting = matchingRecv.getWaitSet();
                if (waiting != null) {
                    waiting.select(matchingRecv);
                }
                send.setPending(false);	
        return send;
    }

    /**
     * Non-blocking version of `recv'.
     * Equivalent to MPI_IRECV
     */
    public mpjdev.Request irecv(mpjbuf.Buffer buf, ProcessID srcID,
            int tag, int context, mpjdev.Status status)
            throws XDevException {

        if (buf == null) 
        throw new XDevException("In SMPDeviceImpl.irecv(), buffer is null.");
        ProcessID myID = id();
        SMPRecvRequest recv = new SMPRecvRequest(buf, context, myID,
                srcID, tag, status);
        SMPSendRequest matchingSend;
        synchronized (SMPDeviceImpl.class) {
        	matchingSend = sendQueue.rem(recv);
            if (matchingSend == null) {
            	recv.setPending(true);
                recvQueue.add(recv);
                return recv;
            }
        }
                buf.setSize(matchingSend.buffer.getSize());
                if (matchingSend.buffer.getDynamicBuffer() != null) {
                    buf.setDynamicBuffer(matchingSend.buffer.getDynamicBuffer());
                }
                ((NIOBuffer) matchingSend.buffer.getStaticBuffer()).getBuffer().limit(buf.getSize());
                ((NIOBuffer) matchingSend.buffer.getStaticBuffer()).getBuffer().position(0);
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().limit(buf.getSize());
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().position(0);
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().put(
                        ((NIOBuffer) matchingSend.buffer.getStaticBuffer()).getBuffer());
                ((NIOBuffer) buf.getStaticBuffer()).getBuffer().flip();
                ((NIOBuffer) matchingSend.buffer.getStaticBuffer()).getBuffer().clear();
////////////////////////////////////////////////////////////////////

                recv.status =status;
                recv.status.srcID = matchingSend.srcID.uuid();
                recv.status.tag = matchingSend.tag;
                recv.type = matchingSend.type;
                recv.numEls = matchingSend.numEls;
                // recv.status.numEls = matchingSend.numEls;  //temp
                //recv.status.type = matchingSend.type;  //temp
                matchingSend.setPending(false);

                // Check if anybody is iwait-ing on `matchingSend'.
                // If so, remove all requests from wait set, and signal 
                // the waiting thread.

                SMPRequest.WaitSet waiting = matchingSend.getWaitSet();
                if (waiting != null) {
                    waiting.select(matchingSend);
                }
                recv.setPending(false);	
        return recv;
    }


    public mpjdev.Status iprobe(ProcessID srcID, int tag,
            int context) throws XDevException {

        mpjdev.Status status = null;
         ProcessID myID = id();
        SMPSendRequest request = sendQueue.check(context, myID, srcID, tag);
        if (request != null) {
            //now this is a tricky one ...
            status = new mpjdev.Status(request.srcID.uuid(),
                   request.tag, -1, request.type,
                    request.numEls); //jd                   
        }
        return status;
    }




    /**
     * Initialize MPJ, and register current thread as a node in the new
     * MPJ world.  This assumes every thread knows in advance the total
     * number of threads, and also knows its own unique rank in the set of
     * threads.
     * <p>
     * <table>
     * <tr><td><tt> nprocs </tt></td><td> number of nodes in the MPJ world.</tr>
     * <tr><td><tt> myId </tt></td><td> rank the current thread should have
     *                                  in that world.</tr>
     * </table>
     * <p>
     */
    public synchronized static ProcessID[] init(String file, int rank)
            throws Exception {
        int nprocs = Integer.parseInt(file); 
        if (initialized) {
            throw new XDevException("Call to SMPDeviceImpl.init() after MPJ has " +
                    "already been successfully initialized");
        }

        if (nprocs < 0) {
            throw new XDevException("In SMPDeviceImpl.init(), requested negative " +
                    "world size " + nprocs);
        } else if (numRegisteredThreads == 0) {

            WORLD.size = nprocs;

            WORLD.pids = new ProcessID[WORLD.size];
            WORLD.threads = new Thread[WORLD.size];
            WORLD.ids = new HashMap();
            WORLD.context = 0;
            WORLD.barrierCount = 0;
            for (int i=0; i<nprocs; i++) WORLD.pids[i] = new ProcessID(UUID.randomUUID());

        } else if (nprocs != WORLD.size) {
            throw new XDevException("In SMPDeviceImpl.init(), mismatch in number of " +
                    "nodes requested by threads: " +
                    WORLD.size + " vs " + nprocs);
        }

        if(rank < 0) {
        throw new XDevException("In SMPDeviceImpl.init(), requested negative " +
        "node id, " + rank) ;
        }
        else if(rank >= WORLD.size) {
        throw new XDevException("In SMPDeviceImpl.init(), requested node id, " +
        rank + " is too large for requested " +
        "world size, " + WORLD.size) ;
        }
        Thread thread = Thread.currentThread();

/*RPC        if (WORLD.threads[rank] != null) {	
            throw new XDevException("In SMPDeviceImpl.init(), requested node id, " +
                    rank + " has already been registered");
        } else {*/ 
            SMPDevProcess smpProcess = (SMPDevProcess)thread.getThreadGroup();
            smpProcess.setProcessID(WORLD.pids[rank]);

            WORLD.threads[rank] = thread;

            numRegisteredThreads++; 
            if (numRegisteredThreads == WORLD.size) {

                initialized = true;
                /*RPCSMPDeviceImpl.class.notifyAll();*/

            }/*RPC else {
                try {
                    SMPDeviceImpl.class.wait();
                } catch (InterruptedException e) {
                    throw new XDevException("In SMPDeviceImpl.init(), unexpected " +
                            "interuption during wait()??");
                }
            } //end else */
        /*RPC} //end else*/
        return WORLD.pids;
    //TTD:- it may throw a null pointer exception ..
    }//end init() 

    /**
     * Finalize MPI.
     * <p>
     * Java binding of the MPI operation <tt>MPI_FINALIZE</tt>.
     */
    public synchronized static void finish() throws XDevException {
        // Need to clean up properly, so applets can be restarted.

        // Check current thread belongs to this communicator
	if(!(Thread.currentThread().getThreadGroup()
                                       instanceof SMPDevProcess)) {
          throw new XDevException("SMPDeviceImpl.finish() invoked by thread " +
                    "outside MPJ world");
        }
        numRegisteredThreads--;
        if (numRegisteredThreads == 0) {

            // Reset MPJ initialized status and wake up other threads.

            initialized = false;

            //RPCSMPDeviceImpl.class.notifyAll();
        } /*RPCelse {
            try {
                SMPDeviceImpl.class.wait();
            } catch (InterruptedException e) {
                throw new XDevException("In SMPDeviceImpl.finish(), unexpected " +
                        "interuption during wait()??");
            }
        }*/
    }

    /**
     * The initial communicator.
     * Equivalent of MPI_COMM_WORLD.
     */
    public static final SMPDeviceImpl WORLD = new SMPDeviceImpl();
 
    static class RecvQueue {

        /**
         *  Add a `SMPRecvRequest' to the front of the queue associated with
         *  its key.
         */
        public void add(SMPRecvRequest recv) {
            add(recv.key, recv);
        }

        /**
         *  Remove from its queue the next `SMPRecvRequest'
         *  that matches one of the keys in `send'.
         */
        public SMPRecvRequest rem(SMPSendRequest send) {
            SMPRecvRequest matchingRecv = null;
            Key[] keys = send.keys;
            long minSequenceNum = Long.MAX_VALUE;
            for (int i = 0; i < keys.length; i++) {
                SMPRecvRequest recv = get(keys[i]);
                if (recv != null && recv.sequenceNum < minSequenceNum) {
                    minSequenceNum = recv.sequenceNum;
                    matchingRecv = recv;
                }
            }
            if (matchingRecv != null) {
                rem(matchingRecv.key, matchingRecv);
            }
            return matchingRecv;
        }

        private SMPRecvRequest get(Key key) {

            return (SMPRecvRequest) map.get(key);
        }

        private void add(Key key, SMPRecvRequest recv) {

            SMPRecvRequest head = (SMPRecvRequest) map.get(key);

            if (head == null) {
                recv.next = recv;
                recv.prev = recv;

                map.put(key, recv);
            } else {
                SMPRecvRequest last = head.prev;

                last.next = recv;
                head.prev = recv;

                recv.prev = last;
                recv.next = head;
            }
        }

        private void rem(Key key, SMPRecvRequest recv) {

            SMPRecvRequest head = (SMPRecvRequest) map.get(key);

            if (recv == head) {
                if (recv.next == recv) {

                    // Unique entry.

                    map.remove(key);
                } else {
                    SMPRecvRequest next = recv.next;
                    SMPRecvRequest last = recv.prev;

                    last.next = next;
                    next.prev = last;

                    map.put(key, next);
                }
            } else {
                SMPRecvRequest next = recv.next;
                SMPRecvRequest prev = recv.prev;

                prev.next = next;
                next.prev = prev;
            }
        }

  
 
        private HashMap map = new HashMap();
    }

    static class SendQueue {

        /**
         *  Add a `SendRequest' to the front of the queues associated with
         *  its keys.
         */
        public void add(SMPSendRequest send) {
            Key[] keys = send.keys;
            for (int i = 0; i < keys.length; i++) {
                add(i, keys[i], send);
            }
        }

        private void add(int i, Key key, SMPSendRequest send) {
            SMPSendRequest head = (SMPSendRequest) map.get(key);
            if (head == null) {
                send.next[i] = send;
                send.prev[i] = send;
                map.put(key, send);
            } else {
                SMPSendRequest last = head.prev[i];
                last.next[i] = send;
                head.prev[i] = send;
                send.prev[i] = last;
                send.next[i] = head;
            }
        }

        /**
         *  Remove from its queues the next `SMPSendRequest' that matches the
         *  key in `recv'.
         */
        public SMPSendRequest rem(SMPRecvRequest recv) {
            SMPSendRequest matchingSend = get(recv.key);
            if (matchingSend != null) {
                Key[] keys = matchingSend.keys;
                for (int i = 0; i < keys.length; i++) {
                    rem(i, keys[i], matchingSend);
                }
            }
            return matchingSend;
        }

        private SMPSendRequest get(Key key) {
            return (SMPSendRequest) map.get(key);
        }

        private void rem(int i, Key key, SMPSendRequest send) {
            SMPSendRequest head = (SMPSendRequest) map.get(key);
            if (send == head) {
                if (send.next[i] == send) {
                    // Unique entry.    
                    map.remove(key);
                } else {
                    SMPSendRequest next = send.next[i];
                    SMPSendRequest last = send.prev[i];
                    last.next[i] = next;
                    next.prev[i] = last;
                    map.put(key, next);
                }
            } else {
                SMPSendRequest next = send.next[i];
                SMPSendRequest prev = send.prev[i];
                prev.next[i] = next;
                next.prev[i] = prev;
            }
        }

        SMPSendRequest check(int context, ProcessID destID, ProcessID srcID, int tag) {
            Key key = new Key(context, destID, srcID, tag);
            return (SMPSendRequest)map.get(key);

        }


        private HashMap map = new HashMap();
    }

    static class Key {

        private int context,  tag;
        private ProcessID destID,  srcID;

        Key(int context, ProcessID destID, ProcessID srcID, int tag) {

            this.context = context;
            this.destID = destID;
            this.srcID = srcID;
            this.tag = tag;
        }

        public int hashCode() {
            return destID.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof Key) {
                Key other = (Key) obj;
                return (other.context == context) &&
                        (other.destID.uuid().equals(destID.uuid())) &&
                        (other.srcID.uuid().equals(srcID.uuid())) &&
                        (other.tag == tag);
            }
            return false;
        }

        public String tostring() {
            return "cxt " + context + " tag " + tag + " sid " + srcID.uuid()+ " did " + destID.uuid();
        }
    }
}


