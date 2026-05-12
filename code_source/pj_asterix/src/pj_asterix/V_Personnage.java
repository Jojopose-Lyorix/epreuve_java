/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pj_asterix;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pergaixj
 */
public class V_Personnage extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(V_Personnage.class.getName());
    
    private LinkedHashMap<Integer, M_personnages> lesPersonnages;
    private LinkedHashMap<Integer, M_peuples> lesPeuples;
    private M_personnages unPersonnage;
    private M_peuples unPeuple;
    private DefaultTableModel dm_tb_Personnages;
    private Integer ligne, iPerso;
    private C_asterix controler;
     private int idRole;
    
    private void activation(boolean actif) {
        tx_id.setEditable(false);
        tx_nom.setEditable(actif);
        tx_com.setEditable(actif);
        rd_homme.setEnabled(actif);
        rd_femme.setEnabled(actif);
        cb_peuple.setEnabled(actif);

    }
    private void sePlacer() {
        iPerso = table_personnage.getSelectedRow();

        int id = (Integer) table_personnage.getValueAt(iPerso, 0);

        unPersonnage = lesPersonnages.get(id);
        tx_id.setText(String.valueOf(unPersonnage.getId()));
        tx_nom.setText(unPersonnage.getNom());
        tx_com.setText(unPersonnage.getCommentaire());
        if (unPersonnage.getGenreCode().equals("F")) {
    rd_femme.setSelected(true);
} else {
    rd_homme.setSelected(true);
}
        peupleCB(unPersonnage.getPeupleId());



    }
    
    
    /**
     * Creates new form V_Personnage
     */
    public V_Personnage(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.idRole = 1;
    }
    
    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
    
   public void peupleCB(Integer peupleId) {
    if (peupleId == null) {
        cb_peuple.setSelectedIndex(-1);
        return;
    }

    M_peuples p = lesPeuples.get(peupleId);
    if (p != null) {
        cb_peuple.setSelectedItem(p.getNom());
        return;
    }

    cb_peuple.setSelectedIndex(-1);
}

