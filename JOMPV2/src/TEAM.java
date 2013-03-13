
public class TEAM{
	int size;
	RUNNER[] runners;
	TEAM(int num){
		size=num;
		runners=new RUNNER[size];
	}
	public void LSD(){ /*Long Slow Distance: this is where the threads come up to speed */
		for(int i=0;i<size;i++){
			runners[i]=new RUNNER(i);
			runners[i].state=0;
		}
	}
	public void Recover(){ /*this is called after the threads are shutdown by COACH */
		while(runners[0].t.isAlive()||runners[1].t.isAlive()||runners[2].t.isAlive()||runners[3].t.isAlive()){
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){
				
			}
		}
		for(int i=0;i<size;i++){
			runners[i].retire();
		}
	}
	public void OldSpiceTime(){/*starts running computations */
		for(int i=0;i<size;i++){
			runners[i].state=1;
		}
	}
}
