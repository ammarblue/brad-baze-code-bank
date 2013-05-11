package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac KWIK.java
 *  Execution:    java KWIK file.txt
 *  Dependencies: StdIn.java In.java
 *  
 *
 *  %  java KWIK tale.txt 15
 *  majesty
 *   most gracious majesty king george th
 *  rnkeys and the majesty of the law fir
 *  on against the majesty of the people 
 *  se them to his majestys chief secreta
 *  h lists of his majestys forces and of
 *
 *  the worst
 *  w the best and the worst are known to y
 *  f them give me the worst first there th
 *  for in case of the worst is a friend in
 *  e roomdoor and the worst is over then a
 *  pect mr darnay the worst its the wisest
 *  is his brother the worst of a bad race 
 *  ss in them for the worst of health for 
 *   you have seen the worst of her agitati
 *  cumwented into the worst of luck buuust
 *  n your brother the worst of the bad rac
 *   full share in the worst of the day pla
 *  mes to himself the worst of the strife 
 *  f times it was the worst of times it wa
 *  ould hope that the worst was over well 
 *  urage business the worst will be over i
 *  clesiastics of the worst world worldly 
 *************************************************************************/

public class da_StringKWIK { 

    public static void main(String[] args) { 
        int context = Integer.parseInt(args[1]);

        // read in text
        String text = args[0].replaceAll("\\s+", " ");
        int N = text.length();

        // build suffix array
        da_StringSuffixArray sa = new da_StringSuffixArray(text);

        // find all occurrences of queries and give context
        while (f_StdIn.hasNextLine()) {
            String query = f_StdIn.readLine();
            for (int i = sa.rank(query); i < N && sa.select(i).startsWith(query); i++) {
                 int from = Math.max(0, sa.index(i) - context);
                 int to   = Math.min(N-1, from + query.length() + 2*context);
                 System.out.println(text.substring(from, to));
            }
            System.out.println();
        }
    } 
} 

