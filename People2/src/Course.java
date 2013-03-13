
public class Course{
		String Name;
		String Number;
		int MaxStudents;
		int cnt=0;
		Teacher A;
		Student B[];
		Course(String name,String num,int max){
			Name=name;
			Number=num;
			MaxStudents=max;
			B=new Student[1];
		}
		public void setTeacher(Teacher x){
			A=x;
		}
		public void setStudents(Student x,int i){
			B[i]=x;
			cnt++;
			if(cnt>MaxStudents){
				System.out.println("You have exceed the max numbers of students...");
				System.out.println("END OF LINE");
				System.exit(0);		
			}
		}
		public void printf(){
			//needs orginization
			System.out.println(Name+" "+Number+" is taught by "+A.Name);
			System.out.println("     Rank: "+A.rank);
			System.out.println("     Department: "+A.department);
			System.out.println("     Tenure?: "+A.tenurestr);
			for(int i=0;i<B.length;i++){
				System.out.println("Student "+i+":");
				System.out.println("     Name: "+B[i].Name);
				System.out.println("     Year: "+B[i].year);
				System.out.println("     Major: "+B[i].major);
			}
		}
}
