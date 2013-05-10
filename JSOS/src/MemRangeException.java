
public class MemRangeException extends Exception{
	public MemRangeException(){
		super("ERROR ADDRESS IS OUT OF MEMMORY RANGE");
	}
	public MemRangeException(String message){
		super("ERROR MEMMORY IS OUT OF RANGE"+message);
	}
}
