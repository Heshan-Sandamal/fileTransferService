/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.fileTransfer.ui;



import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.models.Node;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Heshan Sandamal
 */
public class FileSearchInterface extends JFrame {

    private DefaultTableModel dtmForSearchResultTable;
    private DefaultTableModel dtmForSelfFileTable;
    private DefaultTableModel dtmForPeerTable;
    private DefaultTableModel dtmForstatTable;


    public FileSearchInterface(GUIController guiController, ArrayList<String> fileList) {
        initComponents();
        this.selfFilesTable.getTableHeader().setVisible(false);
        this.userNameTextField.setText(ApplicationConstants.USER_NAME);
        this.ipTextField.setText(ApplicationConstants.IP);
        this.portTextField.setText(String.valueOf(ApplicationConstants.PORT));
        this.unregisterButton.setEnabled(false);
        this.searchTextField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dtmForSearchResultTable.setRowCount(0);
                guiController.searchFile(searchTextField.getText());

            }
        });

        this.searchButton.addActionListener(evt -> {
            dtmForSearchResultTable.setRowCount(0);
            guiController.searchFile(this.searchTextField.getText());
        });

        this.registerButton.addActionListener(evt -> {
            try {
                guiController.registerInBS();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.unregisterButton.addActionListener(evt -> {
            guiController.unRegister();
        });

        dtmForSearchResultTable = (DefaultTableModel) searchResultsTable.getModel();
        dtmForSearchResultTable.setRowCount(0);

        dtmForSelfFileTable = (DefaultTableModel) selfFilesTable.getModel();

        dtmForPeerTable = (DefaultTableModel) peerTable.getModel();
        dtmForPeerTable.setRowCount(0);

        dtmForstatTable = (DefaultTableModel) statTable.getModel();
        dtmForstatTable.setRowCount(0);

        for (int x = 0; x < fileList.size(); x++) {
            dtmForSelfFileTable.setValueAt(fileList.get(x).replace("@", " ").toString(), 0, x);
        }
    }

    public synchronized void addToTable(String nodeIp, int port, int fileCount, HashSet<String> fileList, int ttl) {
        System.out.println("Calling interface " + nodeIp + port);
        StringBuilder fileNames = new StringBuilder();
        for (String fileName : fileList) {
            fileNames.append(fileName.replace("@", " ")).append(" , ");
        }
        fileNames.delete(fileNames.length()-2,fileNames.length()-1);
        int noOfHops = ApplicationConstants.HOPS - ttl;
        if (!this.isValueExistsAtTable(nodeIp, port)) {
            this.dtmForSearchResultTable.addRow(new Object[]{nodeIp, port, fileCount, fileNames.toString(), noOfHops});
        }
    }

    private boolean isValueExistsAtTable(String ip, int port) {
        int rowCount = this.dtmForSearchResultTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String nodeIp = String.valueOf(dtmForSearchResultTable.getValueAt(i, 0));
            int nodePort = Integer.parseInt(dtmForSearchResultTable.getValueAt(i, 1).toString());

            if (nodeIp.equals(ip) && nodePort == port) {
                return true;
            }
        }
        return false;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public String showInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message);
    }
    // End of variables declaration//GEN-END:variables


    public void populatePeerTable(Set<Node> peerList) {
        dtmForPeerTable.setRowCount(0);
        final Iterator<Node> iterator = peerList.iterator();
        while(iterator.hasNext()) {
            Node node = iterator.next();
            dtmForPeerTable.addRow(new Object[]{node.getNodeIp(), node.getPort()});
        }
    }

    public void populateStatTable(ConcurrentHashMap<String, ConcurrentLinkedQueue<Node>> statTable){
        dtmForstatTable.setRowCount(0);
        Enumeration<String> keys = statTable.keys();
        while(keys.hasMoreElements()){
            String fileName = keys.nextElement();
            final ConcurrentLinkedQueue<Node> concurrentLinkedQueue = statTable.get(fileName);
            String nodesList="";
            for (Node node:concurrentLinkedQueue) {
                nodesList += node.getNodeIp()+ ":" +node.getPort() + " , ";
            }
            dtmForstatTable.addRow(new Object[]{fileName.replace("@", " "),nodesList});
        }

    }
    public void handleRegistration(){
        registerButton.setEnabled(false);
        unregisterButton.setEnabled(true);
    }
    public void handleUnRegistration(){
        registerButton.setEnabled(true);
        unregisterButton.setEnabled(false);
    }


    public FileSearchInterface() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new JLabel();
        jPanel2 = new JPanel();
        jLabel5 = new JLabel();
        portTextField = new JTextField();
        userNameTextField = new JTextField();
        jLabel3 = new JLabel();
        ipTextField = new JTextField();
        jLabel4 = new JLabel();
        jScrollPane2 = new JScrollPane();
        selfFilesTable = new JTable();
        jPanel3 = new JPanel();
        searchButton = new JButton();
        searchTextField = new JTextField();
        jLabel1 = new JLabel();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        searchResultsTable = new JTable();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jScrollPane3 = new JScrollPane();
        peerTable = new JTable();
        jPanel6 = new JPanel();
        jScrollPane5 = new JScrollPane();
        statTable = new JTable();
        unregisterButton = new JButton();
        registerButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Distributed File Search");
        setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Distributed File Search");

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Port");

        portTextField.setEditable(false);
        portTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                portTextFieldActionPerformed(evt);
            }
        });

        userNameTextField.setEditable(false);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("UserName");

        ipTextField.setEditable(false);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("IP");

        selfFilesTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
                }
        ));
        selfFilesTable.setAutoscrolls(false);
        jScrollPane2.setViewportView(selfFilesTable);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        searchButton.setBackground(new java.awt.Color(51, 153, 0));
        searchButton.setText("Search");

        searchTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Search Query:");

        jPanel1.setBorder(BorderFactory.createTitledBorder("Search Results"));

        searchResultsTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{
                        "Node Ip", "Port", "File Count", "Files", "HOPS"
                }
        ));
        jScrollPane1.setViewportView(searchResultsTable);
        if (searchResultsTable.getColumnModel().getColumnCount() > 0) {
            searchResultsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            searchResultsTable.getColumnModel().getColumn(0).setMaxWidth(400);
            searchResultsTable.getColumnModel().getColumn(1).setMaxWidth(100);
            searchResultsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            searchResultsTable.getColumnModel().getColumn(2).setMaxWidth(100);
            searchResultsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            searchResultsTable.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBorder(BorderFactory.createTitledBorder("Peer Table"));

        peerTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String[]{
                        "Node IP", "Port"
                }
        ));
        jScrollPane3.setViewportView(peerTable);
        if (peerTable.getColumnModel().getColumnCount() > 0) {
            peerTable.getColumnModel().getColumn(0).setPreferredWidth(130);
            peerTable.getColumnModel().getColumn(0).setMaxWidth(130);
        }

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(BorderFactory.createTitledBorder("Stat Table"));

        statTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String[]{
                        "File", "Nodes"
                }
        ));
        jScrollPane5.setViewportView(statTable);
        if (statTable.getColumnModel().getColumnCount() > 0) {
            statTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            statTable.getColumnModel().getColumn(0).setMaxWidth(300);
        }

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane5)
                                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(searchTextField)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane2)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(userNameTextField, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
                                                .addGap(45, 45, 45)
                                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ipTextField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                                .addGap(48, 48, 48)
                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                        .addComponent(jPanel3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(userNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(ipTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(189, 189, 189))
        );

        unregisterButton.setBackground(new java.awt.Color(204, 0, 0));
        unregisterButton.setText("Unregister");

        registerButton.setBackground(new java.awt.Color(0, 153, 0));
        registerButton.setText("Register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(registerButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(unregisterButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(registerButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(unregisterButton))
                                                .addGap(15, 15, 15))
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 694, GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void portTextFieldActionPerformed(ActionEvent evt) {//GEN-FIRST:event_portTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_portTextFieldActionPerformed

    private void registerButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registerButtonActionPerformed

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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileSearchInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileSearchInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileSearchInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileSearchInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileSearchInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField ipTextField;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane5;
    private JTable peerTable;
    private JTextField portTextField;
    private JButton registerButton;
    private JButton searchButton;
    private JTable searchResultsTable;
    private JTextField searchTextField;
    private JTable selfFilesTable;
    private JTable statTable;
    private JButton unregisterButton;
    private JTextField userNameTextField;
    // End of variables declaration//GEN-END:variables
}
