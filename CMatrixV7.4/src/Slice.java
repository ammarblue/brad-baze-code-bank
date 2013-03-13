//just manipulate the data in the thread not any data stored in this class, this is to prevent race conditions
public class Slice {
	int NUM_THREADS=100;
    static int NUM_ROWS=10;//how many rows there will be vs the chunks
    static int FLOOR_INCREMENTER=10;//size of chunks
    static int CEILING_INCREMENTER=(FLOOR_INCREMENTER-1);
    int floor;
    int ceiling;
    int cnt=NUM_THREADS;
	Slice(){
	}
	public void SliceNum(int i){
        int row=i/NUM_ROWS;
        floor=(i-(FLOOR_INCREMENTER*row))*FLOOR_INCREMENTER;
        ceiling=floor+CEILING_INCREMENTER;
	}
}
