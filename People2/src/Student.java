import java.util.*;
import javax.swing.*;
public class Student extends Person{
	String major;
	String year;
	private double gpa;
	private String id;
	Student(){
		setNonPrivate();
		setPrivate();
	}
	public void setNonPrivate(){
		System.out.println("Enter Student Name");
		Scanner NAME=new Scanner(System.in);
		super.Name=NAME.next();
		System.out.println("Enter Student Gender(M F)");
		Scanner GENDER=new Scanner(System.in);
		super.gender=GENDER.next();
		System.out.println("Enter Student Height");
		Scanner HEIGHT=new Scanner(System.in);
		super.height=HEIGHT.nextDouble();
		System.out.println("Enter your birth date ex: 1991 12 1");
		System.out.println("YEAR:");
		Scanner BYEAR=new Scanner(System.in);
		System.out.println("MONTH:");
		Scanner MONTH=new Scanner(System.in);
		System.out.println("DAY:");
		Scanner DAY=new Scanner(System.in);
		super.setDate(BYEAR.nextInt(), MONTH.nextInt(), DAY.nextInt());
		System.out.println("Enter Student Major");
		Scanner MAJOR=new Scanner(System.in);
		major=MAJOR.next();
		System.out.println("Enter Student Year(freshman ect...");
		Scanner YEAR=new Scanner(System.in);
		year=YEAR.next();
	}
	public void setPrivate(){
		System.out.println("Enter Student SSN");
		Scanner SSN=new Scanner(System.in);
		super.setSSN(SSN.next());
		System.out.println("Enter Student GPA");
		Scanner GPA=new Scanner(System.in);
		gpa=GPA.nextDouble();
		System.out.println("Enter Student ID");
		Scanner ID=new Scanner(System.in);
		id=ID.next();
		System.out.println("Enter Student Weight");
		Scanner WEIGHT=new Scanner(System.in);
		super.setWeight(WEIGHT.nextInt());
	}
}
