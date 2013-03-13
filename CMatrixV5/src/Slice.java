
public class Slice {
	int cnt,k,jb,jt;
	Slice(int i){
		cnt=i;
		k=cnt/10;
		jb=(cnt-(10*k))*10;
		jt=jb+9;
		//System.out.println(cnt+" k: "+k+" jb: "+jb+" jt: "+jt);
	}
}
