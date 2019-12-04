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
	
	JPanel mainPanel  = new JPanel(new GridLayout(1,3));
	JTextArea dominationText = new JTextArea(30,40);
	JTextArea startupText = new JTextArea(30,40);
	JTextArea cardsText = new JTextArea(30,40);
	JPanel dominationPanel = new JPanel();
	JPanel startupPanel = new JPanel();
	JPanel cardsPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane scrollPane2 = new JScrollPane();
	JScrollPane scrollPane3 = new JScrollPane();
	JFrame frame = new JFrame();
	
	public void display() {
		String dominationData =domination.dominationViewData;
		if(dominationData!=null && (!dominationData.isEmpty())) {
			dominationText.append(dominationData);
			dominationText.setLineWrap(true);
			dominationText.setWrapStyleWord(true);
			dominationText.setEditable(false);
		}
		String startupData = StartupPhaseObserver.startupViewData;
		if(startupData!=null && (!startupData.isEmpty())) {
			startupText.append(startupData);
			startupText.setLineWrap(true);
			startupText.setWrapStyleWord(true);
			startupText.setEditable(false);
		}
		String cardData = StartupPhaseObserver.startupViewData;
		if(cardData!=null && (!cardData.isEmpty())) {
			cardsText.append(cardData);
			cardsText.setLineWrap(true);
			cardsText.setWrapStyleWord(true);
			cardsText.setEditable(false);
		}
		dominationPanel.add(dominationText);
		startupPanel.add(startupText);
		cardsPanel.add(cardsText);
		scrollPane.setViewportView(dominationPanel);
		scrollPane2.setViewportView(startupPanel);
		scrollPane3.add(cardsPanel);
		mainPanel.add(scrollPane);
		mainPanel.add(scrollPane2);
		mainPanel.add(scrollPane3);
		
		frame.setContentPane(mainPanel);
		frame.setVisible(true);
		frame.setSize(1300, 1300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
	}	
}
