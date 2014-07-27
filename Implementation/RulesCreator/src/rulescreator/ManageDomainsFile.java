/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rulescreator;

import com.sun.deploy.util.StringUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Anna Adjemian
 */
public class ManageDomainsFile {
    
    public String filePath;
    public String domains;
    public boolean first = true;
    public DefaultMutableTreeNode root;
    
    public ManageDomainsFile (String path)
    {
        filePath = path;
        domains = "";
    }
    
    public void readFile ()
    {
       try 
       {
           FileReader fr = new FileReader(filePath);
           BufferedReader br = new BufferedReader(fr); 
           String line = "";
           while (br.ready())
           {
               line = br.readLine();
               domains += line + ",";
           }
           br.close();
       }
       catch (IOException e)
       {
           JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
       }
    }
    
    public DefaultMutableTreeNode getDomains ()
    {
        root = new DefaultMutableTreeNode("Root");;
        findDomain("root", root);
        return root;
    }
    
    public void findDomain (String dom, DefaultMutableTreeNode parent)
    {
        String[] parts = domains.split(",");
        
        for (int i = 0 ; i < parts.length; i++)
        {
            if (parts[i].split(" ")[1].equals(dom))
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(parts[i].split(" ")[0]);
                findDomain(parts[i].split(" ")[0], node);
                parent.add(node);
                
            }
        }
    }
}
