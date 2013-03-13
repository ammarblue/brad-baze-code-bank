
public class bag {
	Object[] bag_array;
	int count=0;
	int point=0;
	bag(){}
	bag(int size){
		bag_array=new Object[size];
	}
	void setBagSize(int size){
		bag_array=new Object[size];
	}
	void add(Object in){
		bag_array[count]=in;
		count++;
	}
	void remove(Object in){
		if(contains(in)){
			bag_array[point]=bag_array[count];
			bag_array[count]=null;
			point=0;
			count--;
		}else{
			System.err.println("NO SUCH OBJECT");
		}
	}
	void removeRandom(){
		point=(int)(Math.random()*count);
		bag_array[point]=bag_array[count];
		bag_array[count]=null;
		point=0;
		count--;
	}
	boolean isEmpty(){
		return(count==0);
	}
	boolean contains(Object in){
		for(int i=0;i<bag_array.length;i++){
			if(bag_array[i]==in){
				point=i;
				return(true);
			}else{
				return(false);
			}
		}
		return(false);
	}
	int size(){
		return(count);
	}
	void addAll(bag in){
		if(this.bag_array.length>=in.bag_array.length*2){
			for(int i=this.count;i<this.count+in.count;i++){
				this.bag_array[i]=in.bag_array[in.count-i];
			}
		}
	}
	void union(){
		
	}
	boolean equals(bag in){
		if(this.count==in.count){
			for(int i=0;i<this.count;i++){
				if(this.contains(in.bag_array[i])){
					continue;
				}else{
					return(false);
				}
			}
			return(true);
		}else{
			return(false);
		}
	}
}
