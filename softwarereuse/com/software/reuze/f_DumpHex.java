package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac HexDump.java
 *  Execution:    java HexDump < file
 *  Dependencies: BinaryStdIn.java
 *  
 *  Reads in a binary file and writes out the bytes in hex, 16 per line.
 *
 *  % java HexDump < input.txt
 *
 *  % hexdump < input.txt
 *  % od -t x1 < input.txt
 *
 *************************************************************************/

public class f_DumpHex {

    public static void main(String[] args) {
        int BYTES_PER_LINE = 16;
        if (args.length == 1) {
            BYTES_PER_LINE = Integer.parseInt(args[0]);
        }

        int i;
        for (i = 0; !f_StdInBits.isEmpty(); i++) {
            if (BYTES_PER_LINE == 0) { f_StdInBits.readChar(); continue; }
            if (i == 0) System.out.printf("");
            else if (i % BYTES_PER_LINE == 0) System.out.printf("\n", i);
            else System.out.print(" ");
            char c = f_StdInBits.readChar();
            System.out.printf("%02x", c & 0xff);
        }
        if (BYTES_PER_LINE != 0) System.out.println();
        System.out.println((i*8) + " bits");
    }
}
