
public class Orders{
	int id;
	Drug A[];
	Orders(int ID){
		id=ID;
	}
	public void setDrug(int numDrugs){
		A=new Drug[numDrugs];
		for(int i=0;i<numDrugs;i++){
			A[i]=new Drug();
		}
		for(int i=0;i<numDrugs;i++){
			A[i].setDrugInfo();
		}
	}
}
