/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import IO.FrameStream;
import IO.config.Config;
import IO.debug.StandardOut;
import Shapes.Template;
import graphics.Filter;
import graphics.FrameProcessor;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import remote.controller.RemoteController;
import tracking.Tracking;

/**
 *
 * @author jeremi
 */
public class GUI extends javax.swing.JFrame {

    public static final String CONFIG_FILE_PATH = "mainConfig.txt";
    public static final String PATH_PREFIX = "Path : ";
    private String path = "C:\\Users\\eloi\\Documents\\ArnaudDossiers\\Prog";
    private RemoteController remote;
    
    
    /**
     * Creates new form GUI
     */
    public GUI() {
        loadConfig();
        setOutputStreams();
        remote = new RemoteController();
        initComponents();
        pathDisplay.setText(PATH_PREFIX + path);
        this.setLocationRelativeTo(null);
        remote.setVisible(true);
        this.addWindowListener(remote);
        display.passRemote(remote);
    }
    
    /**
     * Load the configuration file, and sets the variables to their correct values, then closes it.
     */
    private void loadConfig(){
        Config conf = new Config(CONFIG_FILE_PATH);
        try {
            conf.load();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "The config file could not be loaded. You may want to check the permissions/access you vave to the application's location.", "Config File load error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        path = conf.getStringParam("path", path);
        conf.write();
    }
    
    /**
     * Changes the default system.out stream to a newer and cooler one
     */
    private void setOutputStreams(){
        StandardOut out = new StandardOut(System.out);
        System.setOut(out);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        goButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        display = new UI.Display();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        chooseButtonMainPath = new javax.swing.JButton();
        pathDisplay = new javax.swing.JLabel();
        chooseButtonTemplatePath = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        matchPercentCheckBox = new javax.swing.JCheckBox();
        paintTrueImgCheckBox = new javax.swing.JCheckBox();
        emphShapesCheckBox = new javax.swing.JCheckBox();

        jCheckBox1.setText("jCheckBox1");

        jCheckBox2.setText("jCheckBox2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        goButton.setText("GO!");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout displayLayout = new javax.swing.GroupLayout(display);
        display.setLayout(displayLayout);
        displayLayout.setHorizontalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );
        displayLayout.setVerticalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(display);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        chooseButtonMainPath.setText("Choose Path");
        chooseButtonMainPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonMainPathActionPerformed(evt);
            }
        });

        pathDisplay.setText("Image Path :");

        chooseButtonTemplatePath.setText("Choose Path");
        chooseButtonTemplatePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonTemplatePathActionPerformed(evt);
            }
        });

        jLabel1.setText("Template path :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chooseButtonTemplatePath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chooseButtonMainPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pathDisplay)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseButtonMainPath)
                    .addComponent(pathDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseButtonTemplatePath)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        jButton1.setText("example send to rasPi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        matchPercentCheckBox.setText("Show match percent");
        matchPercentCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matchPercentCheckBoxActionPerformed(evt);
            }
        });

        paintTrueImgCheckBox.setText("Paint original frame");
        paintTrueImgCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintTrueImgCheckBoxActionPerformed(evt);
            }
        });

        emphShapesCheckBox.setText("Emphacise shapes");
        emphShapesCheckBox.setEnabled(false);
        emphShapesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emphShapesCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(matchPercentCheckBox)
                    .addComponent(jButton1)
                    .addComponent(paintTrueImgCheckBox)
                    .addComponent(emphShapesCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(matchPercentCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paintTrueImgCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emphShapesCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(goButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(goButton)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        try {
            FrameStream fs;
            if(remote.isConnected()){
                fs = new FrameStream(remote);// <-- le serveur
            }else{
                int i = JOptionPane.showOptionDialog(this, "The Server is not connected. Do you wish to loat the files from a local repository?", "Server not connected", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,null,null,null);
                if(i == JOptionPane.YES_OPTION){
                    fs = new FrameStream(path);// <-- le path
                }else{
                    return;
                }
            }
            Filter f = new Filter(Color.red,20);
            FrameProcessor fp = new FrameProcessor(f,display,null);
            fs.setOutput(fp);
            fs.start();
            Template temp = new Template(ImageIO.read(new File("C:\\Users\\jérémi\\Desktop\\template.png")));
            
            display.start(new Tracking(temp));
        } catch (IOException ex) {
            System.out.println("OOOOPs");
        }
    }//GEN-LAST:event_goButtonActionPerformed

    private void chooseButtonMainPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonMainPathActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            path = 
                chooser.getSelectedFile().getPath();
            pathDisplay.setText(PATH_PREFIX+path);
        }
    }//GEN-LAST:event_chooseButtonMainPathActionPerformed

    private void paintTrueImgCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintTrueImgCheckBoxActionPerformed
        display.setPaintOriginal(paintTrueImgCheckBox.isSelected());
        emphShapesCheckBox.setEnabled(paintTrueImgCheckBox.isSelected());
        display.refresh();
    }//GEN-LAST:event_paintTrueImgCheckBoxActionPerformed

    private void emphShapesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emphShapesCheckBoxActionPerformed
        display.setEmphasis(emphShapesCheckBox.isSelected());
        display.refresh();
    }//GEN-LAST:event_emphShapesCheckBoxActionPerformed

    private void matchPercentCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matchPercentCheckBoxActionPerformed
        display.setMatchStringVisible(matchPercentCheckBox.isSelected());
        display.refresh();
    }//GEN-LAST:event_matchPercentCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        remote.send("Greetings.");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void chooseButtonTemplatePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonTemplatePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chooseButtonTemplatePathActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the System look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* sets the system look and fee;
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        Tracking.init(args);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
                
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseButtonMainPath;
    private javax.swing.JButton chooseButtonTemplatePath;
    private UI.Display display;
    private javax.swing.JCheckBox emphShapesCheckBox;
    private javax.swing.JButton goButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JCheckBox matchPercentCheckBox;
    private javax.swing.JCheckBox paintTrueImgCheckBox;
    private javax.swing.JLabel pathDisplay;
    // End of variables declaration//GEN-END:variables
}
