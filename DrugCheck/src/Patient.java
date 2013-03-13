import java.util.*;
public class Patient extends Doctor{
	int id;
	private String FName;
	private String LName;
	private int age;
	private boolean gender;
	private String condition;
	Orders A[];
	Patient(int ID){
		id=ID;
	}
	public void setInfo(){
		System.out.println("Enter patient First Name");
		Scanner FNAME=new Scanner(System.in);
		FName=FNAME.next();
		System.out.println("Enter patient Last Name");
		Scanner LNAME=new Scanner(System.in);
		LName=LNAME.next();
		System.out.println("Enter patient age");
		Scanner AGE=new Scanner(System.in);
		age=AGE.nextInt();
		System.out.println("Enter patient condition");
		Scanner CONDITION=new Scanner(System.in);
		condition=CONDITION.next();
		System.out.println("How many orders?");
		Scanner ONUM=new Scanner(System.in);
		int ordernum=ONUM.nextInt();
		A=new Orders[ordernum];
		for(int i=0;i<ordernum;i++){
			A[i]=new Orders(i);
		}
		for(int i=0;i<ordernum;i++){
			System.out.println("How many drugs will be in the script");
			Scanner DNUM=new Scanner(System.in);
			int dnum=DNUM.nextInt();
			A[i].setDrug(dnum);
		}
	}
}
