
public class TRACK {
	int PS,PF;
	int numPrimes=0;
	int LOCK=0;
	TRACK(){}
	public void HandOff(){
		LOCK++;
		numPrimes++;
		LOCK--;
	}
}
