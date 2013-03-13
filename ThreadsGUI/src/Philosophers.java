
public class Philosophers implements Runnable{
	Thread t;
	int ID;
	int state;
	CS cs;
	Philosophers(int Num,CS in){
		cs=in;
		ID=Num+1;
		state=1;
		t=new Thread(this);
		t.start();
	}
	public void run(){
		if(state==1){
			System.out.println("Philosopher "+ID+" is thinking");
			long busy=(long)(Math.random()*20000);
			try{
				Thread.sleep(busy);
			}catch(InterruptedException e){}
			state=2;
			run();
		}else if(state==2){
			System.out.println("Philosopher "+ID+" is hungry");
			if(cs.forks>0){
				if(cs.enter()==true){state=3;run();}
			}else{
				System.out.println("Philosopher "+ID+" is waiting");
				try{
					Thread.sleep(1);
					run();
				}catch(InterruptedException e){}
			}
		}else if(state==3){
			System.out.println("Philosopher "+ID+" is eating");
			long busy=(long)(Math.random()*20000);
			try{
				Thread.sleep(busy);
				if(cs.exit()==true){state=1;run();}
			}catch(InterruptedException e){}
		}
	}
}
