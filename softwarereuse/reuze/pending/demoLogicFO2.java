package reuze.pending;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.software.reuze.m_ProofStep;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_InferenceProcedureBCAsk;
import com.software.reuze.mf_InferenceProcedureFCAsk;
import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_i_InferenceResult;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Proof;

public class demoLogicFO2 {

	private static enum InferenceProcedureType{ForwardChaining,BackwardChaining};
	
	private static mf_Domain CreateSprinterDomain()
	{
		mf_Domain domain = new mf_Domain();
		
		domain.addConstant("Bolt");
		domain.addConstant("SlowPoke");
		domain.addPredicate("Sprinter");
		domain.addPredicate("Fastest");		
		domain.addPredicate("Winner");
		domain.addPredicate("Loser");
		domain.addPredicate("Slowest");
		return domain;
	}
	
	
	
	private static mf_KnowledgeBase CreateSprinterKnowledgeBase(InferenceProcedureType inferenceProcedureType)
	{
		mf_KnowledgeBase kb = null;
		
		switch(inferenceProcedureType)
		{
			case ForwardChaining:
				mf_InferenceProcedureFCAsk infpFC = new mf_InferenceProcedureFCAsk();
				kb = new mf_KnowledgeBase(CreateSprinterDomain(),infpFC);
				break;
			case BackwardChaining:
				mf_InferenceProcedureBCAsk infpBC = new mf_InferenceProcedureBCAsk();
				kb = new mf_KnowledgeBase(CreateSprinterDomain(),infpBC);
				break;
		}
		
		//Add the logic		
		kb.tell("((Sprinter(x) AND Fastest(x)) => Winner(x))");
		kb.tell("((Sprinter(x) AND Slowest(x)) => Loser(x))");
		kb.tell("Sprinter(SlowPoke)");
		kb.tell("Sprinter(Bolt)");
		kb.tell("Fastest(Bolt)");
		kb.tell("Slowest(SlowPoke)");
		
		
		return kb;
	}
	
	public static void displayQuestions()
	{
		System.out.println("1 - Did Bolt win the race?");
		System.out.println("2 - Did SlowPoke lose the race?");
		System.out.println("3 - Is Bolt the fastest Sprinter?");
		System.out.println("4 - Is SlowPoke the slowest Sprinter?");
		
		System.out.println("Enter the number to ask a question.  Press Q to quit. :");
		
	}
	
	public static mf_Predicate getQuestionAsPredicate(int questionNumber)
	{
		mf_Predicate query = null;
		
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		
		switch(questionNumber)
		{
			case 1: //Did Bolt win the race?
				terms.add(new mf_SymbolConstant("Bolt"));
				query = new mf_Predicate("Winner", terms);
				break;
				
			case 2: //Did SlowPoke lose the race?
				terms.add(new mf_SymbolConstant("SlowPoke"));
				query = new mf_Predicate("Loser", terms);
				break;
				
			case 3: //Is Bolt the fastest sprinter?
				terms.add(new mf_SymbolConstant("Bolt"));
				query = new mf_Predicate("Fastest", terms);
				break;
				
			case 4: //Is SlowPoke the slowest sprinter?
				terms.add(new mf_SymbolConstant("SlowPoke"));
				query = new mf_Predicate("Slowest", terms);
				break;
				
		}
		return query;
	}
	
	private static String readFromConsole()
	{
		Scanner in = new Scanner(System.in);
		
		return in.next();
	}
	
	public static void main(String[] args)
	{			
		
		boolean exitDemo = false;
		
		mf_KnowledgeBase kb = CreateSprinterKnowledgeBase(InferenceProcedureType.BackwardChaining);
				
		while (!exitDemo)
		{
			displayQuestions();
			
			String selection = readFromConsole();
			
			if (selection.equalsIgnoreCase("Q"))
			{
				exitDemo = true;
			}
			else
			{
				mf_Predicate query = getQuestionAsPredicate(Integer.parseInt(selection));				
				
				mf_i_InferenceResult answer = kb.ask(query);
				
				if (answer.isTrue())
				{
					System.out.println("Yes.  See proof steps below:");
					
					for(mf_i_Proof proof : answer.getProofs())
					{
						for(m_ProofStep step: proof.getSteps())
						{
							System.out.println(step.getProof());
						}
					}
				}
				else
				{
					System.out.println("No");
				}
			}
			
			System.out.println("");
		}
	}
}