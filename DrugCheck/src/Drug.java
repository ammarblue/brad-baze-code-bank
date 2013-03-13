import java.util.*;
public class Drug implements Check{
	String DrugName;
	int NumPills;
	Drug(){}
	public void setDrugInfo(){
		System.out.println("Enter the name of the drug");
		Scanner NAME=new Scanner(System.in);
		DrugName=NAME.next();
		System.out.println("Enter number of pills");
		Scanner PILLS=new Scanner(System.in);
		NumPills=PILLS.nextInt();
		if(DrugName.compareTo(DrugA)==0){
			if(NumPills>limitA){
				System.out.println("AMOUNT OF PILLS FOR "+DrugName+" IS LIMITED TO "+limitA);
				System.out.println("THIA ATTEMPT WILL BE REPORTED!");
				System.exit(0);
			}
		}else if(DrugName.compareTo(DrugB)==0){
			if(NumPills>limitB){
				System.out.println("AMOUNT OF PILLS FOR "+DrugName+" IS LIMITED TO "+limitB);
				System.out.println("THIA ATTEMPT WILL BE REPORTED!");
				System.exit(0);
			}
		}else{
			System.out.println("This drug is not recognized");
		}
	}
}
