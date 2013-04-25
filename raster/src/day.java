import java.util.ArrayList;

public class day {
	public ArrayList<point> points;

	public day() {
		this.points = new ArrayList<point>();
	}
	public void sort(){
		point temp=new point();
		for(int i=0;i<points.size()-1;){
			if(points.get(i).x>points.get(i+1).x){
				temp=points.get(i);
				points.set(i,points.get(i+1));
				points.set(i+1,temp);
				i=0;
			}else{
				i++;
			}
		}
	}
	public void printOut(){
		for(int i=0;i<points.size();i++){
			System.out.println("x="+points.get(i).x+" y="+points.get(i).y+" t="+points.get(i).day+" v="+points.get(i).value);
		}
	}
}
