public class Program 
{
public static void main(String[] args) 
{       
	class Domain
	{
		private String name;
		private String op;
		private float weight;
		public Domain(String domainString)
		{
			if (domainString.indexOf(">=") != -1)
			{
				this.op = ">=";
			}
			else if (domainString.indexOf("<=") != -1)
			{
				this.op = "<=";
			}
			else if (domainString.indexOf(">") != -1)
			{
				this.op = ">";
			}
			else if (domainString.indexOf("<") != -1)
			{
				this.op = "<";
			}
			else if (domainString.indexOf("=") != -1)
			{
				this.op = "=";
			}				
			int indexOfOp = domainString.indexOf(this.op);
			this.name = domainString.substring(0, indexOfOp);
			this.weight = Float.parseFloat(domainString.substring(indexOfOp + op.length(), domainString.length()));
		}
		public Boolean doesNotConflictWith(Domain docDomain)
		{
			Boolean result = true;
			if (!this.name.equalsIgnoreCase(docDomain.name))
			{
				return result;
			}
			switch (this.op) 
			{
				case ">=":
				{
					result = docDomain.weight >= this.weight;
					break;
				}
				case "<=":
				{
					result = docDomain.weight <= this.weight;
					break;
				}
				case ">":
				{
					result = docDomain.weight > this.weight;
					break;
				}
				case "<":
				{
					result = docDomain.weight < this.weight;
					break;
				}
				case "=":
				{
					result = docDomain.weight == this.weight;
					break;
				}
			}
			return result;
		}
	    @Override
	    public String toString()
	    {
	        return this.name + this.op + this.weight;
	    }
	}
	class Rule
	{
		private java.util.List<Domain> domians;
		private java.util.List<java.util.List<String>> synonymsSets;
		private String action;
		public Rule(String ruleString)
		{
			String cleanedRuleString = ruleString.replaceAll("\\s",""); // clean white spaces
			int i = cleanedRuleString.indexOf("(");
			int j = cleanedRuleString.indexOf(")");
			int k = cleanedRuleString.indexOf(":");
			String domainsString;
			if (i == -1) // if there are no sysonymsSets
			{
				domainsString = cleanedRuleString.substring(0, k);
			}
			else
			{
				domainsString = cleanedRuleString.substring(0, i);
			}
			String [] domainsStrings;
			if (domainsString.length() > 0)
			{
				domainsStrings = domainsString.split("&");
			}
			else
			{
				domainsStrings = new String[0];
			}				
			this.domians = new java.util.ArrayList<>();
			for (String domainString : domainsStrings)
			{
				Domain d = new Domain(domainString);
				this.domians.add(d);
			}	
			String synonymsSetsString;
			if (i == -1)
			{
				synonymsSetsString = "";
			}
			else
			{
				synonymsSetsString = cleanedRuleString.substring(i + 1, j);
			}
			String [] synonymsSetsStrings;
			if (synonymsSetsString.length() > 0)
			{
				synonymsSetsStrings = synonymsSetsString.split("&");
			}
			else
			{
				synonymsSetsStrings = new String[0];
			}
			this.synonymsSets = new java.util.ArrayList<>();
			for (String synonymsSetString : synonymsSetsStrings)
			{
				java.util.List<String> synonymsSet = new java.util.ArrayList<>();
				java.util.Collections.addAll(synonymsSet, synonymsSetString.split(","));
				this.synonymsSets.add(synonymsSet);
			}	
			this.action = cleanedRuleString.substring(k + 1, cleanedRuleString.length());				
		}
		private Boolean domainsMatches(java.util.List<Domain> docDomains)
		{
			Boolean result = true;
			for (Domain ruleDomain : this.domians)
			{
				for (Domain docDomain : docDomains)
				{
					if (!ruleDomain.doesNotConflictWith(docDomain))
					{
						result = false;
						return result;
					}
				}
			}
			return result;
		}
		private Boolean synonymsSetsMatches(String docContent)
		{
			Boolean result = true;
			docContent = docContent.toLowerCase();
			for (java.util.List<String> synonymsSet : this.synonymsSets)
			{
				Boolean partialResult = false;
				for (String word : synonymsSet)
				{
					if (docContent.contains(word.toLowerCase()))
					{
						partialResult = true;
						break;
					}
				}
				if (partialResult == false)
				{
					result = false;
					return result;
				}
			}
			return result;
		}
		private Boolean matches(java.util.List<Domain> docDomains, String docContent)
		{
			Boolean result = domainsMatches(docDomains) && synonymsSetsMatches(docContent);			
			return result;
		}
	    @Override
	    public String toString()
	    {
	        return this.domians.toString() + this.synonymsSets + ":" + this.action;
	    }
	}
	class SemanticAnalyzer
	{
		public String getResponse(String serviceURL, String input, Boolean inputIsText, Boolean sortDocDomainsByWeight, Boolean addWeightsToAncestors, int shortWordsLimit, String acceptedTags, Boolean searchInDbPedia, String dbPediaPredicates, int dbPediaResultsLimit, int disambiguationMethod, int leskWindowSize, int leskComparisonWay, Boolean withCoreference) throws java.io.IOException
		{
			input = java.net.URLEncoder.encode(input, "utf-8");
			serviceURL = serviceURL + "?" + "input=" + input + "&" + "inputIsText=" + inputIsText + "&" + "sortDocDomainsByWeight=" + sortDocDomainsByWeight + "&" + "addWeightsToAncestors=" + addWeightsToAncestors + "&" + "shortWordsLimit=" + shortWordsLimit + "&" + "acceptedTags=" + acceptedTags + "&" + "searchInDbPedia=" + searchInDbPedia + "&" + "dbPediaPredicates=" + dbPediaPredicates + "&" + "dbPediaResultsLimit=" + dbPediaResultsLimit + "&" + "disambiguationMethod=" + disambiguationMethod + "&" + "leskWindowSize=" + leskWindowSize + "&" + "leskComparisonWay=" + leskComparisonWay + "&" + "withCoreference=" + withCoreference;
			java.net.URL url = new java.net.URL(serviceURL);
			java.net.URLConnection connection = url.openConnection();
	        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
	        String inputLine;
		    String newLine = System.getProperty("line.separator");
			StringBuffer response = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) 
	        {
	            response.append(inputLine + newLine);
	        }
	        in.close();
	        return response.toString();
		}
		public java.util.List<Domain> getDomains(String response)
		{
			java.util.List<Domain> result = new java.util.ArrayList<>();
			String beginSeparator = "<pre>";
			String endSeparator = "</pre>";
			int i = response.indexOf(beginSeparator) ;
			int j = response.indexOf(endSeparator);
			if (i == -1 || j == -1) // there is an error in the service response
			{
				return result;
			}
			String cleanedResponse = response.substring(i + beginSeparator.length(), j);
			String newLine = System.getProperty("line.separator");
			String [] domainsStrings = cleanedResponse.split(newLine);
			for (String domainString : domainsStrings)
			{
				Domain d = new Domain(domainString);
				result.add(d);
			}			
			return result;
		}
	}
	class GeneralHelper
	{
		private String readFile(String path) throws java.io.FileNotFoundException
		{
			String result = null;
			java.io.File file = new java.io.File(path);
			java.util.Scanner scan = new java.util.Scanner(file);  
			scan.useDelimiter("\\Z");  
			result = scan.next(); 
			return result;
		}
		public java.util.List<Rule> getRules() throws java.io.FileNotFoundException
		{
			java.util.List<Rule> rules = new java.util.ArrayList<>();
			String rulesString = readFile("rules.txt");
			String [] rulesStrings = rulesString.split(";");
			for (String ruleString : rulesStrings)
			{
				Rule rule = new Rule(ruleString);
				rules.add(rule);
			}
			return rules;
		}
		public String getConfigString() throws java.io.FileNotFoundException
		{
			String result = readFile("config.txt");
			result = result.replaceAll("\\s",""); // clean white spaces
			return result;
		}
		public String getConfigParamValue(String configString, String paramName)
		{
			String result = null;
			String str = configString;
			int i = str.indexOf(paramName);			
			str = str.substring(i + paramName.length() + 1, configString.length());
			int j = str.indexOf(";");
			result = str.substring(0 , j);
			return result;
		}
	}
	
	
	//load rules and config from files
	GeneralHelper gh = new GeneralHelper();
	java.util.List<Rule> rules = null;
	String configString = null;
	String errorPageString = null;
	try 
	{
		rules = gh.getRules();
		configString = gh.getConfigString();
		errorPageString = gh.readFile("errorPage.htm");
	} 
	catch (java.io.FileNotFoundException e) 
	{
		//////////////// change here ////////////////
		/////////////////////////////////////////////
		/////////////////////////////////////////////
		System.out.println("required file not found");
		//httpMessage.setBody("required file not found");
		return;
	}
	String serviceURL = gh.getConfigParamValue(configString, "serviceURL");
	Boolean getDocContentFromProxy = Boolean.parseBoolean(gh.getConfigParamValue(configString, "getDocContentFromProxy"));
	Boolean sortDocDomainsByWeight = Boolean.parseBoolean(gh.getConfigParamValue(configString, "sortDocDomainsByWeight"));
	Boolean addWeightsToAncestors = Boolean.parseBoolean(gh.getConfigParamValue(configString, "addWeightsToAncestors"));
	Integer shortWordsLimit = Integer.parseInt(gh.getConfigParamValue(configString, "shortWordsLimit"));
	String acceptedTags = gh.getConfigParamValue(configString, "acceptedTags");
	Boolean searchInDbPedia = Boolean.parseBoolean(gh.getConfigParamValue(configString, "searchInDbPedia"));
	String dbPediaPredicates = gh.getConfigParamValue(configString, "dbPediaPredicates");
	Integer dbPediaResultsLimit = Integer.parseInt(gh.getConfigParamValue(configString, "dbPediaResultsLimit"));
	Integer disambiguationMethod = Integer.parseInt(gh.getConfigParamValue(configString, "disambiguationMethod"));
	int leskWindowSize = Integer.parseInt(gh.getConfigParamValue(configString, "leskWindowSize"));
	int leskComparisonWay = Integer.parseInt(gh.getConfigParamValue(configString, "leskComparisonWay"));
	Boolean withCoreference = Boolean.parseBoolean(gh.getConfigParamValue(configString, "withCoreference"));
	//--------------------------

		
	//////////////// change here ////////////////
	/////////////////////////////////////////////
	/////////////////////////////////////////////	
	String input;
	Boolean inputIsText;
	String docContent = "sexy girl";
	//String docContent = httpMessage.getBody();
	if (getDocContentFromProxy)
	{	
		input = docContent;
		inputIsText = true;
	}
	else
	{
		input = "http://en.wikipedia.org/wiki/Sex";
		//input = httpMessage.getUrl();
		inputIsText = false;
	}
	
