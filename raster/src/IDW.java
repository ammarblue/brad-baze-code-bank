import java.util.ArrayList;

public class IDW {
	day[] year;
	double x, y, v;
	int n, p,t;
	point[] nab;

	IDW() {
	}

	IDW(day[] in) {
		year = in;
	}

	IDW(day[] in, double x, double y,int t, int n, int p) {// n number of
																// nab
		year = in;
		this.x = x;
		this.y = y;
		this.t = t;
		this.n = n;
		this.p = p;
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
			while (s < n) {
				if (year[t].points.get(i).d < N[s].d) {
					N[s] = year[t].points.get(i);
					break;
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
			System.out.println("Nab" + i + " x=" + nab[i].x + " y=" + nab[i].y
					+ " t=" + nab[i].day + " d=" + nab[i].d + " v="
					+ nab[i].value);
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
}
