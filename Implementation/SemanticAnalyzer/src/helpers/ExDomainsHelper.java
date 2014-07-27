package helpers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import entities.SynsetDomain;

public class ExDomainsHelper 
{
	private static String eXDomainsFolder;
	private static String eXWDHFileName;
    private static Boolean memoryMode;
	private static Boolean initialized = false;
	private static List<String> domainFilesNames = null;
	private static Hashtable<String, Integer> synsytsIDs = null;
	private static float [][] weights = null;
	private static Hashtable<String, List<Integer>> domainAncestors = null;
    public static void initialize (String eXDomainsFolder, String eXWDHFileName, Boolean memoryMode) throws NumberFormatException, IOException
    {
    	if (!initialized)
    	{
	    	ExDomainsHelper.eXDomainsFolder = eXDomainsFolder;
	    	ExDomainsHelper.eXWDHFileName = eXWDHFileName;
	    	ExDomainsHelper.memoryMode = memoryMode;
	    	// load domain files names vector and synsets ids vector
	    	File[] domainFiles = (new File(eXDomainsFolder)).listFiles();
	        domainFilesNames = new ArrayList<String>();
	        for (File domainFile : domainFiles)
	        {
	        	domainFilesNames.add(domainFile.getName());
	        }
	        synsytsIDs = new Hashtable<String, Integer>();
			BufferedReader br = new BufferedReader(new FileReader(eXDomainsFolder + "//" + domainFilesNames.get(0)));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) 
	    	{
	    		int indexOfSpace = line.indexOf("-") + 2;
	            String synsetID = line.substring(0, indexOfSpace); 
	            synsytsIDs.put(synsetID, lineNumber);
	            lineNumber++;
	    	}	
	        // end
			// load eXWDH
			loadDomainsHierarchy();			
			//end
	    	if (memoryMode)
	    	{
		    	loadWeightsToMemory();
	    	}
	    	initialized = true;
    	}
    }
    public static List<String> getDomainFilesNames()
    {
    	return domainFilesNames;
    }
    public static List<Integer> getDomainAncestors(String domainName)
    {
    	return ExDomainsHelper.domainAncestors.get(domainName);
    }
    private static void loadDomainsHierarchy() throws IOException
    {
    	domainAncestors = new Hashtable<String, List<Integer>>();
		for (String domainFileName : domainFilesNames)
		{
			String domainName = domainFileName.substring(0, domainFileName.length() - 4);
			ExDomainsHelper.domainAncestors.put(domainName, new ArrayList<Integer>());
		}
		BufferedReader br = new BufferedReader(new FileReader(ExDomainsHelper.eXWDHFileName));
		String line;
		// get index of direct parent
		while ((line = br.readLine()) != null) 
    	{
            int indexOfSpace = line.indexOf(" ");
            String domainName = line.substring(0, indexOfSpace);
            String parentName = line.substring(indexOfSpace + 1, line.length());
            int indexOfParent = ExDomainsHelper.domainFilesNames.indexOf(parentName + ".ppv");
            domainAncestors.get(domainName).add(indexOfParent);
    	}
		// get index for all ancestors
		for (String domainName : ExDomainsHelper.domainAncestors.keySet())
		{
			int indexOfParent = domainAncestors.get(domainName).get(0);
			while (indexOfParent != -1)
			{
				String parentFileName = domainFilesNames.get(indexOfParent);
				String parentName = parentFileName.substring(0, parentFileName.length() - 4);
				int indexOfGrandParent = domainAncestors.get(parentName).get(0);
				domainAncestors.get(domainName).add(indexOfGrandParent);
				indexOfParent = indexOfGrandParent;
			}
		}
    }
    private static void loadWeightsToMemory() throws NumberFormatException, IOException
    {
		weights = new float[domainFilesNames.size()][synsytsIDs.keySet().size()];
		String line;
		int indexOfSpace;
		float weight;
		BufferedReader br;	
		String domainFileName ;
		int s;
		for (int d = 0; d < domainFilesNames.size(); d++)
        {
			s = 0;
			domainFileName = domainFilesNames.get(d);
			System.err.println("loading domain " + (int)(d + 1) + ": " + domainFileName);
        	br = new BufferedReader(new FileReader(eXDomainsFolder + "//" + domainFileName));
        	while ((line = br.readLine()) != null) 
        	{
        		indexOfSpace = line.indexOf("-") + 2;
            	weight = Float.parseFloat(line.substring(indexOfSpace + 1));
                weights[d][s] = weight;
                s++;
        	}
        	br.close();              
        }
    }
    public static List<SynsetDomain> getDomains(String synsetID)
    {
    	List<SynsetDomain> result = null;
    	if (memoryMode)
    	{
    		result =  getDomainsMemoryModeEnabled(synsetID.toLowerCase());
    	}
    	else
    	{
    		result = getDomainsMemoryModeDisabled(synsetID.toLowerCase());
    	}
    	return result;
    }
    private static List<SynsetDomain> getDomainsMemoryModeEnabled(String synsetID)
    {
    	List<SynsetDomain> result = new ArrayList<SynsetDomain>();
    	try
    	{
        	int s = synsytsIDs.get(synsetID);
        	for (int d = 0; d < domainFilesNames.size(); d++)
        	{
        		String domainFileName = domainFilesNames.get(d);
        		String domainName = domainFileName.substring(0, domainFileName.length() - 4);
        		float weight = ExDomainsHelper.weights[d][s];  	
        		result.add(new SynsetDomain(domainName, weight));
        	}
    	}
        catch (Exception e) 
        {
        	e.printStackTrace();
        	return null;
		}
    	return result;
    }
    private static List<SynsetDomain> getDomainsMemoryModeDisabled(String synsetID)
    {
        List<SynsetDomain> result = new ArrayList<SynsetDomain>();
        try 
        {
        	int s = synsytsIDs.get(synsetID);
            for (String domainFileName : domainFilesNames)
            {
            	BufferedReader br = new BufferedReader(new FileReader(eXDomainsFolder + "//" + domainFileName));
            	String line;
                int lineNumber = 0;
            	while ((line = br.readLine()) != null) 
            	{
                    if (lineNumber == s)
                    {
                    	Integer indexOfSpace = line.indexOf("-") + 2;
                    	float weight = Float.parseFloat(line.substring(indexOfSpace + 1));
                        String domainName = domainFileName.substring(0, domainFileName.length() - 4);
                        result.add(new SynsetDomain(domainName, weight));
                        break;
                    }    
                    lineNumber ++;
            	}
            	br.close();              
            }        	
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        	return null;
		}
        return result;     
    }
}
