/*
 * this is the semaphore and the critical section
 * there are 4 forks in the CS
 * when a philosopher gets hungry and wants to eat he/she will come and see if there are any forks 
 * if there are then take one and tell the others that there is now one less fork
 * if there is no forks then the philosopher just waits and checks back to see if one of the others put one back 
 */
public class CS {
	int forks;
	CS(){
		forks=4;//the shared data
		System.out.println(forks+" forks left");
	}
	public boolean enter(){//enters the basket of forks and takes one
		forks--;
		System.out.println(forks+" forks left");
		return true;
	}
	public boolean exit(){//puts fork back into the basket 
		forks++;
		System.out.println(forks+" forks left");
		return true;
	}
}
