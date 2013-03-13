//Brad Bazemore
//CSCI1302
//Dr. LI

public class Main {
	public static void main(String args[]){
		Course A=new Course("PP2","CSCI1302",6);
		Teacher B=new Teacher();
		Student C[]=new Student[1];
		for(int i=0;i<1;i++){
			C[i]=new Student();
			A.setStudents(C[i], i);
		}
		A.setTeacher(B);
		A.printf();
	}
}
