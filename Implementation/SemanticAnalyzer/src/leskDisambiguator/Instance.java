package leskDisambiguator;
import java.util.ArrayList;
import java.util.List;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;

public class Instance {

	List<Word> words;
	List<Combination> combinations;
	List<Pointer> relations;
	int windowSize;
	int way;
	
	public void set_instance (int size, int selectedway, List<Pointer> relations)
	{
		words = new ArrayList<>();
		combinations = new ArrayList<>();
		this.relations = new ArrayList<>();
		for (Pointer pointer : relations) {
			this.relations.add(pointer);
		}
		windowSize = size;
		way = selectedway;
	}
	
	public void createCombinations (IDictionary dict)
	{
		List<Combination> combs = new ArrayList<>();
		findAllCombinations(combs, words.get(0), dict);
		for (int i = 1 ; i< words.size(); i++)
		{
			findAllCombinations(combs, words.get(i), dict);
		}
		combinations.addAll(combs);
	}
	
	public void select_BestCombinations (IDictionary dict, List<String> comparedGlosses)
	{
		int maxScore = 0;
		int bestID = 0;
		for (int i = 0;i< combinations.size() ; i++)
		{
			combinations.get(i).calculate_score(dict, comparedGlosses, relations, way);
			if (combinations.get(i).score > maxScore)
			{
				maxScore = combinations.get(i).score;
				bestID = i;
			}
		}
		if (maxScore == 0)
		{
			//get common sense of words
		}
		else
		{
			for (int i = 0 ; i< words.size() ; i++)
			{
				words.get(i).bestSenceIDs.add(combinations.get(bestID).sences.get(i).getID());
			}
		}	
	}
	
	public void findAllCombinations (List<Combination>  combs , Word word, IDictionary dict)
	{
		
		List<IWord> combinationSet = new ArrayList<>();
		List<Combination> temp = new ArrayList<>();
		
		if (combs.isEmpty())
		{
			for (int i = 0; i< word.sences.size(); i++)
			{
				combinationSet = new ArrayList<>();
				combinationSet.add(word.sences.get(i));
				Combination com = new Combination();
				com.set_combination(combinationSet);
				combs.add(com);
			}
		}
		else
		{
			for (int i = 0; i< combs.size(); i++)
			{
				for (int j = 0; j< word.sences.size(); j++)
				{
					combinationSet = new ArrayList<>();
					combinationSet.addAll(combs.get(i).sences);
					combinationSet.add(word.sences.get(j));
					Combination com = new Combination();
					com.set_combination(combinationSet);
					temp.add(com);
				}				
			}
			combs.clear();
			combs.addAll(temp);
		}
		
	}
}