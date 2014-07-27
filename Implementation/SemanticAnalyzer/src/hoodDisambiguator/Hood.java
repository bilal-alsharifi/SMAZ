package hoodDisambiguator;
import java.util.Enumeration;
import java.util.List;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Lama
 */
public class Hood
{
    public List<String> words;
    public List<ISynset> synsets;
    public List<IWord> rightWords;

    public Hood ()
    {
    }


    public List<Term> calcualteTerms (List<CoreLabel> tokens, IDictionary dict)
    {
        int index = 0;
        List<Term> terms = new ArrayList<Term>();
        
        for (CoreLabel token : tokens)
        {
            // this is the text of the token
            String tok = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the lemma of the token
            String lemma = token.get(LemmaAnnotation.class);
            Term term = new Term();
            term.set_word(dict, tok, pos, lemma, index);
            try
            {
                if (!dict.getIndexWord(term.wordLemma, term.wordPos).equals(null))
                {
                    if ((term.wordPos == POS.NOUN || term.wordPos == POS.VERB || term.wordPos == POS.ADJECTIVE || term.wordPos == POS.ADVERB ))
                    {
                        terms.add(term);
                        index++;
                    }
                }
            }
            catch (Exception e)
            {
                //rightWords.add(null);
            }
        }
        return terms;
    }

    public List<IWord> disambiguate (List<CoreLabel> tokens, IDictionary dict)
    {
        List<Term> ambiguousWords = calcualteTerms(tokens, dict);
        this.words = new ArrayList<String>();
        this.synsets = new ArrayList<ISynset>();
        this.rightWords = new ArrayList<IWord>();

        List<Hashtable<ISynset, ISynset>> wordsHoods = new ArrayList<Hashtable<ISynset, ISynset>>();
        List<List<Integer>> rootsVisits = new ArrayList<List<Integer>>();

        this.Initiliaze(ambiguousWords, wordsHoods, rootsVisits, dict);

        // visit the roots of the synsets

        for (int i = 0; i < wordsHoods.size(); i++ )
        {
            Hashtable<ISynset, ISynset> synSetsRoots = wordsHoods.get(i);

            for (int j = 0; j < synSetsRoots.size(); j++)
            {
                Enumeration<ISynset> keys = synSetsRoots.keys();
                int counter = 0;
                ISynset key = null;
                while (keys.hasMoreElements() && counter <= j)
                {
                    key = (ISynset) keys.nextElement();
                    counter++;
                }
                //ISynset syn = synSetsRoots.keys().nextElement();
                ISynset root = synSetsRoots.get(key);

                for (int k = 0; k < wordsHoods.size(); k++)
                {
                    if (k == i)
                        continue;
                    Hashtable<ISynset, ISynset> wordCRoots = wordsHoods.get(k);
                    Enumeration<ISynset> keys1 = wordCRoots.keys();
                    int counter1 = 0;
                    ISynset key1 = null;
                    for (int n = 0; n < wordCRoots.size(); n++)
                    {
                        while (keys1.hasMoreElements() && counter1 <= n)
                        {
                            key1 = (ISynset) keys1.nextElement();
                            counter1++;
                        }
                        ISynset synWC = key1;

                        List<ISynsetID> hypernymsID = getAllHypernyms(synWC, dict);
                        if (IsContaine(hypernymsID, root.getID()))
                        {
                            List<Integer> temp = rootsVisits.get(i);
                            temp.set(j, temp.get(j)+1);
                            rootsVisits.set(i, temp);
                        }
                    }
                }
            }
        }

        // assign each word to the synset which it's root has the max visits times

        for (int m = 0; m < rootsVisits.size(); m++)
        {
            Term w = ambiguousWords.get(m);
            List<Integer> visits = rootsVisits.get(m);
            List<Integer> indexes = getIndexOfMax(visits);
            Hashtable<ISynset, ISynset> sr = wordsHoods.get(m);
            if (indexes.size() == 1 )
            {
                int counter = 0;
                Enumeration<ISynset> keys = sr.keys();
                ISynset key = null;
                while (keys.hasMoreElements() && counter <= indexes.get(0))
                {
                    key = (ISynset) keys.nextElement();
                    counter++;
                }
                ISynset rightkey = key;
                List<IWord> words = new ArrayList<IWord>();
                words = rightkey.getWords();
                for (IWord word : words)
                {
                    if (word.getLemma().equals(ambiguousWords.get(m).wordLemma))
                    {
                        rightWords.add(m,word);
                        break;
                    }
                }
                this.synsets.add(m,rightkey);
            }
            else
            {
                List<ISynset> possibleSyns = new ArrayList<ISynset>();
                for (int i=0; i<indexes.size(); i++)
                {
                    int counter = 0;
                    Enumeration<ISynset> keys = sr.keys();
                    ISynset key = null;
                    while (keys.hasMoreElements() && counter <= indexes.get(i))
                    {
                        key = (ISynset) keys.nextElement();
                        counter++;
                    }
                    ISynset possibleKey = key;
                    possibleSyns.add(possibleKey);
                }

                ISynset rightSyn = getMostCommonSynsetBetweenGroup(w , possibleSyns, dict);
                List<IWord> words = new ArrayList<IWord>();
                words = rightSyn.getWords();
                for (IWord word : words)
                {
                    if (word.getLemma().equals(ambiguousWords.get(m).wordLemma))
                    {
                        rightWords.add(m,word);
                        break;
                    }
                }
                this.synsets.add(m,rightSyn);
            }
        }

        return this.rightWords;
    }

