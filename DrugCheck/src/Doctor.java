import java.util.*;
public class Doctor{
	String FName;
	String LName;
	int age;
	int yearsofpractice;
	boolean gender;
	Patient A[];
	Doctor(){}
	public void setPatient(){
		System.out.println("Enter number of patients");
		Scanner NP=new Scanner(System.in);
		int num=NP.nextInt();
		A=new Patient[num];
		for(int i=0;i<num;i++){
			A[i]=new Patient(i);
		}
		for(int i=0;i<num;i++){
			A[i].setInfo();
		}
	}
	public void setDoctor(){
		System.out.println("Enter your first name");
		Scanner FNAME=new Scanner(System.in);
		FName=FNAME.next();
		System.out.println("Enter your last name");
		Scanner LNAME=new Scanner(System.in);
		LName=LNAME.next();
		System.out.println("Enter your age");
		Scanner AGE=new Scanner(System.in);
		age=AGE.nextInt();
		System.out.println("Enter the numbers of years you have be in practice");
		Scanner YIP=new Scanner(System.in);
		yearsofpractice=YIP.nextInt();
	}
}
