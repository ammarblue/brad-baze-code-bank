import java.util.ArrayList;

public class IDW {
	day[] year;
	double y, x, v;
	int n, p, t;
	point[] nab;

	IDW() {
	}

	IDW(day[] in) {
		year = in;
	}

	IDW(day[] in, double y, double x, int t, int n, int p) {// n number of
															// nab
		year = in;
		this.y = x;
		this.x = y;
		this.t = t;
		this.n = n;
		this.p = p;
	}
	IDW(day[] in, double y,double x){
		year=in;
		this.y=x;
		this.x=y;
	}

	public day[] getYear() {
		return year;
	}

	public void setYear(day[] year) {
		this.year = year;
	}

	public double getX() {
		return y;
	}

	public void setX(double y) {
		this.y = y;
	}

	public double getY() {
		return x;
	}

	public void setY(double x) {
		this.x = x;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public point[] getNab() {
		return nab;
	}

	public void setNab(point[] nab) {
		this.nab = nab;
	}

	public void findN() {
		point[] N = new point[n];
		for (int i = 0; i < n; i++) {
			N[i] = year[t].points.get((int)(Math.random()*year[t].points.size()));
			N[i].d = dis(N[i].y, N[i].x);
		}
		for(int i=0;i<year[t].points.size();i++){
			year[t].points.get(i).d = dis(year[t].points.get(i).y,
					year[t].points.get(i).x);
		}
		for (int i = 0; i < year[t].points.size(); i++) {
			if(year[t].points.get(i).d==0){
				break;
			}
			for(int j=0;j<N.length;j++){
				if (year[t].points.get(i).d < N[j].d ) {
					N[j] = year[t].points.get(i);
					break;
				}
			}
		}
		nab = N;
	}

	public double dis(double Ax, double Ay) {
		return Math.sqrt(Math.pow((Ax - y), 2) + Math.pow((Ay - x), 2));
	}

	public double di(double Ax, double Ay) {
		return Math.sqrt(Math.pow((Ax - y), 2) + Math.pow((Ay - x), 2));
	}

	public double idw() {
		findN();
		v = 0;
		for (int i = 0; i < nab.length; i++) {
			/*System.out.println("Nab" + i + " y=" + nab[i].y + " x=" + nab[i].x
					+ " t=" + nab[i].day + " d=" + nab[i].d + " v="
					+ nab[i].value);*/
			v += lambda(i) * nab[i].value;
		}
		return v;
	}
	public double idw(double y, double x, int t, int p, int n) {
		//p = 2 *weight value*
		//n = 5 *neighbors*
		this.y = y;
		this.x = x;
		this.t = t;
		this.p = p;
		this.n = n;
		
		findN();
		v = 0;
		for (int i = 0; i < nab.length; i++) {
			/*System.out.println("Nab" + i + " y=" + nab[i].y + " x=" + nab[i].x
					+ " t=" + nab[i].day + " d=" + nab[i].d + " v="
					+ nab[i].value);*/
			
			v += lambda(i) * nab[i].value;
		}
		return v;
	}

	public double lambda(int i) {
		double L = Math.pow((1 / di(nab[i].y, nab[i].x)), p);
		double l = 0;
		for (int k = 0; k < n; k++) {
			l += Math.pow((1 / di(nab[k].y, nab[k].x)), p);
		}
		L /= l;
		return L;
	}
	public double findKnown(point in){
		point temp=in;
		year[t].points.remove(in);
		double stem=idw();
		year[t].points.add(temp);
		return stem;
	}
	public String forPrint(point in){
		String temp="";
		temp+=in.value+" ";
		this.n=3;
		this.p=1;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=3;
		this.p=2;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=3;
		this.p=3;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=4;
		this.p=1;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=4;
		this.p=2;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=4;
		this.p=3;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=5;
		this.p=1;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=5;
		this.p=2;
		temp+=Double.toString(findKnown(in));
		temp+=" ";
		this.n=5;
		this.p=3;
		temp+=Double.toString(findKnown(in));
		temp+=" \n";
		return temp;
	}
}
