public class CHARACTER_CODE {
	String characterCode;
	static //max num of characters in each of these variables
	int maxNameLength = 15;
	static int maxRaceLength = 8;
	static int maxClassLength = 12;
	static int maxExperienceLength = 9;
	
	public static String generateCode(CHARACTER c){
		StringBuilder code = new StringBuilder();
		String finalCode = "";
		for (int i = 0; i < maxNameLength;i++){
			if(i < c.getName().length()){
				code.append( c.getName().charAt(i));
			}else{
				code.append('$');
			}
		}
		for (int i = 0; i < maxRaceLength;i++){
			if(i < c.getRace().length()){
				code.append(c.getRace().charAt(i));
			}else{
				code.append('$');
			}
		}
		
		for (int i = 0; i < maxClassLength;i++){
			if(i < c.getClass1().length()){
				code.append( c.getClass1().charAt(i));
			}else{
				code.append('$');
			}
		}
		for (int i = 0; i < maxExperienceLength;i++){
			if(i < Integer.toString(c.getExperience()).length()){
				code.append(Integer.toString(c.getExperience()).charAt(i));
			}else{
				code.append('$');
			}
		}
		
		
		finalCode = code.toString();
		return finalCode;
		
	}
	
	public static String getName(String code){
		String name = code.substring(0, 15);
		name = name.replace("$", "");
		return name;
	}
	public static String getRace(String code){
		String race = code.substring(15, 23);
		race = race.replace("$", "");
		return race;
	}
	public static String getClass1(String code){
		String class1 = code.substring(23, 35);
		class1 = class1.replace("$", "");
		return class1;
	}
	public static int getExperience(String code){
		String expStr = code.substring(35,code.length());
		expStr = expStr.replace("$", "");
		int expInt = Integer.parseInt(expStr);
		return expInt;
	}
	
}
