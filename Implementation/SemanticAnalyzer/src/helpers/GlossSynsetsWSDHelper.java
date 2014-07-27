package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.mit.jwi.item.IWord;
import edu.stanford.nlp.ling.CoreLabel;
public class GlossSynsetsWSDHelper 
{
	private static String glossSynsetsFileName;
	private static Boolean initialized = false;
    private static Boolean memoryMode;
    private static Hashtable<String, List<String>> glossSynsetsHT;
	public static void initialize(String glossSynsetsFileName, Boolean memoryMode) throws IOException
	{
		GlossSynsetsWSDHelper.glossSynsetsFileName = glossSynsetsFileName;
		GlossSynsetsWSDHelper.memoryMode = memoryMode;
		if (!initialized)
		{
			if (memoryMode)
			{
				loadToMemory(GlossSynsetsWSDHelper.glossSynsetsFileName);
			}
		}
	}
	private static void loadToMemory(String glossSynsetsFileName) throws IOException
	{
		glossSynsetsHT = new Hashtable<String, List<String>>();
		BufferedReader br = new BufferedReader(new FileReader(glossSynsetsFileName));
		String line;
		while ((line = br.readLine()) != null) 
    	{
            String[] synsetsInLine = line.split(";");
            String synset = synsetsInLine[0];
            List<String> glossSynsetsInLine = new ArrayList<String>();
            for (int i = 1; i < synsetsInLine.length; i++)
            {
            	glossSynsetsInLine.add(synsetsInLine[i]);
            }
            glossSynsetsHT.put(synset, glossSynsetsInLine);    
    	}
	}
	public static List<String> getGlossSynsets(String synsetID)
	{
		List<String> result = null;
		if (memoryMode)
		{
			result  = getGlossSynsetsMemoryModeEnabled(synsetID);
		}
		else
		{
			result  = getGlossSynsetsMemoryModeDisabled(synsetID);
		}
		return result;
	}
	private static List<String> getGlossSynsetsMemoryModeEnabled(String synsetID)
	{
		List<String> result = null;
		try
		{
			result = glossSynsetsHT.get(synsetID.toLowerCase());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	private static List<String> getGlossSynsetsMemoryModeDisabled(String synsetID)
	{
		List<String> result = null;
		try
		{
			FileReader fr = new FileReader(glossSynsetsFileName);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) 
	    	{
				int indexOfSeparator = line.indexOf(";");
				String synset;
				if (indexOfSeparator != -1)
				{
					synset = line.substring(0, indexOfSeparator);
				}
				else
				{
					synset = line;
				}
				if (synset.equalsIgnoreCase(synsetID))
				{
					String[] synsetsInLine = line.split(";");
		            List<String> glossSynsetsInLine = new ArrayList<String>();
		            for (int i = 1; i < synsetsInLine.length; i++)
		            {
		            	glossSynsetsInLine.add(synsetsInLine[i]);
		            }
		            result = glossSynsetsInLine;
		            break;
				}	              
	    	}
			fr.close();
			br.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	public static IWord disambiguate(String targetTokenLemma, String targetTokenMainTag, List<String> contextTokensLemmas)
	{
		IWord result = null;
		
		// get all possible synsets for the target token
		List<IWord> targetTokenSynsets = JWIHelper.getSynsets(targetTokenLemma, targetTokenMainTag);
		
		// if there is only one possible synset or there is nothing at all don't continue
		if (targetTokenSynsets == null)
		{
			return null;
		}
		else if (targetTokenSynsets.size() == 1)
		{
			return targetTokenSynsets.get(0);
		}
		
		// set the scores of all target token synsets to 0
		int[] scores = new int[targetTokenSynsets.size()];
		for (int i = 0; i < targetTokenSynsets.size(); i++)
		{
			scores[i] = 0;
		}
				
		// get a list that contains all sysnsets for all context tokens
		List<String> contextTokensSynsetsIDs = new ArrayList<String>();
		for (String t : contextTokensLemmas)
		{
			List<IWord> synsets = JWIHelper.getSynsetsOfAnyTag(t);
			if (synsets != null)
			{
				for (IWord s: synsets)
				{
					String sID = s.getSynset().getID().toString().substring(4).toLowerCase();
					contextTokensSynsetsIDs.add(sID);					
				}
			}
		}
			
		// calculate the score of each target synset
		for (int s = 0; s < targetTokenSynsets.size(); s++)
		{
			String sID = targetTokenSynsets.get(s).getSynset().getID().toString().substring(4);
			List<String> glossSynsets =  getGlossSynsets(sID);
			if (glossSynsets != null)
			{
				for (int i = 0; i < glossSynsets.size(); i++)
				{
					if (contextTokensSynsetsIDs.contains(glossSynsets.get(i)))
					{
						scores[s]++;
					}
				}
			}			
		}
			
		// take the synset that has the highest score
		int maxScore = 0;
		result =  targetTokenSynsets.get(0);
		for (int i = 0; i < targetTokenSynsets.size(); i++)
		{
			if (scores[i] > maxScore)
			{
				maxScore = scores[i];
				result = targetTokenSynsets.get(i);
			}
		}
	
		return result;
	}
	
	public static List<IWord> disambiguate2(List<CoreLabel> sentence)
	{
		List<IWord> result = new ArrayList<>();
		for (CoreLabel token : sentence)
		{
			List<String> contextTokensLemmas = new ArrayList<>();
			for (CoreLabel contextToken : sentence)
			{
				if (token != contextToken) // do not add target token
				{
					contextTokensLemmas.add(contextToken.lemma());
				}
			}
			String targetTokenMainTag = StanfordCoreNLPHelper.getMainTagFromTag(token.tag());			
			IWord synset = disambiguate(token.lemma(), targetTokenMainTag, contextTokensLemmas);
			result.add(synset);
		}
		return result;
	}
	public static List<IWord> disambiguate(List<CoreLabel> sentence)
	{
		List<IWord> result = new ArrayList<>();
	
		// get synsets ids for each token
		List<List<String>> tokensSynsetsIDs = new ArrayList<>();
		for (CoreLabel token : sentence)
		{
			List<IWord> tokenSynsets = JWIHelper.getSynsetsOfAnyTag(token.lemma());
			List<String> tokenSynsetsIDs = new ArrayList<>();
			if (tokenSynsets != null)
			{
				for (IWord s : tokenSynsets)
				{
					String sID = s.getSynset().getID().toString().substring(4).toLowerCase();
					tokenSynsetsIDs.add(sID);
				}
			}
			tokensSynsetsIDs.add(tokenSynsetsIDs);
		}
		
		// loop through all tokens to disambiguate it
		for (int tt = 0; tt < sentence.size(); tt++)
		{
			// get all possible synsets for the target token
			String targetTokenMainTag = StanfordCoreNLPHelper.getMainTagFromTag(sentence.get(tt).tag());	
			List<IWord> targetTokenSynsets = JWIHelper.getSynsets(sentence.get(tt).lemma(), targetTokenMainTag);
			
			// if there is only one possible synset or there is nothing at all
			if (targetTokenSynsets == null)
			{
				result.add(null);
				continue;
			}
			else if (targetTokenSynsets.size() == 1)
			{
				result.add(targetTokenSynsets.get(0));
				continue;
			}
			
			// set the scores of all target token synsets to 0
			int[] scores = new int[targetTokenSynsets.size()];
			for (int sc = 0; sc < scores.length; sc++)
			{
				scores[sc] = 0;
			}
			
			// get context tokens synsets ids
			List<String> contextTokensSynsetsIDs = new ArrayList<>();
			for (int t = 0; t < sentence.size(); t++)
			{
				if (tt != t) // do not add target token
				{
					contextTokensSynsetsIDs.addAll(tokensSynsetsIDs.get(t));
				}
			}
			
			// calculate the score of each target token synset
			for (int s = 0; s < targetTokenSynsets.size(); s++)
			{
				String sID = targetTokenSynsets.get(s).getSynset().getID().toString().substring(4);
				List<String> glossSynsets =  getGlossSynsets(sID);
				if (glossSynsets != null)
				{
					for (int gs = 0; gs < glossSynsets.size(); gs++)
					{
						if (contextTokensSynsetsIDs.contains(glossSynsets.get(gs)))
						{
							scores[s]++;
						}
					}
				}			
			}
				
			// take the synset that has the highest score
			int maxScore = 0;
			IWord synset =  targetTokenSynsets.get(0);
			for (int s = 0; s < targetTokenSynsets.size(); s++)
			{				
				if (scores[s] > maxScore)
				{
					maxScore = scores[s];
					synset = targetTokenSynsets.get(s);
				}
			}
			result.add(synset);
		}
		return result;
	}
}
