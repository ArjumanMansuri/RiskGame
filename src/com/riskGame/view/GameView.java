package com.riskGame.view;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.riskGame.observer.*;

public class GameView {

	static PlayerDominationViewObserver domination = new PlayerDominationViewObserver();
	static StartupPhaseObserver startup = new StartupPhaseObserver();
	
	JPanel mainPanel  = new JPanel(new GridLayout(1,2));
	JTextArea textArea = new JTextArea(30,40);
	JTextArea textArea2 = new JTextArea(30,40);
	JPanel jpanelNorth = new JPanel();
	JPanel jpanelSouth = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane scrollPane2 = new JScrollPane();
	JFrame frame = new JFrame();
	
	public void display() {
		String dominationData =domination.dominationViewData;
		if(dominationData!=null && (!dominationData.isEmpty())) {
		textArea.append(dominationData);
		textArea.append("\n-------------------------\n");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		}
		String startupData = startup.startupViewData;
		if(startupData!=null && (!startupData.isEmpty())) {
		textArea2.append(startupData);
		textArea2.append("\n-------------------------\n");
		textArea2.setLineWrap(true);
		textArea2.setWrapStyleWord(true);
		textArea2.setEditable(false);
		jpanelNorth.add(textArea);
		jpanelSouth.add(textArea2);
		}
		scrollPane.setViewportView(jpanelNorth);
		scrollPane2.setViewportView(jpanelSouth);
		mainPanel.add(scrollPane);
		mainPanel.add(scrollPane2);
		
		frame.setContentPane(mainPanel);
		frame.setVisible(true);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
	}	
}
