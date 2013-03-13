import java.util.Scanner;
import javax.swing.JOptionPane;
public class Teacher extends Person{
	String rank;
	boolean tenure;
	String tenurestr;
	String department;
	private int salary;
	Teacher(){
		setNonPrivate();
		Private();
	}
	public void setNonPrivate(){
		System.out.println("Enter Professor Name");
		Scanner NAME=new Scanner(System.in);
		super.Name=NAME.next();
		System.out.println("Enter your birth date ex: 1991 12 1");
		System.out.println("YEAR:");
		Scanner BYEAR=new Scanner(System.in);
		System.out.println("MONTH:");
		Scanner MONTH=new Scanner(System.in);
		System.out.println("DAY:");
		Scanner DAY=new Scanner(System.in);
		super.setDate(BYEAR.nextInt(), MONTH.nextInt(), DAY.nextInt());
		System.out.println("Enter Professor Height");
		Scanner HEIGHT=new Scanner(System.in);
		super.height=HEIGHT.nextDouble();
		System.out.println("Enter Professor Rank(full,assistant ect...)");
		Scanner RANK=new Scanner(System.in);
		rank=RANK.next();
		System.out.println("Enter Professor Department");
		Scanner DEPARTMENT=new Scanner(System.in);
		department=DEPARTMENT.next();
		int n=JOptionPane.showConfirmDialog(null,"Does this teacher have tenure?","Tenure?",JOptionPane.YES_NO_OPTION);
		if(n==JOptionPane.YES_OPTION){
			tenure=true;
			tenurestr="YES";
		}else if(n==JOptionPane.NO_OPTION){
			tenure=false;
			tenurestr="NO";
		}
	}
	public void Private(){
		System.out.println("Enter Professor SSN");
		Scanner SSN=new Scanner(System.in);
		super.setSSN(SSN.next());
		System.out.println("Enter Professor Salary");
		Scanner SALARY=new Scanner(System.in);
		salary=SALARY.nextInt();
		System.out.println("Enter Professor Weight");
		Scanner WEIGHT=new Scanner(System.in);
		super.setWeight(WEIGHT.nextInt());
	}
}
