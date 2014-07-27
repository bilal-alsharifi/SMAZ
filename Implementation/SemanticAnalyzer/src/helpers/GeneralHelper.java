package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.IntPair;

public class GeneralHelper 
{
	public static String splitCompundWords(String text) 
	{
	    String result = text.replaceAll("([a-z])([A-Z])([a-z]+)", "$1 $2$3")
	    					.replaceAll("([a-z])([_\\-])([a-z]+)", "$1 $3");
	    return result;	    
	}
	public static String removeStrangeCharacters(String text) 
	{
		// \\xA0 is none breaking space &nbsp;
		String result = text.replaceAll("[^\\w \\xA0\\t\\n\\r!\"',\\.:?&@]", "");
	    return result;
	}
	public static List<String> loadStopWords(String stopWordsFileName) throws IOException
	{
		List<String> stopWords = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(stopWordsFileName));
		while (in.ready()) 
		{
		  String line = in.readLine();
		  stopWords.add(line);
		}
		in.close();
		return stopWords;
	}
	public static List<List<CoreLabel>> filterAndTokenize(String text, List<String> acceptedTags, List<String> stopWords, int shortWordsLimit, Boolean withCoreference)
	{
		// just to make sure that all tags are in upper case
		for (int i = 0; i < acceptedTags.size(); i++)
		{
			String tag = acceptedTags.get(i).toUpperCase();
			acceptedTags.set(i, tag);
		}
		
		Annotation annotation = StanfordCoreNLPHelper.getAnnotation(text);
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    List<List<CoreLabel>> processedSentences = new ArrayList<>();
	    for(int s = 0; s < sentences.size(); s++) 
	    {
	    	List<CoreLabel> sentence = sentences.get(s).get(TokensAnnotation.class);
	    	List<CoreLabel> processedSentence = new ArrayList<>();
	    	for (int t = 0; t < sentence.size(); t++) 
	    	{
	    		IntPair tokenKey = new IntPair(s + 1, t + 1);
	    		CoreLabel token = sentence.get(t);
	    		
	    		// find source token by coreference
	    		if (withCoreference)
	    		{
		    		CoreLabel sourceToken =  StanfordCoreNLPHelper.getSourceToken(annotation, tokenKey);
		    		if (sourceToken != null)
		    		{
		    			token = sourceToken;
		    		}
	    		}
	    		
	    		// check token tag
	    		if (!StanfordCoreNLPHelper.tokenTagIsAccepted(token, acceptedTags))
	    		{
	    			continue;
	    		}
	    		
	    		// do not process very short words
	    		if (token.lemma().length() < shortWordsLimit)
	    		{
	    			continue;
	    		}
	    		
	    		
	    		// do not process very short words
	    		if (stopWords.contains(token.lemma().toLowerCase()))
	    		{
	    			continue;
	    		}
	    		
	    		
	    		processedSentence.add(token);	    		
	    	}
	    	processedSentences.add(processedSentence);
	    }
	    return processedSentences;
	}
}
