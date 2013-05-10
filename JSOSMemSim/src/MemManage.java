public class MemManage {
	Mem memmory;

	MemManage() {
	}

	public int initMem(int size) {
		try {
			memmory = new Mem(size);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int store(int v, int add) {
		try {
			memmory.storeInt(v, add);
		} catch (ArrayIndexOutOfBoundsException | MemRangeException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int printMem() {
		try {
			for(int i=0;i<5;i++){
				System.out.println(memmory.getInt(i*(Integer.SIZE/Byte.SIZE)));
			}
		} catch (MemRangeException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
}
