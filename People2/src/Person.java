import java.util.*;
public class Person{
	String Name;
	private String SSN;
	String gender;
	double height;
	private int weight;
	Date bday;
	Person(){}
	public void setSSN(String IN){
		SSN=IN;
	}
	public void setWeight(int IN){
		weight=IN;
	}
	public void setDate(int year,int month,int day){
		bday=new Date(year,month,day);
	}
}
