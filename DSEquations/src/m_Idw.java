
public class m_Idw {
	int _x,_y,_t,_n;
	int _points[][][];
	m_Idw(){}
	
	public double _Idw(int n,int x,int y,int t){
		double w=0;
		_x=x;
		_y=y;
		_t=t;
		_n=n;
		for(int j=0;j<_n;j++){
			w+=_Lambda(j)*_Wit(j);
		}
		return w;
	}
	
	public double _Wit(int i){
		
		return (Double)null;
	}
	
	public double _Lambda(int i){
		double lambda=0;
		lambda=1/_Di(i);
		double dk=0;
		for(int k=0;k<_n;k++){
			dk+=1/_Di(k);
		}
		lambda/=dk;
		return lambda;
	}
	
	public double _Di(int i){
		return Math.sqrt(Math.pow((i-_x), 2)+Math.pow(i-_y, 2)+Math.pow((i-_t),2));
	}
}
