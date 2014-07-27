/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rulescreator;
import java.awt.Desktop;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
/**
 *
 * @author Anna Adjemian
 */
public class ManageRulesFile {
    private final String filePath = "rules.txt";
    
    public String[] readFromFile () throws IOException
    {       
       String[] rules = new String[100];
       int i = 0;
       try 
       {
           FileReader fr = new FileReader(filePath);
           BufferedReader br = new BufferedReader(fr); 
           while (br.ready())
           {
               String s = br.readLine();
               if (!s.trim().equals(""))
                  rules[i] = s;
               i++;
           }
           br.close();
       }
       catch (IOException e)
       {
           JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
       }
       
       return rules;
    }
    
    public void writeToFile (String[] rules) throws IOException
    {
        try
        {
            FileWriter fw = new FileWriter(filePath);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < rules.length; i++)
            {
                pw.println(rules[i]);
            }
            pw.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    public void viewFile ()
    {
        String osName = System.getProperty("os.name").toLowerCase();			
        boolean isWindows = osName.toUpperCase().startsWith("WINDOWS");
        if (isWindows)
        {
            try
            {
                    Desktop.getDesktop().open(new File(filePath));
            }
            catch (IOException e)
            {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            try 
            {
                    Runtime.getRuntime().exec("gedit " + new File(filePath).toURI());
            } 
            catch (IOException e) 
            {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
