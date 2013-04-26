
public class IDWanimation {
	day[] year;
	double[] px,py;
	IDWanimation(){}
	IDWanimation(day[] in){
		year=in;
	}
	IDWanimation(day[] in,double[] pointsx,double[] pointsy){//give an array of all x values, and array of all y and this main yeare you created in mainframe
		year=in;
		px=pointsx;
		py=pointsy;
	}
	public day step(int t,int p,int n){//t is time, p is the power default should be 2, n should be 1,3 or 5 
		day temp=new day();
		for(int i=0;i<px.length;i++){
			IDW dw=new IDW(year,px[i],py[i],t,n,p);
			temp.points.add(new point(px[i],py[i],t,dw.idw()));
		}
		return temp;//just returns an instanse of day with all the information for one time instance
	}
}