	try 
	{
		Boolean blocked = false;
		SemanticAnalyzer sa = new SemanticAnalyzer();
		String response = sa.getResponse(serviceURL, input, inputIsText, sortDocDomainsByWeight, addWeightsToAncestors, shortWordsLimit, acceptedTags, searchInDbPedia, dbPediaPredicates, dbPediaResultsLimit, disambiguationMethod, leskWindowSize, leskComparisonWay, withCoreference);
		java.util.List<Domain> docDomains = sa.getDomains(response);
		if (docDomains.size() != 0) // if there are no errors in server response
		{				
			for (Rule rule : rules)
			{
				Boolean ruleMatchesDoc = rule.matches(docDomains, docContent);
				System.out.println(rule + "     (matches: "+ ruleMatchesDoc + ")");
				if (ruleMatchesDoc)
				{
					if (rule.action.equalsIgnoreCase("b"))
					{
						blocked = true;
					}
					break; // do not check the rules after this rule
				}
			}
		}
		else
		{
			//////////////// change here ////////////////
			/////////////////////////////////////////////
			/////////////////////////////////////////////
			System.out.println("there is an error in service response");
			//httpMessage.setBody("there is an error in service response");
		}
		
		if (blocked)
		{
			//////////////// change here ////////////////
			/////////////////////////////////////////////
			/////////////////////////////////////////////
			//httpMessage.setBody(errorPageString);
		}
		System.out.println("blocked: " + blocked);	
		System.out.println("---------------------------------------------");
		//httpMessage.setBody(httpMessage.getBody() +  "<!--" + response + "-->");
	} 
	catch (java.io.IOException e) 
	{
		//////////////// change here ////////////////
		/////////////////////////////////////////////
		/////////////////////////////////////////////			
		System.out.println(e.toString());
		//httpMessage.setBody(e.toString());
	}
	
	
}

	

}
