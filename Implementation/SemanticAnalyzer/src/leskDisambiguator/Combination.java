package leskDisambiguator;

import helpers.StanfordCoreNLPHelper;

import java.util.ArrayList;
import java.util.List;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Combination {

	 List<IWord> sences;
	 int score;
	 
	 
	 public void set_combination (List<IWord> words)
	 {
		 sences = new ArrayList<>();
		 score = 0;
		 sences.addAll(0, words);
	 }
	 
	 public void calculate_score (IDictionary dict, List<String> comparedGlosses, List<Pointer> relations , int way)
	 {
		List<String> gloss1 = new ArrayList<>();
		List<String> gloss2 = new ArrayList<>();
		List<String> glosses1 = new ArrayList<>();
		List<String> glosses2 = new ArrayList<>();
		int score1 = 0;
		for (int i = 0 ; i < sences.size()-1; i++)
		{
			for (int j = 0; j < sences.size(); j++)
			{
				if ((i != j) && (i < j))
				{
					glosses1 = new ArrayList<>();
					glosses2 = new ArrayList<>();
					glosses1.add(sences.get(i).getSynset().getGloss());
					glosses2.add(sences.get(j).getSynset().getGloss());
					if (way == 2)  //Homogeneous
					{
						gloss1 = process_gloss(glosses1.get(0));
						gloss2 = process_gloss(glosses2.get(0));
						if (gloss1.size() != 0 && gloss2.size() != 0)
							score += compair_pair(gloss1, gloss2);
					}
					
					for (Pointer rel : relations) {
						glosses1.add(get_RelatedSynsetsGlosses(dict, sences.get(i).getSynset() , rel));
						glosses2.add(get_RelatedSynsetsGlosses(dict, sences.get(j).getSynset() , rel));
						if (way == 2)  //Homogeneous
						{
							gloss1 = process_gloss(glosses1.get(glosses1.size()-1));
							gloss2 = process_gloss(glosses2.get(glosses2.size()-1));
							if (gloss1.size() != 0 && gloss2.size() != 0)
								score += compair_pair(gloss1, gloss2);
						}
						
					}

					if (way == 1)   //Heterogeneous
					{
						for (int k = 0; k < glosses1.size() ; k++)
						{
							for (int h = 0; h < glosses2.size() ; h++)
							{								
								if (!comparedGlosses.contains(glosses1.get(k) +"#"+ glosses2.get(h)))
								{
										gloss1 = process_gloss(glosses1.get(k));
										gloss2 = process_gloss(glosses2.get(h));
										if (gloss1.size() != 0 && gloss2.size() != 0)
										{
											score1 = compair_pair(gloss1, gloss2); 
											comparedGlosses.add(glosses1.get(k) +"#"+ glosses2.get(h));
											comparedGlosses.add(Integer.toString(score1));
										}
								}
								else
								{
									int index = comparedGlosses.indexOf(glosses1.get(k) +"#"+ glosses2.get(h)) + 1;
									score1 = Integer.valueOf(comparedGlosses.get(index));
								}
								score += score1;
							}
						}
					}
				}
			}
		}
	 }
	 	 
	 public int compair_pair (List<String> gloss1, List<String> gloss2)
	 {
		 int score = 0;
		 int numWords = 0;
		 int index = 0;
		 String s = "";
		 int i = 0;
		 while (i < gloss1.size())
		 {
			 if (numWords == 0)
			 {
				 if (gloss2.contains(gloss1.get(i)))
				 {
					 numWords++;
					 index = gloss2.indexOf(gloss1.get(i));
				 }
				 i++;
			 }
			 else
			 {
				if (index+1 < gloss2.size())
				{
					s = gloss2.get(++index);
					if (gloss1.get(i).equals(s))
					{
						numWords++;
						i++;
					}
					else
					{
						score += numWords * numWords;
						numWords = 0;
					}
				}
				else
				{
					score += numWords * numWords;
					numWords = 0;
					i++;
				}
			 }
		 }
		 return score;
	 }
	 
	  public String get_RelatedSynsetsGlosses (IDictionary dict, ISynset synset ,IPointer rel)
	    {
	    	String gloss = "";
			List<ISynsetID> res = synset.getRelatedSynsets(rel);
			for (ISynsetID synsetID : res)
			{
				ISynset s = dict.getSynset(synsetID);
				gloss += s.getGloss() + " ";
			}
			return gloss;
	    }
	  public List<String> process_gloss (String gloss)
	    {
	    	List<String> gloss_list = new ArrayList<>();;
	    	gloss = cleanString (gloss);
			Annotation annotation =  StanfordCoreNLPHelper.getAnnotation(gloss);
			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			for(CoreMap sentence: sentences)
		    {
		      // traversing the words in the current sentence
		      // a CoreLabel is a CoreMap with additional token-specific methods
		      for (CoreLabel token: sentence.get(TokensAnnotation.class))
		      {	  
			      // this is the lemma of the token
			      String lemma = token.get(LemmaAnnotation.class);
			      gloss_list.add(lemma);
		      }
		    }
			return gloss_list;
	    }
	  
	  public String cleanString(String s) 
		{
		    String result = s.replaceAll("[^\\w , ' .]", "");
		    return result;
		}
}
