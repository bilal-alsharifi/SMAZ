package helpers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;

public class JWIHelper 
{
	private static Boolean initialized = false;
	private static IDictionary dict = null;
	public static void initialize(String wordNetDictFolder, int memoryMode) throws IOException
	{
		if (!initialized)
		{
			URL url = new URL("file", null , wordNetDictFolder);
			dict = new RAMDictionary(url, memoryMode);
			dict.open();
		}
	}
	public static IDictionary getDict()
	{
		return dict;
	}
	public static List<IWord> getSynsets(String word, String mainTag)
	{
		List<IWord> result = null;
		POS pos = getJWIPosFromStanfordMainTag(mainTag);
		IIndexWord idxWord = dict.getIndexWord(word, pos);
		if (idxWord != null)
		{
			result = new ArrayList<IWord>();
			List<IWordID> wordIDs = idxWord.getWordIDs();
			for (IWordID id : wordIDs)
			{
				result.add(dict.getWord(id));
			}
		}
		return result;
	}
	public static IWord getMostCommonSynset(String word, String mainTag)
	{
		IWord result = null;
		List<IWord> synsets = getSynsets(word, mainTag);
		if (synsets != null)
		{
			result = synsets.get(0);
		}
		return result;
	}
	public static List<IWord> getSynsetsOfAnyTag(String word)
	{
		List<IWord> result = new ArrayList<IWord>();
		List<IWord> subSet = null;
		subSet = getSynsets(word, "NN");
		if (subSet != null)
		{
			result.addAll(subSet);
		}
		subSet = getSynsets(word, "VB");
		if (subSet != null)
		{
			result.addAll(subSet);
		}
		subSet = getSynsets(word, "JJ");
		if (subSet != null)
		{
			result.addAll(subSet);
		}
		subSet = getSynsets(word, "RB");
		if (subSet != null)
		{
			result.addAll(subSet);
		}
		return result;
	}
	private static POS getJWIPosFromStanfordMainTag(String mainTag)
	{
		POS result = null;
		switch (mainTag) 
		{
	        case "NN":  
	        	result = POS.NOUN;
	            break;
	        case "VB":  
	        	result = POS.VERB;
	            break;
	        case "JJ":  
	        	result = POS.ADJECTIVE;
	            break;
	        case "RB":  
	        	result = POS.ADVERB;
	            break;
	        default:
	        	result = POS.NOUN;
	        	break;              
		}
		return result;
	}
	public static List<IWord> disambiguate(List<CoreLabel> sentence)
	{
		List<IWord> result = new ArrayList<>();
		for (CoreLabel token : sentence)
		{
			String targetTokenMainTag = StanfordCoreNLPHelper.getMainTagFromTag(token.tag());			
			IWord synset = getMostCommonSynset(token.lemma(), targetTokenMainTag);
			result.add(synset);
		}
		return result;
	}

}
