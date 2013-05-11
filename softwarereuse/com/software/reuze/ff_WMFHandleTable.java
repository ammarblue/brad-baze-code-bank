package com.software.reuze;

import java.util.Hashtable;
import java.util.Vector;

public class ff_WMFHandleTable{
   private    Vector handleTable;
   private    Hashtable MRecordTable;

   public ff_WMFHandleTable(){
     Integer i;
     i = new Integer(-1);
     handleTable = new Vector();
     this.MRecordTable = new Hashtable();
   }

   public ff_WMFMetaRecord selectObject(int index){
     Integer i;
     Integer j;
     java.util.Enumeration thisVector;
     ff_WMFMetaRecord m;

      i = new Integer(-1);
        try{
//            System.out.println ("h index " + index);
            i = (Integer)handleTable.elementAt(index);
//            System.out.println ("h i     " + i);
           }  catch(StringIndexOutOfBoundsException e){
            System.err.println(e);
           }
        m = (ff_WMFMetaRecord)MRecordTable.get(i);
     return(m);
   }

   public void deleteObject(int index){
      Integer i,j;
      i = new Integer(-1);  j=i;
      try{
        j = (Integer)handleTable.elementAt(index);
       }  catch(StringIndexOutOfBoundsException e){
        System.err.println(e);
       }
      handleTable.setElementAt((Integer) i, index);
      MRecordTable.remove(j);
   }

   public void addObject(int recordValue, ff_WMFMetaRecord m){
   int index;
   Integer h;
   Integer i;

   h = new Integer(recordValue);


   i = new Integer(-1); // -1

   if (handleTable.contains(i)){  // if there is a free handle due to delete
     index = handleTable.indexOf(i);  // get the index of the deleted record
     handleTable.setElementAt(h, index); //set the new value
   }
   else{
     handleTable.addElement(h);
   }
   
   MRecordTable.put(h, m);

  }
}