    private void Initiliaze(List<Term> ambiguousWords,List<Hashtable<ISynset, ISynset>> wordsHoods, List<List<Integer>> rootsVisits, IDictionary dict)
    {
        for (Term word : ambiguousWords)
        {
            List<ISynset> _wordSynsets = new ArrayList<ISynset>();
            this.words.add(word.wordLemma);
            IIndexWord idxWord = dict.getIndexWord (word.wordLemma, word.wordPos);
	    List<IWordID> wordIDs = idxWord.getWordIDs();
            for (IWordID id : wordIDs)
            {
                _wordSynsets.add(dict.getWord(id).getSynset());
            }

            List<ISynset> wordSynsets = new ArrayList<ISynset>();
            List<Integer> visits = new ArrayList<Integer>();

            for (ISynset s : _wordSynsets)
            {
                wordSynsets.add(s);
            }

            Hashtable<ISynset, ISynset> synsRoots = new Hashtable<ISynset, ISynset>();
            for (ISynset syn : wordSynsets)
            {
                List<ISynset> others = new ArrayList<ISynset>();
                for (ISynset s : wordSynsets)
                {
                  others.add(s);
                }
                others.remove(syn);
                ISynset root = this.FindHoodRoot(syn, others, dict);
                synsRoots.put(syn, root);
                visits.add(0);
            }
            wordsHoods.add(synsRoots);
            rootsVisits.add(visits);
        }
    }

