package edu.umass.ckc.wo.servertest;

import edu.umass.ckc.wo.smgr.User;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.util.List;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Apr 4, 2006
 * Time: 10:37:55 AM
 */
public class ServerTestGui extends JFrame implements ActionListener {
    private JButton runBut;
    private JButton delTestDataBut;
    private JLabel statusLabel;
    private JList baselineList;
    private JTextPane testOutputPane;
    private JTextPane baselineOutputPane;
    private JPanel mainPanel;
    private ServerTestMgr mgr;
    private JComboBox groupPulldown;
    private JLabel groupLab;
    private int group=100;

    public ServerTestGui () {
        this.getContentPane().add(mainPanel);
        try {
            this.init();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void init () throws SQLException {
        mgr = new ServerTestMgr();
        mgr.init();
        guiInit();
    }

    private void guiInit () throws SQLException {
        List baselineUsers = mgr.getBaseLineUsers();
        DefaultListModel lm = new DefaultListModel();
        for (int i = 0; i < baselineUsers.size(); i++) {
            Object o = baselineUsers.get(i);
            lm.addElement(o);
        }
        baselineList.setModel(lm);
        runBut.addActionListener(this);
        delTestDataBut.addActionListener(this);
        DefaultComboBoxModel cm = new DefaultComboBoxModel();
        cm.addElement("100 -  RandomProblemSelector)");
        cm.addElement("200 - RandomProblemSelector + Bayesian Interventions ");
        cm.addElement("300 - Adaptive+ChunkingProblemSelector ");
        cm.addElement("400 - MLProblemSelector ");
        groupPulldown.setModel(cm);
        groupPulldown.addActionListener(this);
        this.addWindowListener(new WindowAdapter () {
            public void windowClosing (WindowEvent e) {
                System.exit(0);
            }
        });
    }

      public void display() {
        this.pack();
        this.setSize(800, 800);
        this.setLocation(0, 0);
        this.setVisible(true);
    }


    private static void createAndShowGUI() {
        try {
//            JFrame.setDefaultLookAndFeelDecorated(true);

            ServerTestGui g = new ServerTestGui();
            g.display();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



    private void displayResults (String baselineResult, String regressionResult) {
        baselineOutputPane.setText(baselineResult);
        testOutputPane.setText(regressionResult);
    }

    public void runTest () {
        statusLabel.setText("Running test.  Please wait...");
        final Object o = baselineList.getSelectedValue();
        if (o != null) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    User u =(User) o;
                    try {
                        mgr.runTest(u, ServerTestGui.this.group);
                        String blout = mgr.getBaselineOutput();
                        String regout = mgr.getRegressionTestOutput();
                        displayResults(blout,regout);
                        statusLabel.setText("");

                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    statusLabel.setText("Exception occurred during testing.");
                }
                }
        });

        }
        else statusLabel.setText("You must select a baseline test.");
    }

    private void removeTestData () {
        try {
            mgr.removeTestUser();
            displayResults("","");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void setGroup () {
        int ix = groupPulldown.getSelectedIndex();
        if (ix==0)
            group = 100;
        else if (ix==1)
            group=200;
        else if (ix==2)
            group=300;
        else if (ix==3)
            group=400;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runBut)
            runTest();
        else if (e.getSource() == delTestDataBut)
            removeTestData();
        else if (e.getSource() == groupPulldown)
            setGroup();
    }



    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
