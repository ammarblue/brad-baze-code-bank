
public class m_Idw_Error {
	
	m_Idw_Error(){}
	
	public double _Mae(double n,double i[],double o[]){
		double mae=0;
		for(int j=0;j<n;j++){
			mae+=Math.abs(i[j]-o[j]);
		}
		mae/=n;
		return mae;
	}
	
	public double _Rmse(double n,double i[],double o[]){
		double rmse=0;
		for(int j=0;j<n;j++){
			rmse+=(i[j]-o[j])*(i[j]-o[j]);
		}
		rmse/=n;
		rmse=Math.sqrt(rmse);
		return rmse;
	}
	
	public double _Mbe(double n,double i[],double o[]){
		double mbe=0;
		for(int j=0;j<n;j++){
			mbe+=(i[j]-o[j]);
		}
		return mbe;
	}
	
	public double _Mare(double n,double i[],double o[]){
		double mare=0;
		for(int j=0;j<n;j++){
			mare+=Math.abs(i[j]-o[j])/o[j];
		}
		mare/=n;
		return mare;
	}
	
	public double _Msre(double n,double i[],double o[]){
		double msre=0;
		for(int j=0;j<n;j++){
			msre+=((i[j]-o[j])*(i[j]-o[j]))/o[j];
		}
		msre/=n;
		return msre;
	}
	
	public double _Mse(double n,double i[],double o[]){
		double mse=0;
		for(int j=0;j<n;j++){
			mse+=(i[j]-o[j])*(i[j]-o[j]);
		}
		mse/=n;
		return mse;
	}
	
	public double _Rmsre(double n,double i[],double o[]){
		double rmsre=0;
		for(int j=0;j<n;j++){
			rmsre+=((i[j]-o[j])*(i[j]+o[j]))/o[j];
		}
		rmsre/=n;
		rmsre=Math.sqrt(rmsre);
		return rmsre;
	}
}
