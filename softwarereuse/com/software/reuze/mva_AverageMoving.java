package com.software.reuze;


public class mva_AverageMoving {

		ga_Vector2[] m_History;
		int m_iNextUpdateSlot;

		//running sum of all elements
		ga_Vector2 sum;

		//to instantiate a Smoother, pass it the number of samples you want
		//to use in the smoothing, and an example of a 'zero' type
		public mva_AverageMoving(int SampleSize) {
			m_iNextUpdateSlot = 0;
			m_History = new ga_Vector2[SampleSize];
			for(int i = 0; i < SampleSize; i++){
				m_History[i] = new ga_Vector2();
			}
			sum=new ga_Vector2();
		}
		
		public void clear() {
			for (ga_Vector2 v:m_History) v.set(0,0);
		}
		
		public final ga_Vector2 average() {
			return tmp.set(sum).div((float)m_History.length);
		}

		//each time you want to get a new average, feed it the most recent value
		//and this method will return an average over the last SampleSize updates
		ga_Vector2 tmp=new ga_Vector2();
		public final ga_Vector2 update(final ga_Vector2 MostRecentValue)
		{  
			//make sure m_iNextUpdateSlot wraps around.
			m_iNextUpdateSlot = (m_iNextUpdateSlot+1)%m_History.length;
			//subtract out old value and add new value to update the sum
			sum.sub(m_History[m_iNextUpdateSlot]).add(MostRecentValue);
			//overwrite the oldest value with the newest
			m_History[m_iNextUpdateSlot].set(MostRecentValue);

			//now to calculate the average of the history list			
			return tmp.set(sum).div((float)m_History.length);
		}
		public static void main(String args[]) {
			mva_AverageMoving asv=new mva_AverageMoving(4);
			for (int i=0; i<10; i++) System.out.println(asv.update(new ga_Vector2(i,i)));
		}
}
