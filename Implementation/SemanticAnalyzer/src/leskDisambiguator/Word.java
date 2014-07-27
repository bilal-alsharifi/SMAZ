package leskDisambiguator;

import java.util.ArrayList;
import java.util.List;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;

public class Word {

	POS wordPos;
	String wordLemma;
	List<IWord> sences;
	List<IWordID> bestSenceIDs;
	List<String> Nouns;
    List<String> Verbs;
    List<String> Adjectives;
    List<String> Adverbs;
    
    public void set_word (IDictionary dict, String pos, String lemma)
    {
    	wordLemma = lemma; 	
    	sences = new ArrayList<>();    	
    	bestSenceIDs = new ArrayList<>();
    	Nouns =  new ArrayList<>();
        Verbs = new ArrayList<>();
        Adjectives = new ArrayList<>();
        Adverbs = new ArrayList<>();
    	Nouns.add("NN");  Nouns.add("NNP");  Nouns.add("NNPS");   Nouns.add("NNS");
	    Verbs.add("VB");  Verbs.add("VBD");  Verbs.add("VBG");   Verbs.add("VBP");   Verbs.add("VBN");   Verbs.add("VBZ");
	    Adjectives.add("JJ");  Adjectives.add("JJR");  Adjectives.add("JJS");
	    Adverbs.add("RB");    Adverbs.add("RBR");   Adverbs.add("RBS");
	    if (Nouns.contains(pos))
	    {
	    	wordPos = POS.NOUN;
	    }
	    else if (Verbs.contains(pos))
	    {
	    	wordPos = POS.VERB;
	    }
	    else if (Adjectives.contains(pos))
	    {
	    	wordPos = POS.ADJECTIVE;
	    }
	    else if (Adverbs.contains(pos))
	    {
	    	wordPos = POS.ADVERB;
	    }
	    else
	    	wordPos = null;
	    if (wordPos != null)
	    	get_sences(dict);
    }
    
    public void get_sences (IDictionary dict)
    {
	    IIndexWord idxWord = dict.getIndexWord (wordLemma, wordPos);
	    if (idxWord != null)
	    {
			List<IWordID> wordIDs = idxWord.getWordIDs();
			for (int i = 0; i < wordIDs.size(); i++) 
			{ 
				IWord word = dict.getWord(wordIDs.get(i)); 
			    sences.add(word);
			}
	    }
    }


}
