package helpers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.IntPair;
public class StanfordCoreNLPHelper 
{
	private static StanfordCoreNLP pipeline;
	private static Boolean initialized = false;
	private static Boolean coreferenceEnabled;
	public static void initialize(Boolean coreferenceEnabled)
	{
		StanfordCoreNLPHelper.coreferenceEnabled = coreferenceEnabled;
		if (!initialized)
		{
		    Properties props = new Properties();
		    if (coreferenceEnabled)
		    {
		    	props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		    }
		    else
		    {
		    	props.put("annotators", "tokenize, ssplit, pos, lemma");
		    }
		    props.put("tokenize.options","ptb3Escaping=false"); // disable traditional PTB3 token transforms (like parentheses becoming -LRB-, -RRB-)
		    pipeline = new StanfordCoreNLP(props);
		    initialized = true;
		}
	}
	public static Annotation getAnnotation(String text)
	{
	    Annotation annotation = new Annotation(text);
	    pipeline.annotate(annotation); 
	    return annotation;
	}	
	public static CoreLabel getSourceToken(Annotation annotation, IntPair tokenKey)
	{
		CoreLabel sourceToken = null;
		
		// if this method invoked by and the coreference models are not loaded
		if (!coreferenceEnabled)
		{
			return null;
		}
		
		// get the source token only for PRP & PRP$ 
		int tokenSentenceIndex = tokenKey.getSource() - 1;
		int tokenIndex =  tokenKey.getTarget() - 1;
		CoreLabel token = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(tokenSentenceIndex).get(TokensAnnotation.class).get(tokenIndex);
		String tokenPOS = token.get(PartOfSpeechAnnotation.class);		
		if (!tokenPOS.equals("PRP$") & !tokenPOS.equals("PRP"))
		{
			return null;
		}
		// end
		
		Map<Integer, CorefChain> graph =  annotation.get(CorefChainAnnotation.class);
		IntPair sourceTokenKey = null;
		for (CorefChain c : graph.values())
	    {
	    	Map<IntPair, Set<CorefMention>> chain = c.getMentionMap();
	    	if (chain.size() > 1)
	    	{ 	
	    		List<IntPair> chainOfKeys = new ArrayList<IntPair>(chain.keySet());
	    		Collections.sort(chainOfKeys);
	    		sourceTokenKey = chainOfKeys.get(0);
	    		for (IntPair p : chainOfKeys)
	    		{			
	    			if (p.equals(tokenKey))
	    			{	 
	    				int s = sourceTokenKey.getSource() - 1;
    					int t = sourceTokenKey.getTarget() - 1;
    					sourceToken = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(s).get(TokensAnnotation.class).get(t);
	    				break;
	    			}	
	    		}	    		
	    	}
	    }	
		
		
		if (sourceToken != null)
		{
			//if the source token is preposition also don't return it
			String sourceTokenPOS = sourceToken.get(PartOfSpeechAnnotation.class);		
			if (sourceTokenPOS.equals("PRP$") || sourceTokenPOS.equals("PRP"))
			{
				return null;
			}
			// end
		}		
		
		return sourceToken;
	}	
	public static Boolean tokenTagIsAccepted(CoreLabel token, List<String> acceptedTags)
	{
		Boolean result = false;
		String mainTag = getMainTagFromTag(token.tag());
		if (acceptedTags.contains(mainTag))
		{
			result = true;
		}
		return result;
	}
	public static String getMainTagFromTag(String tag)
	{
		String mainTag;
		if (tag.length() >= 3)
		{
			mainTag = tag.substring(0, 2);
		}
		else
		{
			mainTag = tag;
		}
		return mainTag;
	}
}