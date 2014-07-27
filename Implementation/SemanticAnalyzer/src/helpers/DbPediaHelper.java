package helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class DbPediaHelper 
{

	public static void initializeJena(String genaLog4jProprtiesFileName)
	{
		PropertyConfigurator.configure(genaLog4jProprtiesFileName);
	}
	private static String getResponseFromSpotLight(String text) throws IOException
	{
		String urlParameters = "text=" + text;
		URL url = new URL("http://spotlight.dbpedia.org/rest/annotate");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("Accept", "application/xhtml+xml");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream ());
		writer.writeBytes(urlParameters);
		writer.flush();		
		String line;
		StringBuffer response = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) 
		{
		    response.append(line + "\r");
		}		
		writer.close();
		reader.close();
		connection.disconnect();
		return response.toString();
	}
	private static String getURIFromSpotLight(String text) throws ParserConfigurationException, SAXException, IOException
	{
		String result = null;
		String htmlText = getResponseFromSpotLight(text);
		Document doc = Jsoup.parse(htmlText);
		Elements elements = doc.select("a");
		if (elements.size() > 0)
		{
			result = elements.get(0).attr("href");
		}
		return result;
	}
	private static List<QuerySolution> queryDbPedia(String queryString)
	{
		List<QuerySolution> result = new ArrayList<>();		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet resultSet = qexec.execSelect();
	    while (resultSet.hasNext()) 
	    {
	    	result.add(resultSet.next());
	    }
	    qexec.close();
	    return result;
	}
	public static String getDefinitionTextFromDbPedia(String word, List<String> dbPediaPredicates, int dbPediaResultsLimit) throws ParserConfigurationException, SAXException, IOException
	{
		String result = "";
		if (dbPediaPredicates == null || dbPediaPredicates.size() == 0)
		{
			return null;
		}
		String uri = getURIFromSpotLight(word);
		if (uri == null)
		{
			return null;
		}
		// build the query
		String whereClause = "";
		for (int i = 0; i < dbPediaPredicates.size(); i++)
		{
			if (i > 0)
			{
				whereClause += " UNION ";
			}
			whereClause += "{<" + uri + "> <" + dbPediaPredicates.get(i) + "> ?ob}";
		}	
		String queryString = "SELECT * WHERE {" + whereClause + "}";
		if (dbPediaResultsLimit > 0)
		{
			queryString += " LIMIT" + dbPediaResultsLimit; 
		}
		// end
		List<QuerySolution> queryResults = queryDbPedia(queryString);
		String newLine = System.getProperty("line.separator");
		for (QuerySolution qs : queryResults)
		{
			String ob = qs.get("ob").toString();
			// clean the object
			if (FileReaderHelper.isURL(ob))
			{
				int indexOfSlash = ob.lastIndexOf("/");
				ob = ob.substring(indexOfSlash + 1);
			}
			int indexOfSemicolon = ob.lastIndexOf(":");
			if (indexOfSemicolon != -1 && indexOfSemicolon < 15)
			{
				ob = ob.substring(indexOfSemicolon + 1);
			}
			// end
			result +=  ob + newLine;
		}
		return result;
	}
}
