package leskDisambiguator;

import java.util.ArrayList;
import java.util.List;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.Pointer;
import edu.stanford.nlp.ling.CoreLabel;

public class Lesk 
{
	private static void getResult (List<Word> instanceWords, List<CoreLabel> orginalWords, IDictionary dict, List<IWord> result)
	{
		for (int i = 0 ; i< orginalWords.size() ; i++)
		  {
			int j = 0;
			while (j < instanceWords.size())
			{
				if (orginalWords.get(i).lemma().equals(instanceWords.get(j).wordLemma))
				{
					result.add(dict.getWord(instanceWords.get(j).bestSenceIDs.get(0)));
					j = orginalWords.size()+1;
				}
				else
					j++;
			}
			if (j == instanceWords.size())
				result.add(null);
		  }
	}
	
	public static List<IWord> disambiguate (List<CoreLabel> words, IDictionary dict,int windowSize, int comparsionWay, List<Pointer> relations)
	{
		List<IWord> result = new ArrayList<>();
		List<Word> sentenceWords = new ArrayList<>();
		Instance ins = new Instance();
		ins.set_instance(windowSize, comparsionWay, relations);
		int counter = 0;
		int tokensCount = 0;
		for (CoreLabel token: words)
	      {
	    	  tokensCount++;
	    	  Word word = new Word();
		      word.set_word(dict, token.tag(), token.lemma());

		      if (!word.sences.isEmpty())  
		      {
		    	  ins.words.add(word);
		    	  counter++;
		      }

		      if ((counter == ins.windowSize) || (counter > 1 && counter < ins.windowSize && tokensCount == words.size()))
		      {
		    	  //if (counter == 1)
		    	  	//get common sense 
		   		  ins.createCombinations(dict);
		   		  List<String> comparedGlosses = new ArrayList<>();
		   		  ins.select_BestCombinations(dict , comparedGlosses);
		   		  sentenceWords.addAll(ins.words);
	    		  ins = new Instance();
	    	      ins.set_instance(windowSize, comparsionWay, relations);
		    	  counter = 0;
	    	  }		      
	       }
		getResult(sentenceWords, words ,dict, result);
		return result;
	}

}
