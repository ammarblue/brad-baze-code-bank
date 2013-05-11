package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac BinaryDump.java
 *  Execution:    java BinaryDump N < file
 *  Dependencies: BinaryStdIn.java
 *  
 *  Reads in a binary file and writes out the bits, N per line.
 *
 *  % java BinaryDump 16 < input.txt
 *
 *************************************************************************/

public class f_DumpBits {

    public static void main(String[] args) {
        int BITS_PER_LINE = 16;
        if (args.length == 1) {
            BITS_PER_LINE = Integer.parseInt(args[0]);
        }

        int count;
        for (count = 0; !f_StdInBits.isEmpty(); count++) {
            if (BITS_PER_LINE == 0) { f_StdInBits.readBoolean(); continue; }
            else if (count != 0 && count % BITS_PER_LINE == 0) System.out.println();
            if (f_StdInBits.readBoolean()) System.out.print(1);
            else                           System.out.print(0);
        }
        if (BITS_PER_LINE != 0) System.out.println();
        System.out.println(count + " bits");
    }
}
