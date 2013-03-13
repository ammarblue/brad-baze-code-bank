/* Ashleigh Amrine
 * CSCI 1302A
 * 9/20/2012
 */

public class Star {

	private String name;
	private int diameter;
	private int age;
	private int surf_temp;
	
	public Star(){
	}
	
	public Star(String name, int diameter, int age, int surf_temp){
		this.name = name;
		this.diameter = diameter;
		this.age = age;
		this.surf_temp = surf_temp;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSurf_temp() {
		return surf_temp;
	}

	public void setSurf_temp(int surf_temp) {
		this.surf_temp = surf_temp;
	}
	
	
}
