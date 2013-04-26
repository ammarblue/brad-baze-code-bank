import java.util.ArrayList;

public class m_Idw_Error {
	ArrayList[] lis = new ArrayList[10];

	m_Idw_Error() {
		for (int i = 0; i <lis.length; i++) {
			lis[i] = new ArrayList<Double>();
		}
	}

	public double[] _Mae() {
		double mae[] = new double[lis.length-1];
		for (int j = 1; j <lis.length; j++) {
			for(int i=0;i<lis[0].size()-1;i++){
				mae[j-1] += Math.abs(Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString()));
			}
			mae[j-1]/=lis[0].size();
		}
		return mae;
	}

	public double[] _Rmse() {
		double[] rmse = new double[lis.length-1];
		for (int j = 1; j <lis.length; j++) {
			for(int i=0;i<lis[0].size()-1;i++){
				rmse[j-1] += (Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString())) * (Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString()));
			}
			rmse[j-1] /= lis[0].size();
			rmse[j-1] = Math.sqrt(rmse[j-1]);
		}
		return rmse;
	}

	public double _Mbe(double n, double i[], double o[]) {
		double mbe = 0;
		for (int j = 0; j < n; j++) {
			mbe += (i[j] - o[j]);
		}
		return mbe;
	}

	public double[] _Mare() {
		double[] mare = new double[lis.length-1];
		for (int j = 1; j < lis.length; j++) {
			for(int i=0;i<lis[0].size()-1;i++){
				mare[j-1] += Math.abs(Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString())) / Double.parseDouble(lis[0].get(i).toString());
			}
			mare[j-1] /= lis[0].size();
		}
		return mare;
	}

	public double _Msre(double n, double i[], double o[]) {
		double msre = 0;
		for (int j = 0; j < n; j++) {
			msre += ((i[j] - o[j]) * (i[j] - o[j])) / o[j];
		}
		msre /= n;
		return msre;
	}

	public double[] _Mse() {
		double mse[] = new double[lis.length-1];
		for (int j = 1; j < lis.length; j++) {
			for(int i=0;i<lis[0].size()-1;i++){
				mse[j-1] += (Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString())) * (Double.parseDouble(lis[j].get(i).toString()) - Double.parseDouble(lis[0].get(i).toString()));
			}
			mse[j-1]/=lis[0].size();
		}
		return mse;
	}

	public double _Rmsre(double n, double i[], double o[]) {
		double rmsre = 0;
		for (int j = 0; j < n; j++) {
			rmsre += ((i[j] - o[j]) * (i[j] + o[j])) / o[j];
		}
		rmsre /= n;
		rmsre = Math.sqrt(rmsre);
		return rmsre;
	}
}
