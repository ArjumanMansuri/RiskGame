package view;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FileDialog;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class GameLaunch extends java.awt.Frame{
	private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private FileDialog fileSelectDialog;
 
    public GameLaunch() {
        super("RISK Game");
        initMenuComponents();
        jButton1.setEnabled(true);
        setLocationRelativeTo(null);
    }
   
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameLaunch().setVisible(true);
            }
        });
    }
    
    private void initMenuComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(1, 1, 1));
        addWindowListener(new java.awt.event.WindowAdapter() {

            //Method to terminate and close the program window.
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        jPanel1.setBackground(new java.awt.Color(1, 1, 1));
        jPanel1.setName("jPanel1");

        //On click the button starts game in single mode.
        jButton1.setText("Play Game");
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
        
            }
        });
        
      //On click the button starts game in riskModels.tournament mode.
        jButton2.setText("Select Map");
        jButton2.setName("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               
            }
        });

        //On click the button terminates the game.
        jButton3.setText("Exit");
        jButton3.setName("jButton6");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
      
        jLabel1.setName("jLabel1");
        //set the layout of the panel with all the components added
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        //set the horizontal group for the panel layout
        jPanel1Layout.setHorizontalGroup
                (
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel1)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                        .addGap(55, 55, 55))
                ));
        //set the vertical group for the panel layout
        jPanel1Layout.setVerticalGroup
                (
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel1)
                                        .addGap(15, 15, 15)
                                        .addComponent(jButton1)
                                        .addGap(12, 12, 12)
                                        .addComponent(jButton2)
                                        .addGap(15, 15, 15)
                                        .addComponent(jButton3)
                                        .addGap(18, 18, 18)
                ));
        add(jPanel1, java.awt.BorderLayout.CENTER);
        pack();
    }

}
