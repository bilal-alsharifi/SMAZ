package rulescreator;


import edu.mit.jwi.IDictionary;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import rulescreator.DomainsDialogModel;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import java.awt.Color;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anna Adjemian
 */
public class RulesModel extends javax.swing.JPanel {

    /**
     * Creates new form RulesModel
     */
    int rowsNum = 0;
    int xDistance = 15;
    int yDistance = 0;
    SpringLayout layout = new SpringLayout();
    JPanel jPanel1;
    boolean first = true;
    int pWidth = 0;
    int pHeight = 0;
    int sWidth = 0;
    int sHeight = 0;
    String rule = "";
    List<JTextField> valueTextFields = new ArrayList<JTextField>();
    List<JComboBox> comboboxs = new ArrayList<JComboBox>();
    List<JTextField> domainTextFields = new ArrayList<JTextField>();
    List<JButton> buttons = new ArrayList<JButton>();
    String ruleBuilder = "";
    JDialog dialog;
    DomainsDialogModel dialogPanel= new DomainsDialogModel();
    IDictionary dict;
    ArrayList<ArrayList<String>> keywordsSynonyms = new ArrayList<ArrayList<String>>();
    String osName = "";			
    boolean isWindows = false; 
    
    public RulesModel(String rule) {
        initComponents();
        this.rule = rule;
        osName = System.getProperty("os.name").toLowerCase();
        isWindows = osName.toUpperCase().startsWith("WINDOWS");
        if (rule != "")
            jLabel2.setText(rule);

        jRadioButton1.setActionCommand("b");
        jRadioButton2.setActionCommand("u");
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));

        try
        {
            URL url = new URL("file", null , "dict30");

            // load from hard disk
            dict = new Dictionary (url);
            dict.open();
        }
        catch (IOException e)
        {
            
        }
        if (rule != "") // edit rule
        {
            ruleBuilder += rule;
            
            String[] ruleParts = new String[50];
            if (rule.contains("("))
            {
                ruleParts = rule.split("\\(")[0].split(" & ");
                String keywords  =  rule.split("\\(")[1].split("\\)")[0].trim();
                String [] syns = keywords.split("&");
                ArrayList<String> synonyms = new ArrayList<String>();
                for (int i = 0 ; i < syns.length; i++)
                {
                    String [] keywordSyns = syns[i].split(",");
                    for (int j = 0 ; j < keywordSyns.length; j++)
                    {
                        synonyms.add(keywordSyns[j]);
                    }
                    keywordsSynonyms.add(synonyms);
                }
                jTextArea1.setText("");
                jTextField1.setText("");
            }
            else 
            {
                ruleParts = rule.split(":")[0].split(" & ");
                jTextArea1.setText("");
            }
            
            if (rule.split(":")[1].split(" ")[1].split(";")[0].equals("b"))
                jRadioButton1.setSelected(true);
            else
                jRadioButton2.setSelected(true);
            for (int i = 0 ; i< ruleParts.length; i++)
            {
                if (domainTextFields.size() > 0)
                     jPanel2.add(new JLabel(" and "));
                addRule(ruleParts[i].split(" ")[0], ruleParts[i].split(" ")[1], ruleParts[i].split(" ")[2]);
            }
        }
        else   //add new rule
        {
            addRule("Social", ">" , "0.1");
        }
        
        ActionListener listener = new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) 
            {
                ruleBuilder = "";
                boolean wrongValue = false;
                if ((valueTextFields.size() > 0) )
                {
                    int i = 0;
                    while (i < valueTextFields.size() && !wrongValue)
                    {
                        if (Float.valueOf(valueTextFields.get(i).getText()) > 1)
                        {
                            JOptionPane.showMessageDialog(null, "The value of chunk "+ i+1 +" should be between 0 and 1");
                            wrongValue = true;
                        }
                        else
                            i++;
                    }
                }
                if (!wrongValue)
                {
                    for(int i = 0 ; i < valueTextFields.size(); i++)
                    {
                        //ImageIcon img = (ImageIcon)comboboxs.get(i).getModel().getSelectedItem();
                        //ruleBuilder += domainTextFields.get(i).getText() + " " + img.getDescription().toString() + " " + valueTextFields.get(i).getText() + " " ;
                        ruleBuilder += domainTextFields.get(i).getText() + " " + comboboxs.get(i).getSelectedItem().toString() + " " + valueTextFields.get(i).getText() + "  " ;
                        //if (valueTextFields.size() > 1 && i < valueTextFields.size()-1)
                        //    ruleBuilder += " & ";
                    }
                    if (domainTextFields.size() > 0)
                        jPanel2.add(new JLabel(" and "));
                    addRule("Social" , ">", "0.1");
                    jLabel2.setText(ruleBuilder + " & Social > 0.1");
                }

            }
        };
        jButton4.addActionListener(listener);
        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setText("Add new chunk");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Rule Editor");

        jScrollPane1.setViewportView(jLabel2);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("The Created Rule");

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Save Rule");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Block");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("unBlock");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Choose whether to block a page that satisfies the rule above or not");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Add Keyword");

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setText("Find Synonyms");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setText("Add to rule");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("The keyword's synonyms");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(133, 133, 133)
                                        .addComponent(jButton2)
                                        .addGap(40, 40, 40)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(214, 214, 214)
                                        .addComponent(jLabel3))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3))
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton4)
                                                .addGap(14, 14, 14))))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(20, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 32, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here: reset rules

        domainTextFields = new ArrayList<JTextField>();
        valueTextFields = new ArrayList<JTextField>();
        comboboxs = new ArrayList<JComboBox>();
        buttons = new ArrayList<JButton>();

        keywordsSynonyms = new ArrayList<ArrayList<String>>();
        jPanel2.removeAll();
        jPanel2.repaint();
        jPanel2.revalidate();
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
        jLabel2.setText(rule);
        if (rule != "") // edit rule
        {
            ruleBuilder += rule;

            String[] ruleParts = new String[50];
            if (rule.contains("("))
            {
                ruleParts = rule.split("\\(")[0].split(" & ");
                //jTextArea1.setText(rule.split("\\(")[1].split("\\)")[0]);
                String keywords  =  rule.split("\\(")[1].split("\\)")[0].trim();
                String [] syns = keywords.split("&");
                ArrayList<String> synonyms = new ArrayList<String>();
                for (int i = 0 ; i < syns.length; i++)
                {
                    String [] keywordSyns = syns[i].split(",");
                    for (int j = 0 ; j < keywordSyns.length; j++)
                    {
                        synonyms.add(keywordSyns[j]);
                    }
                    keywordsSynonyms.add(synonyms);
                }
                jTextArea1.setText("");
                jTextField1.setText("");
            }
            else
            {
                ruleParts = rule.split(":")[0].split(" & ");
                jTextArea1.setText("");
                jTextField1.setText("");
            }

            if (rule.split(":")[1].split(" ")[1].split(";")[0].equals("b"))
            jRadioButton1.setSelected(true);
            else
            jRadioButton2.setSelected(true);
            for (int i = 0 ; i< ruleParts.length; i++)
            {
                if (domainTextFields.size() > 0)
                jPanel2.add(new JLabel(" and "));
                addRule(ruleParts[i].split(" ")[0], ruleParts[i].split(" ")[1], ruleParts[i].split(" ")[2]);
            }
        }
        else   //add new rule
        {
            addRule("Social", ">" , "0.1");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here: save rule
        String s = "";
        for(int i = 0 ; i < valueTextFields.size(); i++)
        {
            //ImageIcon img = (ImageIcon)comboboxs.get(i).getModel().getSelectedItem();
            //s += domainTextFields.get(i).getText() + " " + img.getDescription().toString() + " " + valueTextFields.get(i).getText() + "  " ;
            s += domainTextFields.get(i).getText() + " " + comboboxs.get(i).getSelectedItem().toString() + " " + valueTextFields.get(i).getText() + "  " ;
            if (i < valueTextFields.size() - 1)
                s += "& ";
        }

        if (keywordsSynonyms.size() > 0)
        {
            s += "(";
            for (int j = 0 ; j < keywordsSynonyms.size(); j++)
            {
                ArrayList<String> keywordSynonyms = keywordsSynonyms.get(j);
                for (int k = 0 ; k < keywordSynonyms.size(); k++)
                {
                    if (k < keywordSynonyms.size() - 1)
                        s += keywordSynonyms.get(k) + ",";
                    else
                        s += keywordSynonyms.get(k);
                }
                if (j < keywordsSynonyms.size() - 1)
                    s += " & ";
            }
            s += ") ";
        }
        s += ": ";
        s += buttonGroup1.getSelection().getActionCommand() + ";";
        jLabel2.setText(s);
        ruleBuilder = s;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        // get SynSystes that contains the word
        String keyword = jTextField1.getText();
        if (!keyword.equals(""))
        {
            String result = "";
            IIndexWord idxWord = dict.getIndexWord (keyword, POS.NOUN);
            if (idxWord != null)
            {
                List<IWordID> wordIDs = idxWord.getWordIDs();
                ArrayList<String> synonyms = new ArrayList<String>();
                for (int i = 0; i < wordIDs.size(); i++) 
                {     
                    IWord sense = dict.getWord(wordIDs.get(i));   
                    if (sense.getSynset().getWords().size() > 1)
                        for (int j = 0 ; j < sense.getSynset().getWords().size() ; j++)
                        {
                            if (!synonyms.contains(sense.getSynset().getWords().get(j).getLemma()))
                                synonyms.add(sense.getSynset().getWords().get(j).getLemma());
                        }
                }
                //keywordsSynonyms.add(synonyms);
                String s = "";
                 for (int k = 0 ; k < synonyms.size(); k++)
                 {
                     s +=  synonyms.get(k);
                     if (k < synonyms.size()-1)
                         s +=   " ,";
                 }
                 jTextArea1.setText(s);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Enter keyword to find its synonyms!");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here: add keyword and its synonyms to rule
        String s = "";
        for(int i = 0 ; i < valueTextFields.size(); i++)
        {
            //ImageIcon img = (ImageIcon)comboboxs.get(i).getModel().getSelectedItem();
            //s += domainTextFields.get(i).getText() + " " + img.getDescription().toString() + " " + valueTextFields.get(i).getText() + "  " ;
            s += domainTextFields.get(i).getText() + " " + comboboxs.get(i).getSelectedItem().toString() + " " + valueTextFields.get(i).getText() + "  " ;
            if (i < valueTextFields.size() - 1)
                s += "& ";
        }

        if (!jTextArea1.getText().equals(""))
        {
            s += "(";
            String [] ss = jTextArea1.getText().trim().split(",");
            ArrayList<String> synonyms = new ArrayList<String>();
            for (int j = 0 ; j < ss.length; j++)
            {
                synonyms.add(ss[j]);
            }
            keywordsSynonyms.add(synonyms);
            for (int j = 0 ; j < keywordsSynonyms.size(); j++)
            {
                ArrayList<String> keywordSynonyms = keywordsSynonyms.get(j);
                for (int k = 0 ; k < keywordSynonyms.size(); k++)
                {
                    if (k < keywordSynonyms.size() - 1)
                        s += keywordSynonyms.get(k) + ",";
                    else
                        s += keywordSynonyms.get(k);
                }
                if (j < keywordsSynonyms.size() - 1)
                    s += " & ";
            }
            s += ") ";
        }
        else if (!jTextField1.getText().equals(""))
        {
            s += "(";
            ArrayList<String> synonyms = new ArrayList<String>();
            synonyms.add( jTextField1.getText());
            keywordsSynonyms.add(synonyms);
            
            for (int j = 0 ; j < keywordsSynonyms.size(); j++)
            {
                ArrayList<String> keywordSynonyms = keywordsSynonyms.get(j);
                for (int k = 0 ; k < keywordSynonyms.size(); k++)
                {
                    if (k < keywordSynonyms.size() - 1)
                        s += keywordSynonyms.get(k) + ",";
                    else
                        s += keywordSynonyms.get(k);
                }
                if (j < keywordsSynonyms.size() - 1)
                    s += " & ";
            }
            s += ") ";
        }
        s += ": ";
        s += buttonGroup1.getSelection().getActionCommand() + ";";
        jLabel2.setText(s);
        ruleBuilder = s;
        jTextArea1.setText("");
        jTextField1.setText("");
                
    }//GEN-LAST:event_jButton5ActionPerformed

    private void addRule (String dom, String op, String val)
    {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        JTextField domain = new JTextField(dom, 10);
        domain.setMaximumSize( domain.getPreferredSize() );
        domain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ((JTextField)evt.getSource()).setText(openDialog());
            }
        });
        domain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ((JTextField)evt.getSource()).setText(openDialog());
            }
        });
        bottom.add(domain);
        bottom.add(Box.createRigidArea(new Dimension(0, 10)));

        JComboBox operator = new JComboBox(fillOperators());
        operator.setPreferredSize(new Dimension(50, 30));
        operator.setMaximumSize( operator.getPreferredSize() );
        //operator.setSelectedIndex(getComboSelectedIndex(op));
        operator.setSelectedItem(op);
        //operator.setBackground(Color.white);
        bottom.add(operator);
        bottom.add(Box.createRigidArea(new Dimension(10, 10)));

        JTextField text = new JTextField(val , 6);
        text.setMaximumSize( text.getPreferredSize() );
        bottom.add(text);
        bottom.add(Box.createRigidArea(new Dimension(20, 10)));

        String imagePath = "";
        
        if (isWindows)
        {
                imagePath = "photos\\remove.png";
        }
        else
        {
                imagePath = "photos/remove.png";
        }
        ImageIcon del = new ImageIcon(imagePath);
        
        JButton delete = new JButton(del);
        delete.setActionCommand(String.valueOf(buttons.size()));
        ActionListener listener1 = new ActionListener()  
        {
            public void actionPerformed(ActionEvent ae) 
            {
               resetPanel(Integer.valueOf(ae.getActionCommand()));
            }
        };
        delete.addActionListener(listener1);
        bottom.add(delete);
        bottom.add(Box.createRigidArea(new Dimension(20, 10)));
        
        jPanel2.add(bottom);
        jPanel2.revalidate();

        //jLabel2.setText(ruleBuilder);
        domainTextFields.add(domain);
        comboboxs.add(operator);
        buttons.add(delete);
        valueTextFields.add(text);
    }
    
    private String openDialog ()
    {
        Window win = SwingUtilities.getWindowAncestor(this);
        dialog = new JDialog(win, "Domains Dialog",Dialog.ModalityType.APPLICATION_MODAL);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialogPanel.domain;
    }
    private void resetPanel (int removeBoxNum)
    {
        valueTextFields.remove(removeBoxNum);
        domainTextFields.remove(removeBoxNum);
        buttons.remove(removeBoxNum);
        comboboxs.remove(removeBoxNum);
        
        jPanel2.removeAll();
        jPanel2.repaint();
        jPanel2.revalidate();
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
        
        for (int i = 0 ; i < domainTextFields.size(); i++)
        {
            JPanel bottom = new JPanel();
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

            bottom.add(domainTextFields.get(i));
            bottom.add(Box.createRigidArea(new Dimension(0, 10)));

            bottom.add(comboboxs.get(i));
            bottom.add(Box.createRigidArea(new Dimension(10, 10)));

            bottom.add(valueTextFields.get(i));
            bottom.add(Box.createRigidArea(new Dimension(20, 10)));

            buttons.get(i).setActionCommand(String.valueOf(i));
            bottom.add(buttons.get(i));
            bottom.add(Box.createRigidArea(new Dimension(20, 10)));
            
            jPanel2.add(bottom);
            if (i != domainTextFields.size()-1)
                jPanel2.add(new JLabel("and"));
            
            jPanel2.revalidate();
        }            
    }
    private int getComboSelectedIndex (String op)
    {
            if (op.equals(">" ))
                return 0;
            else if (op.equals(">="))
                return 1;
            else if (op.equals("<"))
                return 2;
            else if (op.equals("<="))
                return 3;
            else
                return 4;
    }
    
    
    private DefaultComboBoxModel fillOperators  ()
    {
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
//        if (isWindows)
//        {
//            ImageIcon icon = new ImageIcon("photos\\gt.png", ">");
//            ImageIcon icon1 = new ImageIcon("photos\\gte.png", ">=");
//            ImageIcon icon2 = new ImageIcon("photos\\lt.png", "<");
//            ImageIcon icon3 = new ImageIcon("photos\\lte.png", "<=");
//            ImageIcon icon4 = new ImageIcon("photos\\equal.png", "=");
//
//            comboModel.addElement(icon);
//            comboModel.addElement(icon1);
//            comboModel.addElement(icon2);
//            comboModel.addElement(icon3);
//            comboModel.addElement(icon4);
//        }
//        else
//        {
//            ImageIcon icon = new ImageIcon("photos/gt.png", ">");
//            ImageIcon icon1 = new ImageIcon("photos/gte.png", ">=");
//            ImageIcon icon2 = new ImageIcon("photos/lt.png", "<");
//            ImageIcon icon3 = new ImageIcon("photos/lte.png", "<=");
//            ImageIcon icon4 = new ImageIcon("photos/equal.png", "=");
//            
//            comboModel.addElement(icon);
//            comboModel.addElement(icon1);
//            comboModel.addElement(icon2);
//            comboModel.addElement(icon3);
//            comboModel.addElement(icon4);
//        }

        comboModel.addElement(">");
        comboModel.addElement(">=");
        comboModel.addElement("<");
        comboModel.addElement("<=");
        comboModel.addElement("=");
        return comboModel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
