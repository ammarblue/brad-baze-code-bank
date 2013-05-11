package com.software.reuze;
import com.software.reuze.f_StdInBits;
import com.software.reuze.f_StdOutBits;

/*************************************************************************
 *  Compilation:  javac RunLength.java
 *  Execution:    java RunLength - < input.txt   (compress)
 *  Execution:    java RunLength + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using
 *  run-length encoding.
 *
 *  % java BinaryDump 40 < 4runs.bin 
 *  0000000000000001111111000000011111111111
 *  40 bits
 *
 *  This has runs of 15 0s, 7 1s, 7 0s, and 11 1s.
 *
 *  % java RunLength - < 4runs.bin | java HexDump
 *  0f 07 07 0b
 *  4 bytes
 *
 *************************************************************************/

public class cc_RunLength {
    private static final int R = 256;

    public static void expand() { 
        boolean b = false; 
        while (!f_StdInBits.isEmpty()) {
            char run = f_StdInBits.readChar();
            for (int i = 0; i < run; i++)
                f_StdOutBits.write(b);
            b = !b;
        }
        f_StdOutBits.close();
    }

    public static void compress() { 
        char run = 0; 
        boolean old = false;
        while (!f_StdInBits.isEmpty()) { 
            boolean b = f_StdInBits.readBoolean();
            if (b != old) {
                f_StdOutBits.write(run);
                run = 1;
                old = !old;
            }
            else { 
                if (run == R-1) { 
                    f_StdOutBits.write(run);
                    run = 0;
                    f_StdOutBits.write(run);
                }
                run++;
            } 
        } 
        f_StdOutBits.write(run);
        f_StdOutBits.close();
    }


    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
