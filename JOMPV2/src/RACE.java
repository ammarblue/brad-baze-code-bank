
public class RACE extends COACH{
	static int FN=3;
	static int EN=10000;
	RACE(){
		TRACK t=new TRACK(); 
		super.GetSet();
	}
	public void start(){
		super.GO();
	}
	public void finish(){
		super.coolDown();
	}
	public void TheRun(TRACK T){
		for(int i=T.PS;i<T.PF;i++){/*PS: point start PF:point finish */
			if(i%2!=0){
				T.HandOff();
			}
		}
	}
}
