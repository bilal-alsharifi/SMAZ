package entities;

import helpers.ExDomainsHelper;
import helpers.FileReaderHelper;
import helpers.GeneralHelper;
import helpers.GlossSynsetsWSDHelper;
import helpers.JWIHelper;
import helpers.DbPediaHelper;
import helpers.StanfordCoreNLPHelper;
import hoodDisambiguator.Hood;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.Pointer;
import edu.stanford.nlp.ling.CoreLabel;

public class SemanticAnalyzerImpl extends UnicastRemoteObject implements SemanticAnalyzer
{
	private static final long serialVersionUID = 1L;
	private String wordNetDictFolder;
	private String eXDomainsFolder;
	private String eXWDHFileName;
	private String stopWordsFileName;
	private String genaLog4jProprtiesFileName;
	private int jWIMemoryMode;
	private Boolean ExDomainsMemoryModeEnabled;
	private List<String> stopWords;
	private String glosTagsFileName;
	private Boolean GlossSynsetsWSDMemoryModeEnabled;
	private List<Pointer> leskRelations;
	
	public SemanticAnalyzerImpl() throws RemoteException
	{				
		// editable variables
		String filesFolder = "files//";
		this.wordNetDictFolder = filesFolder + "wordnet//dict30";
		this.eXDomainsFolder = filesFolder + "wordnet//exDomains";
		this.eXWDHFileName = filesFolder + "wordnet//eXWDH";
		this.stopWordsFileName = filesFolder + "StopWords.txt";
		this.genaLog4jProprtiesFileName = filesFolder + "lib//Apache Jena//jena-log4j.properties";
		this.jWIMemoryMode = ILoadPolicy.BACKGROUND_LOAD;
		this.ExDomainsMemoryModeEnabled = true;
		
		
		// disambiguation variables
		// method 2
		this.glosTagsFileName = filesFolder + "wordnet//glossSynsets.txt";
		this.GlossSynsetsWSDMemoryModeEnabled = true;
		// method 4
		this.leskRelations =  new ArrayList<>();
		this.leskRelations.add(Pointer.HYPERNYM);
		this.leskRelations.add(Pointer.HYPONYM);
	}
	
