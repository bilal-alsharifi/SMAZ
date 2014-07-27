/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hoodDisambiguator;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Lama
 */
public class Term
{
    String wordLemma;
    POS wordPos;
    String wordStem;
    List<IWord> sences;
    int wordIndex;
    List<String> Nouns =  new ArrayList<String>();
    List<String> Verbs = new ArrayList<String>();
    List<String> Adjectives = new ArrayList<String>();
    List<String> Adverbs = new ArrayList<String>();

    public Term ()
    {
        Nouns.add("NN");  Nouns.add("NNP");  Nouns.add("NNPS");   Nouns.add("NNS");
        Verbs.add("VB");  Verbs.add("VBD");  Verbs.add("VBG");   Verbs.add("VBP");   Verbs.add("VBN");   Verbs.add("VBZ");
        Adjectives.add("JJ");  Adjectives.add("JJR");  Adjectives.add("JJS");
        Adverbs.add("RB");    Adverbs.add("RBR");   Adverbs.add("RBS");
    }

    public void set_word (IDictionary dict, String token,String pos, String lemma, int index)
    {
    	wordLemma = lemma;
    	wordStem = token;
        wordIndex = index;
    	sences = new ArrayList<IWord>();
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
    }
}
