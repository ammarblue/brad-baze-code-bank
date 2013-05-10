import java.nio.ByteBuffer;
public class Mem {
	private static byte[] memmory;
	private static final int CharSize=Character.SIZE/Byte.SIZE;
	private static final int IntSize=Integer.SIZE/Byte.SIZE;
	private static final int LongSize=Long.SIZE/Byte.SIZE;
	private static final int DoubleSize=Double.SIZE/Byte.SIZE;
	Mem(){
		memmory=new byte[128];
	}
	Mem(int size) throws Exception{
		if(size<1){
			throw new Exception("MEMSIZE MUST BE >1");
		}
		memmory=new byte[size];
	}
	public int sizeOfMem(){
		return memmory.length;
	}
	public void storeByte(byte v,int add) throws MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		memmory[add]=v;
	}
	public void storeInt(int v,int add) throws ArrayIndexOutOfBoundsException,MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		byte[] temp=ByteBuffer.allocate(IntSize).putInt(v).array();
		for(int i=0;i<temp.length;i++){
			memmory[add+i]=temp[i];
		}
	}
	public void storeLong(long v,int add) throws ArrayIndexOutOfBoundsException, MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		byte[] temp=ByteBuffer.allocate(LongSize).putLong(v).array();
		for(int i=0;i<temp.length;i++){
			memmory[add+i]=temp[i];
		}
	}
	public void storeDouble(double v,int add) throws ArrayIndexOutOfBoundsException, MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		byte[] temp=ByteBuffer.allocate(DoubleSize).putDouble(v).array();
		for(int i=0;i<temp.length;i++){
			memmory[add+i]=temp[i];
		}
	}
	public void storeChar(char v,int add) throws ArrayIndexOutOfBoundsException, MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		byte[] temp=ByteBuffer.allocate(CharSize).putChar(v).array();
		for(int i=0;i<temp.length;i++){
			memmory[add+i]=temp[i];
		}
	}
	public void storeBoolean(boolean v,int add) throws MemRangeException{
		if(add>memmory.length||add<0){
			throw new MemRangeException();
		}
		if(v){
			memmory[add]=1;
		}else{
			memmory[add]=0;
		}
	}
	public byte getByte(int add){
		return memmory[add];
	}
	public int getInt(int add) throws MemRangeException{
		if(add+IntSize>memmory.length){
			throw new MemRangeException();
		}
		return ByteBuffer.wrap(memmory, add, IntSize).getInt();
	}
	public long getLong(int add) throws MemRangeException{
		if(add+LongSize>memmory.length){
			throw new MemRangeException();
		}
		return ByteBuffer.wrap(memmory, add, LongSize).getLong();
	}
	public double getDouble(int add) throws MemRangeException{
		if(add+DoubleSize>memmory.length){
			throw new MemRangeException();
		}
		return ByteBuffer.wrap(memmory, add, DoubleSize).getDouble();
	}
	public char getChar(int add) throws MemRangeException{
		if(add+CharSize>memmory.length){
			throw new MemRangeException();
		}
		return ByteBuffer.wrap(memmory, add, CharSize).getChar();
	}
	public boolean getBoolean(int add) throws MemRangeException{
		if(add>memmory.length){
			throw new MemRangeException();
		}
		if(memmory[add]==1){
			return true;
		}else{
			return false;
		}
	}
}