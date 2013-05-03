import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import processing.core.PApplet;
import processing.core.PFont;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/87962*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */

public class Tagger extends PApplet implements Console.Application{
	private TagSet	 Tags;
	private Dictionary Lexicon;
	private Bigram	 LanguageModel;
	
	public Tagger()
	{
		InputStream tag_input_stream = getClass().getResourceAsStream("data/TagSet.txt");
		InputStream dict_input_stream = getClass().getResourceAsStream("data/Dictionary.txt");
		InputStream bigram_input_stream = getClass().getResourceAsStream("data/Bigram.txt");
		if (tag_input_stream != null &&
			dict_input_stream != null &&
			bigram_input_stream != null)
		{
			InputStreamReader tag_input_isr = new InputStreamReader(tag_input_stream);
			InputStreamReader dict_input_isr = new InputStreamReader(dict_input_stream);
			InputStreamReader bigram_input_isr = new InputStreamReader(bigram_input_stream);
			Tags = new TagSet(tag_input_isr);
			Lexicon		  = new Dictionary(Tags);
			LanguageModel = new Bigram(Tags);
			Lexicon.Load(dict_input_isr);
			LanguageModel.Load(bigram_input_isr);
		}
		else
		{
			try
			{
				Reader tag_reader = new FileReader(sketchPath+"/Data/TagSet.txt");
				Reader dict_reader = new FileReader(sketchPath+"/Data/Dictionary.txt");
				Reader bigram_reader = new FileReader(sketchPath+"/Data/Bigram.txt");
				Tags		  = new TagSet(tag_reader);
				Lexicon		  = new Dictionary(Tags);
				LanguageModel = new Bigram(Tags);
				Lexicon.Load(dict_reader);
				LanguageModel.Load(bigram_reader);
			}
			catch (FileNotFoundException e){ e.printStackTrace(); System.exit(0); }
		}
	}
	
	public void Process(String sentence)
	{
		String [] tokens = Tokeniser.Tokenise(sentence);
		Word [] words = new Word[tokens.length];
		for (int i=0 ; i<tokens.length ; ++i)
		{
			String lookup_word = tokens[i].toLowerCase();
			words[i] = Lexicon.GetWord(lookup_word);

			if (words[i] == null)
			{
				long tags = GuessPennTags(tokens[i]);
				words[i] = new Word(tokens[i], tags);
			}
		}

		int [] tags = LanguageModel.Viterbi(words);

		String tagged_sentence = "";
		for (int i=0 ; i<tokens.length ; ++i)
		{
			tagged_sentence = tagged_sentence + tokens[i] + "/" + Tags.GetStringFromIndex(tags[i]) + " ";
		}
		Console.Write("\n" + tagged_sentence+"\n");
	}

	public long GuessPennTags(String word)
	{
		long bit_field = 0;

		for (int i=0 ; i < word.length() ; ++i)
		{
			if (Character.isDigit(word.charAt(i)))
			{
				bit_field |= Tags.GetBitsFromString("CD");
			}
		}
		
		if (word.endsWith("us") ||
			word.endsWith("ic") ||
			word.endsWith("ble") ||
			word.endsWith("ive") ||
			word.endsWith("ary") ||
			word.endsWith("ful") ||
			word.endsWith("ical") ||
			word.endsWith("less"))
		{
			bit_field |= Tags.GetBitsFromString("JJ");
		}
		if (word.endsWith("ed"))
		{
			bit_field |= Tags.GetBitsFromString("JJ");
			bit_field |= Tags.GetBitsFromString("VBN");
			bit_field |= Tags.GetBitsFromString("VBD");
		}

		if (Character.isUpperCase(word.charAt(0)))
		{
			bit_field |= Tags.GetBitsFromString("NNP");
			if (word.endsWith("s"))
			{
				bit_field |= Tags.GetBitsFromString("NNPS");
			}
		}
		
		if (word.endsWith("s"))
		{
			bit_field |= Tags.GetBitsFromString("NNS");
			bit_field |= Tags.GetBitsFromString("VBZ");
		}

		if (word.endsWith("ly")) bit_field |= Tags.GetBitsFromString("RB");
		if (word.endsWith("ing"))
		{
			if (word.endsWith("ly")) bit_field |= Tags.GetBitsFromString("JJ");
			bit_field |= Tags.GetBitsFromString("VBG");
		}
		if (word.indexOf('-') > -1)
		{
			// Often NN / NNS too?
			bit_field |= Tags.GetBitsFromString("JJ");
		} 
		if (bit_field == 0)  bit_field |= Tags.GetBitsFromString("NN");

		return bit_field;
	}
	
	public String TabComplete(String s)
	{
		// *** - Stanford Parser incorrectly tags this sentence.
		
		String [] strings_that_pass =
		{
			"The farm was used to produce produce.", // ***
			"The dump was so full that it had to refuse more refuse.",
			"The soldier decided to desert his dessert in the desert.",
			"I did not object to the object.",
			"We must polish the Polish furniture.",
			"The bandage was wound around the wound.",
			"Fruit flies like a banana.",
			"The government plans to raise taxes were defeated.", // ***
			"My dog also likes eating sausage.",
			"He saw the duck.",
			"He used the saw.",
			"Unknown wurds handled spiffingly.",
			"Carl's tagger don't got no problem with apostrophes.",
			"Flying planes can be dangerous."
		};
		
		String [] strings_that_fail =
		{
			"Time flies like an arrow.",						// "Flies" should be VBZ, not NNS.
			"The man whistling tunes pianos.",					// "Tunes" should be VBZ, not NNS.
			"They were too close to the door to close it.",		// First "close" should be JJ, not VB.
			"The insurance was invalid for the invalid.",		// First "invalid" should be JJ, not NN.
			"When shot at, the dove dove into the bushes.",		// Second "dove" should be VBD, not NN.
		};

		
		// Change this to strings_that_fail to see some incorrect tagging:
		String [] example_strings = strings_that_pass;

		return example_strings[(int)random(example_strings.length)];
	}

public void setup()
{
	size(640, 480);
	PFont font = loadFont("Monospaced-16.vlw");
	Tagger tagger = new Tagger();
	Console.Initialise(this, tagger, font, 16);

	Console.Write("Type a sentence to tag, or press <TAB> for an example.\n");
}

public void draw()
{
	background(0);
	stroke(255);
	
	Console.Draw();
}

public void keyPressed()
{
	Console.KeyPressed();
}
}