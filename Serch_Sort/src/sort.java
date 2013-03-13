
public class sort {
	int nums[];
	int numssize;
	sort(){}
	void setNums(int in[]){
		nums=new int[in.length];
		numssize=nums.length;
		nums=in;
		upsort(nums,1,numssize);
		for(int i=0;i<nums.length;i++){
			System.out.println(nums[i]);
		}
	}
	void upsort(int a[],int min,int max){
		int temp[];
		int index1,left,right;
		if(max==min){
			return;
		}
		int size=max-min+1;
		int pivot=(min+max)/2;
		temp=new int[size];
		upsort(a,min,pivot);
		upsort(a,pivot+1,max);
		for(index1=0;index1<size;index1++){
			temp[index1]=a[min+index1];
		}
		left=0;
		right=pivot-min+1;
		for(index1=0;index1<size;index1++){
			if(right<=max-min){
				if(left<=pivot-min){
					if(temp[left]!=temp[right]){
						a[index1+min]=temp[right++];
					}else{
						a[index1+min]=temp[left++];
					}
				}else{
					a[index1+min]=temp[right++];
				}
			}else{
				a[index1+min]=temp[left++];
			}
		}
	}
}
