//this is the class that will create all the objects and run the threads
public class Schedualer extends Worker{
	String op;//the sting that is the name of the arithamtic operation that the user wants
	Operations o;//operations object
	Slice s;//the slice 
	THREADS t[];//array of threads
	int i;//number of iterations that user made for loop
	int [] a;
	int [] b;
	Schedualer(String op,int i,int[] a,int b[]){
		op=op;
		i=i;
		a=a;
		b=b;
		o=new Operations();
		s=new Slice(i);
		t=new THREADS[s.i];
	}
	public void THREADS(){//runs the threads
		for(int j=0;j<s.i;j++){
			t[j]=new THREADS(o,s,op);
		}
	}
}
