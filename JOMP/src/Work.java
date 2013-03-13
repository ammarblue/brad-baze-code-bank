import java.util.concurrent.CountDownLatch;


public class Work {
    int start, end;
    int task;
    CountDownLatch c;
    IWork where;
    int who;
    int nTeam;
    public Work(IWork w, int start, int end, int task, int tid, int n, CountDownLatch c) {
    	where=w; this.start=start; this.end=end; this.task=task; this.c=c; who=tid; nTeam=n;
    }
    public void countDown() {
    	if (c!=null) c.countDown();
    }

}
