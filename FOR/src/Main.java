/*Based on the runs i have done so far when i implement the thread train which breaks things up into 50000 threads i have the best result of 11 secs every time 
 * yet if i use NewThread there is no consistency to the results getting best times of 14sec to 24sec 
 * if you do one thread per calculation Java's cleanup and ect. slows it down and wastes time getting times of 31 sec at best
 */
import java.util.*;
import java.awt.*;
public class Main{
	public static void main(String[] args){
		Date mains=new Date();
		/*new NewThreads("ONE",2,100000);
		Date d1=new Date();
		System.out.println(d1);
		new NewThreads("TWO",100000,200000);
		Date d2=new Date();
		System.out.println(d2);
		new NewThreads("THREE",200000,300000);
		Date d3=new Date();
		System.out.println(d3);
		new NewThreads("FOUR",300000,400000);
		Date d4=new Date();
		System.out.println(d4);
		new NewThreads("FIVE",400000,500002);
		Date d5=new Date();
		System.out.println(d5);*/
		ThreadTrain array[]=new ThreadTrain[50000];
		for(int i=1;i<50000;i++){
			array[i]=new ThreadTrain(Integer.toString(i),i);
		}
		Date mainf=new Date();
		try{
			Thread.sleep(3000);
		}catch (InterruptedException e){
			System.out.println("ERROR");
		}
		System.out.println(mains);
		System.out.println(mainf);
		System.out.print("END OF LINE");
		Toolkit.getDefaultToolkit().beep();
	}
}