private Integer getPeupleIdFromCombo() {
    String nom = (String) cb_peuple.getSelectedItem();
    if (nom == null) return null;

    for (Integer id : lesPeuples.keySet()) {
        M_peuples p = lesPeuples.get(id);
        if (p != null && p.getNom().equals(nom)) {
            return id;
        }
    }
    return null;
}




    
    
    public void clearCombo(){
        cb_peuple.removeAllItems();
        for (int uneCle : lesPeuples.keySet()) {
            unPeuple = lesPeuples.get(uneCle);
            cb_peuple.addItem(unPeuple.getNom());
        }
    }
     public void clear() {
        tx_id.setText("");
        tx_nom.setText("");
        tx_com.setText("");
        grp_genre.clearSelection(); // ← CORRIGÉ
        cb_peuple.setSelectedIndex(-1);
    }
    
    public void afficher(C_asterix gestionPerso, LinkedHashMap<Integer, M_personnages> lesPersonnages, LinkedHashMap<Integer, M_peuples> lesPeuples) {
        this.lesPersonnages = lesPersonnages;
        this.lesPeuples = lesPeuples;
        this.controler = gestionPerso;

        dm_tb_Personnages = (DefaultTableModel) table_personnage.getModel();
        dm_tb_Personnages.setRowCount(lesPersonnages.size());
        ligne = 0;

        
        for (Integer uneCle : lesPersonnages.keySet()) {
            unPersonnage = lesPersonnages.get(uneCle);
            table_personnage.setValueAt(unPersonnage.getId(), ligne, 0);
            table_personnage.setValueAt(unPersonnage.getNom(), ligne, 1);
           
            ligne++;
            
        }
        
        clearCombo();

        this.setSize(560, 400);
        this.setLocationRelativeTo(null);

        bt_detail.setVisible(true);
        jpan_1.setVisible(true);
        this.ihm('T');
        
        
        setVisible(true);
    }
    private void ihm(char mode) {
        if (mode == 'T') {
            jpan_1.setVisible(true);
            bt_detail.setVisible(true);
             if (idRole == 3) {
                bt_modif.setVisible(false);
                bt_supp.setVisible(false);
                bt_ajout.setVisible(true);
            } else {
                bt_modif.setVisible(true);
                bt_supp.setVisible(true);
                bt_ajout.setVisible(true);
            }
        } else {
            switch (mode) {
                case 'C' -> {
                    lb_titre.setText("Consultationner");
                    this.activation(false);
                    jpan_1.setVisible(false);
                    this.setSize(560, 400);
                    sePlacer();
                    bt_detail.setVisible(true);
                    bt_retour.setVisible(true);
                    bt_enre.setVisible(false);
                    bt_modif.setVisible(false);
                    bt_annuler.setVisible(false);
                }
                case 'A' -> {
                    lb_titre.setText("Ajoutationner");
                    this.activation(true);
                    jpan_1.setVisible(false);
                    this.setSize(560, 400);
                    bt_detail.setVisible(true);
                    bt_enre.setVisible(true);
                    bt_annuler.setVisible(true);
                    bt_retour.setVisible(false);
                    clear();
                }
                case 'M' -> {
                    if (idRole == 3) {
                        JOptionPane.showMessageDialog(this, 
                            "Vous n'avez pas les permissions pour modifier.", 
                            "Accès refusé", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    lb_titre.setText("Modificationner");
                    this.activation(true);
                    jpan_1.setVisible(false);
                    this.setSize(560, 400);
                    sePlacer();
                    bt_detail.setVisible(true);
                    bt_enre.setVisible(true);
                    bt_annuler.setVisible(true);
                    bt_retour.setVisible(false);
                }

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grp_genre = new javax.swing.ButtonGroup();
        jpan_1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_personnage = new javax.swing.JTable();
        bt_detail = new javax.swing.JButton();
        bt_modif = new javax.swing.JButton();
        bt_supp = new javax.swing.JButton();
        bt_ajout = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lb_titre = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        lb_nom = new javax.swing.JLabel();
        lb_com = new javax.swing.JLabel();
        lb_genre = new javax.swing.JLabel();
        lb_peuple = new javax.swing.JLabel();
        tx_id = new javax.swing.JTextField();
        tx_nom = new javax.swing.JTextField();
        tx_com = new javax.swing.JTextField();
        bt_enre = new javax.swing.JButton();
        bt_annuler = new javax.swing.JButton();
        bt_retour = new javax.swing.JButton();
        cb_peuple = new javax.swing.JComboBox<>();
        rd_femme = new javax.swing.JRadioButton();
        rd_homme = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        table_personnage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "id", "Personnage"
            }
        ));
        table_personnage.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                table_personnagePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(table_personnage);

        bt_detail.setText("Detail");
        bt_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_detailActionPerformed(evt);
            }
        });

        bt_modif.setText("Modifier");
        bt_modif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_modifActionPerformed(evt);
            }
        });

        bt_supp.setText("Supprimer");
        bt_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_suppActionPerformed(evt);
            }
        });

        bt_ajout.setText("Ajouter");
        bt_ajout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_ajoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpan_1Layout = new javax.swing.GroupLayout(jpan_1);
        jpan_1.setLayout(jpan_1Layout);
        jpan_1Layout.setHorizontalGroup(
            jpan_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpan_1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jpan_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_detail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_modif, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_ajout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jpan_1Layout.setVerticalGroup(
            jpan_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpan_1Layout.createSequentialGroup()
                .addGroup(jpan_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpan_1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpan_1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(bt_detail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_modif, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_ajout, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        lb_id.setText("id :");

        lb_nom.setText("nom :");

        lb_com.setText("commentaire :");

        lb_genre.setText("genre :");

        lb_peuple.setText("peuple :");

        bt_enre.setText("Enregistrer");
        bt_enre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_enreActionPerformed(evt);
            }
        });

        bt_annuler.setText("Annuler");
        bt_annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_annulerActionPerformed(evt);
            }
        });

        bt_retour.setText("Retour");
        bt_retour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_retourActionPerformed(evt);
            }
        });

        cb_peuple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_peupleActionPerformed(evt);
            }
        });

        grp_genre.add(rd_femme);
        rd_femme.setText("Femme");

        grp_genre.add(rd_homme);
        rd_homme.setText("Homme");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(lb_titre, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lb_com, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                                .addComponent(tx_com, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(lb_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tx_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(lb_id, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                .addComponent(tx_id, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(247, 247, 247))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lb_genre, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_homme)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_femme)
                        .addContainerGap(398, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lb_peuple, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_peuple, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(bt_enre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bt_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bt_retour, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lb_titre, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_id, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_id, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_nom, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_com, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_com, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_genre, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rd_homme)
                    .addComponent(rd_femme))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_peuple, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_peuple, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_enre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_retour, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpan_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jpan_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_personnagePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_personnagePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_table_personnagePropertyChange

    private void bt_detailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_detailActionPerformed
                                                  
       if (table_personnage.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "un personnage doit être sélectionné dans le tableau", "Sélection obligatoire", JOptionPane.ERROR_MESSAGE);
        } else {
            this.ihm('C');
        }

    
    }//GEN-LAST:event_bt_detailActionPerformed

    private void bt_retourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_retourActionPerformed
        this.setSize(560, 400);
        this.setLocationRelativeTo(null);
        
        clearCombo();
        

        bt_detail.setVisible(true);
        
        jpan_1.setVisible(true);
        this.ihm('T');
    }//GEN-LAST:event_bt_retourActionPerformed

    private void bt_modifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_modifActionPerformed
         if (idRole == 3) {
            JOptionPane.showMessageDialog(this, 
                "Vous n'avez pas les permissions pour modifier.", 
                "Accès refusé", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (table_personnage.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "un personnage doit être sélectionné dans le tableau", "Sélection obligatoire", JOptionPane.ERROR_MESSAGE);
        } else {
            this.ihm('M');
        }
    }//GEN-LAST:event_bt_modifActionPerformed

    private void bt_enreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_enreActionPerformed
     String vNom = tx_nom.getText().trim();
    String vCom = tx_com.getText().trim();

    if (vNom.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Le nom est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    char vGenre = rd_femme.isSelected() ? 'F' : 'M';

    Integer vPeupleId = getPeupleIdFromCombo(); // ✅ Integer
    if (vPeupleId == null) {
        JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un peuple !",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean isModif = "Modificationner".equals(lb_titre.getText()); // ✅ bonne comparaison

    if (isModif) {
        int vidPerso = Integer.parseInt(tx_id.getText());

        int reponse = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir modifier " + vNom + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (reponse == JOptionPane.YES_OPTION) {
            try {
                controler.enregistrer(vidPerso, vNom, vCom, vGenre, vPeupleId); // ✅ Integer OK
                afficher(controler, lesPersonnages, lesPeuples);
            } catch (IOException | SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la modification : " + ex.getMessage(),
                        "Erreur SQL/IO",
                        JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
            }
        }
    } else {
        int reponse = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir ajouter " + vNom + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (reponse == JOptionPane.YES_OPTION) {
            try {
                controler.ajouter(vNom, vCom, vGenre, vPeupleId); // ✅ Integer OK
                afficher(controler, lesPersonnages, lesPeuples);
            } catch (IOException | SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'ajout : " + ex.getMessage(),
                        "Erreur SQL/IO",
                        JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
    }//GEN-LAST:event_bt_enreActionPerformed

    private void bt_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_suppActionPerformed
         if (idRole == 3) {
            JOptionPane.showMessageDialog(this, 
                "Vous n'avez pas les permissions pour supprimer.", 
                "Accès refusé", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (table_personnage.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "un site doit être sélectionné dans le tableau", "Sélection obligatoire", JOptionPane.ERROR_MESSAGE);
        } else {
            iPerso = table_personnage.getSelectedRow();
            int id = (Integer) table_personnage.getValueAt(iPerso, 0);
            int reponse = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer le personnage ?",
                    "Attention !",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (reponse == JOptionPane.YES_OPTION) {
                try {
                    controler.supp_Personnage(id);
                } catch (SQLException ex) {
                    
                    JOptionPane.showMessageDialog(this, "Suppréssion impossible le site est utilisé.","", JOptionPane.ERROR_MESSAGE);
        
                }
            }
        }
    }//GEN-LAST:event_bt_suppActionPerformed

    private void bt_ajoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_ajoutActionPerformed
        this.ihm('A');
    }//GEN-LAST:event_bt_ajoutActionPerformed

    private void bt_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_annulerActionPerformed
        this.setSize(560, 400);
        this.setLocationRelativeTo(null);

        clearCombo();
        bt_detail.setVisible(true);
        jpan_1.setVisible(true);
    }//GEN-LAST:event_bt_annulerActionPerformed

    private void cb_peupleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_peupleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_peupleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                V_Personnage dialog = new V_Personnage(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_ajout;
    private javax.swing.JButton bt_annuler;
    private javax.swing.JButton bt_detail;
    private javax.swing.JButton bt_enre;
    private javax.swing.JButton bt_modif;
    private javax.swing.JButton bt_retour;
    private javax.swing.JButton bt_supp;
    private javax.swing.JComboBox<String> cb_peuple;
    private javax.swing.ButtonGroup grp_genre;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpan_1;
    private javax.swing.JLabel lb_com;
    private javax.swing.JLabel lb_genre;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_nom;
    private javax.swing.JLabel lb_peuple;
    private javax.swing.JLabel lb_titre;
    private javax.swing.JRadioButton rd_femme;
    private javax.swing.JRadioButton rd_homme;
    private javax.swing.JTable table_personnage;
    private javax.swing.JTextField tx_com;
    private javax.swing.JTextField tx_id;
    private javax.swing.JTextField tx_nom;
    // End of variables declaration//GEN-END:variables
}
