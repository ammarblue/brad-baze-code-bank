package com.software.reuze;

public class ff_WMFMetaRecord extends Object {
    
    private   int         rdSize;
    private   short       rdFunction;
    private   byte[]      rdParm;


    
    public ff_WMFMetaRecord(int rdSize, short rdFunction, byte[] rdParm) {
        this.rdSize = rdSize;
        this.rdFunction = rdFunction;
        this.rdParm = rdParm; // arraycopy
        //rdParm = new byte[4];
    }
    
    public void initialize(int rdSize, short rdFunction, byte[] rdParm) {
        this.rdSize = rdSize;
        this.rdFunction = rdFunction;
        this.rdParm = rdParm; // arraycopy
        //rdParm = new byte[4];
    }

    public int getSize() {
        return rdSize;
    }
   
    public short getFunction() {
        return rdFunction;
    }

    public byte[] getParm() {
        return rdParm;
    }

}