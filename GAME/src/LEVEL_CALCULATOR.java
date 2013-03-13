
public class LEVEL_CALCULATOR {
	
	public static int getLevel(int experience){
		int level = 1;
		for (int i = 1; i <= 49; i++){
			if(experience >= levelMultiplier(i)){
				level++;
			}
		}
		return level;
	}
	
	public static int levelMultiplier(int level){
		int totalExperience;
		if (level < 14){
			totalExperience = (int) (((level * 247) + level)+Math.pow(level, 2));
		}else if(level < 38 ){
			totalExperience = (int) (((level * 422) + level)+Math.pow(level, 2));
		}else{
			totalExperience = (int) (((level * 517) + level)+Math.pow(level, 2));
		}
		return totalExperience;
	}
}
