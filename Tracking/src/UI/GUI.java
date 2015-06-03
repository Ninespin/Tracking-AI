/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import IO.FrameStream;
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

    public static final String PATH_PREFIX = "Path :";
    private String path = "C:\\Users\\eloi\\Documents\\ArnaudDossiers\\Prog";
    private RemoteController remote;
    
    
    /**
     * Creates new form GUI
     */
    public GUI() {
        remote = new RemoteController();
        initComponents();
        pathDisplay.setText(PATH_PREFIX + path);
        this.setLocationRelativeTo(null);
        remote.setVisible(true);
        this.addWindowListener(remote);
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
        pathDisplay = new javax.swing.JLabel();
        chooseButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        display = new UI.Display();
        paintTrueImgCheckBox = new javax.swing.JCheckBox();
        emphShapesCheckBox = new javax.swing.JCheckBox();
        matchPercentCheckBox = new javax.swing.JCheckBox();

        jCheckBox1.setText("jCheckBox1");

        jCheckBox2.setText("jCheckBox2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        goButton.setText("GO!");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        pathDisplay.setText("Path :");

        chooseButton.setText("Chose Path");
        chooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout displayLayout = new javax.swing.GroupLayout(display);
        display.setLayout(displayLayout);
        displayLayout.setHorizontalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );
        displayLayout.setVerticalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(display);

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

        matchPercentCheckBox.setText("Show match percent");
        matchPercentCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matchPercentCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(goButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chooseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pathDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(matchPercentCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(paintTrueImgCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emphShapesCheckBox))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(goButton)
                        .addComponent(pathDisplay)
                        .addComponent(chooseButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(paintTrueImgCheckBox)
                        .addComponent(emphShapesCheckBox)
                        .addComponent(matchPercentCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
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

    private void chooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            path = 
                chooser.getSelectedFile().getPath();
            pathDisplay.setText(PATH_PREFIX+path);
        }
    }//GEN-LAST:event_chooseButtonActionPerformed

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
    private javax.swing.JButton chooseButton;
    private UI.Display display;
    private javax.swing.JCheckBox emphShapesCheckBox;
    private javax.swing.JButton goButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox matchPercentCheckBox;
    private javax.swing.JCheckBox paintTrueImgCheckBox;
    private javax.swing.JLabel pathDisplay;
    // End of variables declaration//GEN-END:variables
}