    public ISynset FindHoodRoot(ISynset synSet , List<ISynset> others, IDictionary dict)
    {
        ISynset root = null;

        Boolean found = false;

        List<ISynsetID> hypernyms = getAllHypernyms(synSet, dict);

        // case -1
        // the synset it self the root of the hood because the direct parent (frist one) has a descendent contain a word of the synset
        if (hypernyms.size()>1)
        {
            for (ISynset other : others)
            {
                if (IsContaine(getAllHypernyms(other, dict), hypernyms.get(0)))
                {
                    root = synSet;
                    found = true;
                    break;
                }
            }
        }

        // case -2
        // the synset has a common ancestor with other synset contains a word from it so the root it is the
        // previous parent from that ancestor

        if (!found && hypernyms.size() > 6)
        {
            for (int i =0; i < hypernyms.size() - 3; i++ )
            {
                ISynsetID previousParent = hypernyms.get(i);

                for (ISynset other : others)
                {
                    if (IsContaine(getAllHypernyms(other, dict) , hypernyms.get(i+1)))
                    {
                        if (root == null)
                        {
                            root = dict.getSynset(previousParent);
                            found = true;
                        }
                        else
                        {
                            if (previousParent.getOffset() > root.getID().getOffset())
                            {
                                root = dict.getSynset(previousParent);
                            }
                        }
                    }

                }
            }

        }

        // handling special words that exist in the top of the hypernym hierarchy
        // because we can't give them the same root always (e.g. abstract entity - physical entity)
        if (root == null)
        {
            if (hypernyms.size() == 3)
                root = dict.getSynset(hypernyms.get(hypernyms.size() - 2));
            else if (hypernyms.size() == 2)
                root = dict.getSynset(hypernyms.get(hypernyms.size() - 2));
            else if (hypernyms.size() == 1)
                root = dict.getSynset(hypernyms.get(hypernyms.size() - 1));
            else if (hypernyms.isEmpty())
                root = synSet;
            else
                root = dict.getSynset(hypernyms.get(hypernyms.size() - 4));
        }

        return root;
    }

     // test if synset contained in list of synset depending the hereiam
    public boolean  IsContaine(List<ISynsetID> list, ISynsetID syn)
    {
        for (int i=0; i < list.size(); i++)
        {
            if (list.get(i).equals(syn))
                return true;
        }
        return false;
    }

    public List<Integer> getIndexOfMax (List<Integer> visits)
    {
        int max  = 0;
        int index = 0;
        List<Integer> listOfIndex = new ArrayList<Integer>();
        for (int i=0; i<visits.size(); i++)
        {
            if (visits.get(i) >= max)
            {
                max = visits.get(i);
                index = i;
                listOfIndex.add(index);
            }
        }
        return listOfIndex;
    }

    public List<ISynsetID> getAllHypernyms (ISynset syn, IDictionary dict)
    {
        List<ISynsetID> allSyns = new ArrayList<ISynsetID>();
        List<ISynsetID> syns = new ArrayList<ISynsetID>();
        while (!(syn.getRelatedSynsets(Pointer.HYPERNYM).isEmpty()))
        {
            syns = syn.getRelatedSynsets(Pointer.HYPERNYM);
            for (ISynsetID id : syns)
            {
                allSyns.add(id);
            }
            if (allSyns.contains(syns.get(0)))
            {
                break;
            }
            syn = dict.getSynset(syns.get(0));
        }
        return allSyns;
    }
    
    public List<IWord> getSynsets(Term word, IDictionary dict)
    {
        List<IWord> result = null;
        IIndexWord idxWord = dict.getIndexWord(word.wordLemma, word.wordPos);
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

    public ISynset getMostCommonSynsets(Term word, IDictionary dict)
    {
        IWord result = null;
        List<IWord> synsets = getSynsets(word, dict);
        if (synsets != null)
        {
                result = synsets.get(0);
        }
        ISynset commonSynset = result.getSynset();
        return commonSynset;
    }

    public ISynset getMostCommonSynsetBetweenGroup (Term word,List<ISynset> group, IDictionary dict)
    {
        List<IWord> words = getSynsets(word, dict);
        ISynset commonSyn = null;
        ISynset synTmp = null;
        int minIndex = 30;
        List<ISynset> syns = new ArrayList<ISynset>();
        for (IWord wordTmp : words)
        {
            synTmp = wordTmp.getSynset();
            syns.add(synTmp);
        }

        for (ISynset syn : group)
        {
            if (syns.indexOf(syn) < minIndex)
            {
                commonSyn = syn;
                minIndex = syns.indexOf(syn);
            }
        }
        return commonSyn;
    }



}
