import java.util.ArrayList;

public class IDW {
	day[] year;
	double x, y, v;
	int n, p, t;
	point[] nab;

	IDW() {
	}

	IDW(day[] in) {
		year = in;
	}

	IDW(day[] in, double x, double y, int t, int n, int p) {// n number of
															// nab
		year = in;
		this.x = x;
		this.y = y;
		this.t = t;
		this.n = n;
		this.p = p;
	}
	IDW(day[] in, double x,double y){
		year=in;
		this.x=x;
		this.y=y;
	}

	public day[] getYear() {
		return year;
	}

	public void setYear(day[] year) {
		this.year = year;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
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
			N[i] = year[t].points.get(i);
			N[i].d = dis(N[i].x, N[i].y);
		}
		for (int i = 0; i < year[t].points.size(); i++) {
			int s = 0;
			year[t].points.get(i).d = dis(year[t].points.get(i).x,
					year[t].points.get(i).y);
			if(year[t].points.get(i).d==0){
				break;
			}
			while (s < n) {
				if (year[t].points.get(i).d < N[s].d ) {
					for(int z=s;z<n;z++){
						if(N[z].d==N[s].d){
							s=n;
							break;
						}
					}
					if(s<n){
						N[s] = year[t].points.get(i);
					}
				} else {
					s++;
					continue;
				}
			}
		}
		nab = N;
	}

	public double dis(double Ax, double Ay) {
		return Math.sqrt(Math.pow((Ax - x), 2) + Math.pow((Ay - y), 2));
	}

	public double di(double Ax, double Ay, double At) {
		return Math.sqrt(Math.pow((Ax - x), 2) + Math.pow((Ay - y), 2));
	}

	public double idw() {
		findN();
		v = 0;
		for (int i = 0; i < nab.length; i++) {
			/*System.out.println("Nab" + i + " x=" + nab[i].x + " y=" + nab[i].y
					+ " t=" + nab[i].day + " d=" + nab[i].d + " v="
					+ nab[i].value);*/
			v += lambda(i) * nab[i].value;
		}
		return v;
	}

	public double lambda(int i) {
		double L = Math.pow((1 / di(nab[i].x, nab[i].y, nab[i].day)), p);
		double l = 0;
		for (int k = 0; k < n; k++) {
			l += Math.pow((1 / di(nab[k].x, nab[k].y, nab[k].day)), p);
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
