
public class Main {
	public static void main(String[] args) {
		MemManage test=new MemManage();
		test.initMem(32);
		for(int i=0;i<5;i++){
			test.store(i,i*4);
		}
		test.printMem();
	}
}
