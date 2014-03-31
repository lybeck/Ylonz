/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import restaurants.Restaurant;
import restaurants.RestaurantSearch;

/**
 *
 * @author Hedd
 */
public class GUI extends javax.swing.JFrame {

    private final RestaurantSearch restaurantSearch;
    private List<Restaurant> matchingRestaurants;
    
    /**
     * Creates new form GUIBoy
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public GUI() throws IOException, ClassNotFoundException {
        restaurantSearch = new RestaurantSearch();

        initComponents();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResultList();
            }            
        });
        
        resultList.addListSelectionListener((ListSelectionEvent e) -> {
            int index = resultList.getSelectedIndex();
            if (index < 0)
                itemDetails.setText("");
            else
                itemDetails.setText(matchingRestaurants.get(index).toString());
        });
        
        updateResultList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchMode = new javax.swing.ButtonGroup();
        partialMatch = new javax.swing.JRadioButton();
        anagram = new javax.swing.JRadioButton();
        searchField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultList = new javax.swing.JList<String>();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemDetails = new javax.swing.JTextArea();
        crossword = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        searchMode.add(partialMatch);
        partialMatch.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        partialMatch.setSelected(true);
        partialMatch.setText("Partial match");
        partialMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partialMatchActionPerformed(evt);
            }
        });

        searchMode.add(anagram);
        anagram.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        anagram.setText("Anagram solver");
        anagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anagramActionPerformed(evt);
            }
        });

        searchField.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        searchField.setToolTipText("");

        resultList.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        resultList.setToolTipText("");
        resultList.setAutoscrolls(false);
        jScrollPane1.setViewportView(resultList);

        itemDetails.setColumns(20);
        itemDetails.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        itemDetails.setRows(5);
        itemDetails.setWrapStyleWord(true);
        jScrollPane2.setViewportView(itemDetails);

        searchMode.add(crossword);
        crossword.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        crossword.setText("Crossword solver");
        crossword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crosswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(anagram)
                            .addComponent(partialMatch)
                            .addComponent(crossword))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partialMatch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(anagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8)
                        .addComponent(crossword)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void anagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anagramActionPerformed
        if (anagram.isSelected())
            updateResultList();
    }//GEN-LAST:event_anagramActionPerformed

    private void partialMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialMatchActionPerformed
        if (partialMatch.isSelected())
            updateResultList();
    }//GEN-LAST:event_partialMatchActionPerformed

    private void crosswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crosswordActionPerformed
        if (crossword.isSelected())
            updateResultList();
    }//GEN-LAST:event_crosswordActionPerformed

    
    private void updateResultList() {
        if (partialMatch.isSelected())
            matchingRestaurants = restaurantSearch.partialMatch(searchField.getText());
        else if (anagram.isSelected())
            matchingRestaurants = restaurantSearch.anagramMatch(searchField.getText());
        else
            matchingRestaurants = restaurantSearch.crossWordMatch(searchField.getText());
        
        String[] matchingNames = new String[matchingRestaurants.size()];
        for (int i = 0; i < matchingRestaurants.size(); ++i)
            matchingNames[i] = matchingRestaurants.get(i).getName();
        resultList.setListData(matchingNames);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anagram;
    private javax.swing.JRadioButton crossword;
    private javax.swing.JTextArea itemDetails;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton partialMatch;
    private javax.swing.JList<String> resultList;
    private javax.swing.JTextField searchField;
    private javax.swing.ButtonGroup searchMode;
    // End of variables declaration//GEN-END:variables

}
