/*
 * Brad Bazemore
 * the main idea of the program is to explain the critical section and semaphores
 * there are 5 philosophers all eating lunch and thinking
 * but there is only 4 forks 
 * so the forks have to be shared
 * the philosophers have 3 states they can be in 
 * 1 thinking: they don't need a fork because they are not eating anything
 * 2 hungry: the philosophers are looking to see if there are any forks so they can eat
 * 3 eating: the philosophers are eating there food and have a fork
 * you have to manually stop the program right now
 * a GUI for this is in the works
 * NOTE: this is just a basic example for the use of teaching the ideas of semaphores and concurrency 
 */
public class Main {
	public static void main(String[] args) {
		CS table=new CS();
		Philosophers[] p=new Philosophers[5];
		for(int i=0;i<5;i++){
			p[i]=new Philosophers(i,table);
		}
	}
}
