/* Ashleigh Amrine
 * CSCI 1302A
 * 9/20/2012
 */


public class Galaxy {

	private String name;
	private int num_of_stars;
	private int diam_of_galaxy;
	private Star[] stars_in_galaxy;
	
	NameGenerator rndName = new NameGenerator();
	
	
	public Galaxy(){
	}
	
	
	public Galaxy(String name, int num_of_stars, int diam_of_galaxy) {
		this.name = name;
		this.num_of_stars = num_of_stars;
		this.diam_of_galaxy = diam_of_galaxy;
		this.stars_in_galaxy = new Star[this.num_of_stars];
		
		for(int i = 0; i < num_of_stars; i++){
			String rnd_name = rndName.generateRandomString((short)(Math.random()*10), (int)(Math.random()*10), true);
			int rnd_int = (int)(Math.random()*15);
			stars_in_galaxy[i] = new Star(rnd_name, rnd_int, rnd_int, rnd_int);
		}
	}
	
	public Star[] getStars_in_galaxy(){
		return stars_in_galaxy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum_of_stars() {
		return num_of_stars;
	}
	public void setNum_of_stars(int num_of_stars) {
		this.num_of_stars = num_of_stars;
	}
	public int getDiam_of_galaxy() {
		return diam_of_galaxy;
	}
	public void setDiam_of_galaxy(int diam_of_galaxy) {
		this.diam_of_galaxy = diam_of_galaxy;
	}
	
	
}
