package reuze.pending;

/* Artificial Intelligence */
/* Travis Losser - Assignment P5: Demo for first order logic. */
/* Date: 10/13/2012 */

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

import com.software.reuze.mf_Domain;
import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_InferenceResultPrinter;
import com.software.reuze.mf_InferenceTFMResolution;
import com.software.reuze.mf_InferenceProcedureModelElimination;
import com.software.reuze.mf_OTTERLikeTheoremProver;
import com.software.reuze.mf_i_InferenceProcedure;
import com.software.reuze.mf_i_InferenceResult;

public class demoLogicFO {
	
	private mf_KnowledgeBase knowledgeBase;
	private mf_i_InferenceProcedure infp;
	private mf_Domain domain;
	private ArrayList<String> sentences;
	private ArrayList<String> queries;
	
	public demoLogicFO() {
		this(new mf_InferenceTFMResolution(10 * 1000));
	}
	
	public demoLogicFO(mf_i_InferenceProcedure inferenceProcedure) {
		domain = new mf_Domain();
		sentences = new ArrayList<String>();
		queries = new ArrayList<String>();
		this.infp = inferenceProcedure;
	}
	
	public mf_KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
	public void readFirstOrderLogicFile(Scanner br) {
		String strLine;
		// Read the file line by line
			while ((strLine = br.nextLine()) != null) {
				StringTokenizer st = new StringTokenizer(strLine, ":");
				while(st.hasMoreTokens()) {
					String key = st.nextToken();
					if (key.equalsIgnoreCase("done")) return;
					String value = st.nextToken();
					
					// Set up the Domain.  Add sentences for the KnowledgeBase to an ArrayList. Add queries to ArrayList.
					if(key.equalsIgnoreCase("Domain-Predicate")) {
						domain.addPredicate(value);
					}
					else if(key.equalsIgnoreCase("Domain-Constant")) {
						domain.addConstant(value);
					}
					else if(key.equalsIgnoreCase("Domain-Function")) {
						domain.addFunction(value);
					}
					else if(key.equalsIgnoreCase("Sentence")) {
						sentences.add(value);
					}
					else if(key.equalsIgnoreCase("Query")) {
						queries.add(value);
					}
				}
			}

	}
	
	public void createFOLKnowledgeBase() {
		knowledgeBase = new mf_KnowledgeBase(domain, infp);
		// loop through the sentences and add to the KnowledgeBase.
		for(String s : sentences) {
			knowledgeBase.tell(s);
		}
		System.out.println();
		System.out.println("Knowledge Base");
		System.out.println("-----------------");
		System.out.println(knowledgeBase.toString());
		System.out.println();
	}
	
	public void queryFOLKnowledgeBase() {
		for(String q : queries) {
			System.out.println("*********************************************************************");
			System.out.println("Query: " + q);
			mf_i_InferenceResult answer = knowledgeBase.ask(q);
			String queryResult = mf_InferenceResultPrinter.printInferenceResult(answer);
			System.out.println(queryResult);
		}
	}
	
	public static void folKnowledgeBaseQuery(Scanner in, mf_i_InferenceProcedure infp) {
		demoLogicFO fol = new demoLogicFO(infp);
		
		fol.readFirstOrderLogicFile(in);
		fol.createFOLKnowledgeBase();
		fol.queryFOLKnowledgeBase();
	}
    static String test="Domain-Constant:A\n"+
"Domain-Constant:B\n"+
"Domain-Constant:C\n"+
"Sentence:B = A\n"+
"Sentence:B = C\n"+
"Sentence:x = x\n"+
"Sentence:(x = y => y = x)\n"+
"Sentence:((x = y AND y = z) => x = z)\n"+
"Query:A = C\ndone\n";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream in=null;
		if (args.length>0) {
			try {
				in=new FileInputStream(args[0]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else in=System.in;
		System.out.println("Inference Procedure Model Elimination");
		System.out.println("______________________________________________________________________________");
		demoLogicFO.folKnowledgeBaseQuery(new Scanner(test), new mf_InferenceProcedureModelElimination());
		
		System.out.println("\n\n\n");
		System.out.println("OTTER Like Theorem Prover");
		System.out.println("______________________________________________________________________________");
		demoLogicFO.folKnowledgeBaseQuery(new Scanner(test), new mf_OTTERLikeTheoremProver());
		
		System.out.println("\n\n\n");
		System.out.println("InferenceTFMResolution");
		System.out.println("______________________________________________________________________________");
		demoLogicFO.folKnowledgeBaseQuery(new Scanner(test), new mf_InferenceTFMResolution());
	}
}