	public Boolean load(Boolean showErrors, Boolean coreferenceEnabled) throws RemoteException
	{
		// hide errors & loading messages
		if (!showErrors)
		{
			System.err.close(); 
		}
		
		// load libraries
		System.err.println("-------------------------------------------");
		System.err.println("loading stop words...");
		stopWords = new ArrayList<String>();
		try 
		{
			stopWords = GeneralHelper.loadStopWords(stopWordsFileName);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		System.err.println("-------------------------------------------");
		System.err.println("loading Stanford CoreNLP...");
		StanfordCoreNLPHelper.initialize(coreferenceEnabled);
		
		System.err.println("-------------------------------------------");
		System.err.println("loading JWI...");
		try 
		{
			JWIHelper.initialize(wordNetDictFolder, jWIMemoryMode);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		System.err.println("-------------------------------------------");
		System.err.println("loading ExDomains...");
		try 
		{
			ExDomainsHelper.initialize(eXDomainsFolder, eXWDHFileName, ExDomainsMemoryModeEnabled);
		} 
		catch (NumberFormatException | IOException e) 
		{
			e.printStackTrace();
			return false;
		} 
		
		System.err.println("-------------------------------------------");
		System.err.println("loading Gloss Sysnsets WSD...");
		try 
		{
			GlossSynsetsWSDHelper.initialize(glosTagsFileName, GlossSynsetsWSDMemoryModeEnabled);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		System.err.println("-------------------------------------------");
		System.err.println("loading Jena...");
		DbPediaHelper.initializeJena(genaLog4jProprtiesFileName);	
		
		System.err.println("---------------------------------------------------------------------------------");
		
		return true;		
	}
	
	public String processRequest(String input, Boolean inputIsText, Boolean sortDocDomainsByWeight, Boolean addWeightsToAncestors, int shortWordsLimit, List<String> acceptedTags, Boolean searchInDbPedia, List<String> dbPediaPredicates, int dbPediaResultsLimit, int disambiguationMethod, int leskWindowSize, int leskComparisonWay, Boolean withCoreference) throws RemoteException
	{
		// read the file or text
	    String result = "";
		System.err.println("reading file or text...");
		String text = null;
		if (inputIsText)
		{
			try 
			{
				text = FileReaderHelper.getTextFromHTMLText(input);
			} 
			catch (FileNotFoundException | BoilerpipeProcessingException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			try 
			{
				text = FileReaderHelper.read(input);
			} 
			catch (BoilerpipeProcessingException | IOException e)
			{
				e.printStackTrace();
			}			
		}
		if (text == null)
		{
			text = "";
		}
		System.err.println("---------------------------------------------------------------------------------");
		
		
		// process the document
		System.err.println("processing the document...");
		text = GeneralHelper.splitCompundWords(text);
		text = GeneralHelper.removeStrangeCharacters(text);		
		List<List<CoreLabel>> processedSentences = GeneralHelper.filterAndTokenize(text, acceptedTags, stopWords, shortWordsLimit, withCoreference);
	    System.err.println("---------------------------------------------------------------------------------");
	    
		// disambiguate the document
		System.err.println("disambiguate the document");
		List<IWord> disambiguatedSynsets = new ArrayList<>();
	    List<CoreLabel> tokensNotFoundInWordnet = new ArrayList<>();
	    for(int s = 0; s < processedSentences.size(); s++) 
	    {
	    	System.err.println("disambiguating sentence: " + (int)(s + 1) + " of " + processedSentences.size() + " sentences.");
	    	List<CoreLabel> sentence = processedSentences.get(s);
	    	List<IWord> senteceDisambiguatedSynsets = null;
	    	switch (disambiguationMethod) 
	    	{
				case 1:
				{
					senteceDisambiguatedSynsets = JWIHelper.disambiguate(sentence);
					break;
				}
				case 2:
				{
					senteceDisambiguatedSynsets = GlossSynsetsWSDHelper.disambiguate(sentence);
					break;
				}
				case 3:
				{
					Hood hood = new Hood();
					senteceDisambiguatedSynsets = hood.disambiguate(sentence, JWIHelper.getDict());					
					break;
				}
				case 4:
				{
					senteceDisambiguatedSynsets = leskDisambiguator.Lesk.disambiguate(sentence, JWIHelper.getDict(), leskWindowSize, leskComparisonWay, leskRelations);
					break;
				}
				default:
				{
					senteceDisambiguatedSynsets = JWIHelper.disambiguate(sentence);
					break;
				}
			}
	    	
	    	disambiguatedSynsets.addAll(senteceDisambiguatedSynsets);
	    	
	    	// get the tokens that are not found in wordnet
			for(int i = 0; i < senteceDisambiguatedSynsets.size(); i++)
			{
				IWord synset = senteceDisambiguatedSynsets.get(i);
				CoreLabel token = sentence.get(i);
				if (synset == null)
				{
					String mainTag = StanfordCoreNLPHelper.getMainTagFromTag(token.tag());
					if (mainTag.equalsIgnoreCase("NN"))
					{
						tokensNotFoundInWordnet.add(token);
					}
				}
				else
				{
					//System.out.println(synset.getLemma() + "(" + synset.getPOS() + "): " + synset.getSynset().getGloss());
				}
			}
	    }
		System.err.println("---------------------------------------------------------------------------------");
		
		
		// analyse tokens not found in wordnet
		if (searchInDbPedia)
		{
			System.err.println("analysing tokens not found in wordnet");
			for(int t = 0; t < tokensNotFoundInWordnet.size(); t++)
			{
				CoreLabel token = tokensNotFoundInWordnet.get(t);
				System.err.println("analysing token (" + token.word() + "): "+ (int)(t + 1) + " of " + tokensNotFoundInWordnet.size() + " tokens.");
				try 
				{
					String definitionText = DbPediaHelper.getDefinitionTextFromDbPedia(token.word(), dbPediaPredicates, dbPediaResultsLimit);
					if (definitionText == null)
					{
						continue;
					}
					definitionText = GeneralHelper.splitCompundWords(definitionText);
					definitionText = GeneralHelper.removeStrangeCharacters(definitionText);	
					List<List<CoreLabel>> processedSentencesForDefinitionText = GeneralHelper.filterAndTokenize(definitionText, acceptedTags, stopWords, shortWordsLimit, withCoreference);				
					for(int s = 0; s < processedSentencesForDefinitionText.size(); s++) 
				    {
				    	List<CoreLabel> sentence = processedSentencesForDefinitionText.get(s);
				    	List<IWord> senteceDisambiguatedSynsets = null;
				    	switch (disambiguationMethod) 
				    	{
							case 1:
							{
								senteceDisambiguatedSynsets = JWIHelper.disambiguate(sentence);
								break;
							}
							case 2:
							{
								senteceDisambiguatedSynsets = GlossSynsetsWSDHelper.disambiguate(sentence);
								break;
							}
							case 3:
							{
								Hood hood = new Hood();
								senteceDisambiguatedSynsets = hood.disambiguate(sentence, JWIHelper.getDict());					
								break;
							}
							case 4:
							{
								senteceDisambiguatedSynsets = leskDisambiguator.Lesk.disambiguate(sentence, JWIHelper.getDict(), leskWindowSize, leskComparisonWay, leskRelations);
								break;
							}
							default:
							{
								senteceDisambiguatedSynsets = JWIHelper.disambiguate(sentence);
								break;
							}
						}					    	
				    	disambiguatedSynsets.addAll(senteceDisambiguatedSynsets);
				    }		
				} 
				catch (ParserConfigurationException | SAXException | IOException e) 
				{
					e.printStackTrace();
				}				
			}
			System.err.println("---------------------------------------------------------------------------------");
		}
		
		// find the domains of the document
		System.err.println("finding the domains");
		DocDomainsFinder docDomainsFinder = new DocDomainsFinder(ExDomainsHelper.getDomainFilesNames()); //start a new clean docDomains	
		for (IWord synset : disambiguatedSynsets)
		{
			if (synset == null)
			{
				continue;
			}
			
    		// get domains of synset
    		String synsetID = synset.getSynset().getID().toString().substring(4);
    		List<SynsetDomain> domains = ExDomainsHelper.getDomains(synsetID);
    		   		
    		if (domains == null) // make sure that the token is listed in WordNet Domains
    		{
    			continue;
    		}
    		
    		// add synset domain weights to the doc domain weights 
    		docDomainsFinder.addDomainsForSynset(domains);
		}
	    
	    // print doc domains
	    List<SynsetDomain> docDomians =  docDomainsFinder.getDocDomains(sortDocDomainsByWeight, addWeightsToAncestors);
	    String separator = System.getProperty("line.separator");
	    for (int i = 0; i < docDomians.size(); i++)
		{
			result += docDomians.get(i).toString() + separator;
		}
	    System.err.println("---------------------------------------------------------------------------------");
	       	
	    return result;
	}
}
