
public class COACH {
	TEAM spartans;
	RACE county;
	COACH(){
		county=new RACE();
		spartans=new TEAM(4);//this creates the team and the number of runners
	}
	public void GetSet(){
		spartans.LSD();
	}
	public void GO(){
		spartans.OldSpiceTime();
	}
	public void coolDown(){
		spartans.Recover();
	}
}
