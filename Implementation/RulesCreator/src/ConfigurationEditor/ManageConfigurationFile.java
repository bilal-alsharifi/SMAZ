/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author Anna Adjemian
 */
public class ManageConfigurationFile {
    
    private final String filePath = "config.txt";
    public boolean getDocContentFromProxy = false;
    public boolean sortDocDomainsByWeight = true;
    public boolean addWeightsToAncestors = false;
    public int shortWordsLimit  = 3;
    public String[] acceptedTags ={"NN" , "VB" , "JJ" , "RB"};
    public boolean searchInDbPedia = false;
    public String dbPediaPredicates = "";
    public int dbPediaResultsLimit = 3;
    public int disambiguationMethod = 2;
    public int leskWindowSize = 3;
    public int leskComparisonWay = 1;
    public boolean withCoreference = false;
    public String Ipaddress = "localhost";
    public String portNumber = "8080";
    
    public void readFromFile () throws IOException
    {       
       try 
       {
           FileReader fr = new FileReader(filePath);
           BufferedReader br = new BufferedReader(fr); 
           String serviceURL = br.readLine();
           Ipaddress = serviceURL.split("://")[1].split(":")[0];
           portNumber = serviceURL.split("://")[1].split(":")[1].split("/")[0];
           getDocContentFromProxy = Boolean.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           sortDocDomainsByWeight = Boolean.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           addWeightsToAncestors = Boolean.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           shortWordsLimit = Integer.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           acceptedTags = br.readLine().split("=")[1].split(";")[0].trim().split(",");
           searchInDbPedia = Boolean.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           dbPediaPredicates = br.readLine().split("=")[1].split(";")[0].trim();
           dbPediaResultsLimit = Integer.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           disambiguationMethod = Integer.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           leskWindowSize = Integer.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           leskComparisonWay = Integer.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           withCoreference = Boolean.valueOf(br.readLine().split("=")[1].split(";")[0].trim());
           
           br.close();
       }
       catch (IOException e)
       {
           JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
       }
       
    }
    
    public void writeToFile () throws IOException
    {
        try
        {
            FileWriter fw = new FileWriter(filePath);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("serviceURL = http://" + Ipaddress +":" + portNumber + "/SemanticAnalyzerService/index.jsp;");
            pw.println("getDocContentFromProxy = "+ getDocContentFromProxy + ";");
            pw.println("sortDocDomainsByWeight = " + sortDocDomainsByWeight+ ";");
            pw.println("addWeightsToAncestors = " + addWeightsToAncestors + ";");
            pw.println("shortWordsLimit = " + shortWordsLimit + ";");
            pw.print("acceptedTags = " );
            for (int i = 0 ; i < acceptedTags.length ; i++)
            {
                if (acceptedTags[i] == null)
                    break;
                if (i == acceptedTags.length -1 || acceptedTags[i+1] == null)
                    pw.println(acceptedTags[i] + ";");
                else
                    pw.print(acceptedTags[i] + ", ");
            }
            pw.println("searchInDbPedia = " + searchInDbPedia+ ";");
            pw.println("dbPediaPredicates = " + dbPediaPredicates+ ";");
            pw.println("dbPediaResultsLimit = " + dbPediaResultsLimit+ ";");
            pw.println("disambiguationMethod = " + disambiguationMethod+ ";");
            pw.println("leskWindowSize = " + leskWindowSize+ ";");
            pw.println("leskComparisonWay = " + leskComparisonWay+ ";");
            pw.println("withCoreference = " + withCoreference+ ";");
            
            pw.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
