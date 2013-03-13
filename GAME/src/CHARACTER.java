
public class CHARACTER {
	public String NAME;
	public String RACE;
	public String CLASS;
	public int EXPERIENCE;
	public int LEVEL;
	
	public CHARACTER(String charCode){//import char from char code
		this.NAME = CHARACTER_CODE.getName(charCode);
		this.RACE = CHARACTER_CODE.getRace(charCode);
		this.CLASS = CHARACTER_CODE.getClass1(charCode);
		this.EXPERIENCE = CHARACTER_CODE.getExperience(charCode);
		this.LEVEL = LEVEL_CALCULATOR.getLevel(this.EXPERIENCE);
	}
	public CHARACTER(String name, String race, String class1){//new char
		this.NAME = name;
		this.RACE = race;
		this.CLASS = class1;
		this.EXPERIENCE = 0;
		this.LEVEL = LEVEL_CALCULATOR.getLevel(this.EXPERIENCE);
	}
	
	public String getName(){
		return this.NAME;
	}
	public String getRace(){
		return this.RACE;
	}
	public String getClass1(){
		return this.CLASS;
	}
	public int getExperience(){
		return this.EXPERIENCE;
	}
	public int getLevel(){
		return this.LEVEL;
	}
}
