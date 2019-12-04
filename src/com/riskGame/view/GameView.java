package com.riskGame.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.riskGame.observer.*;
/**
 * This class holds that method that helps to update the view Panels
 * @author Mudra-PC
 *
 */
public class GameView {

	static StartupPhaseObserver startup = new StartupPhaseObserver();

	JPanel mainPanel  = new JPanel(new GridLayout(1,3));
	JTextArea dominationText = new JTextArea(30,40);
	JTextArea startupText = new JTextArea(30,40);
	JTextArea cardsText = new JTextArea(30,40);
	JPanel dominationPanel = new JPanel();
	JPanel startupPanel = new JPanel();
	JPanel cardsPanel = new JPanel();
	JLabel dominationLabel = new JLabel("WORLD DOMINATION VIEW");
	JLabel startupLabel = new JLabel("GAME PLAY VIEW");
	JLabel cardsLabel = new JLabel("CARD EXCHANGE VIEW");
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane scrollPane2 = new JScrollPane();
	JScrollPane scrollPane3 = new JScrollPane();
	JFrame frame = new JFrame();
	GridBagConstraints gbc = new GridBagConstraints();
	
	/**
	 * This method creates ad updates the view for different panels
	 */
	public void display() {
		
		//World Domination View
		String dominationData =StartupPhaseObserver.dominationViewData;
		if(dominationData!=null && (!dominationData.isEmpty())) {
			dominationText.append(dominationData);
			dominationText.append("\n--------------------\n");
			dominationText.setLineWrap(true);
			dominationText.setWrapStyleWord(true);
			dominationText.setEditable(false);
		}
		dominationPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
	    gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        dominationPanel.add(dominationLabel,gbc);
        gbc.gridy++;
        dominationPanel.add(dominationText,gbc);
		gbc.gridy++;
		
		//Game Play View
		String startupData = StartupPhaseObserver.startupViewData;
		if(startupData!=null && (!startupData.isEmpty())) {
			startupText.append(startupData + "\n");
			startupText.setLineWrap(true);
			startupText.setWrapStyleWord(true);
			startupText.setEditable(false);
		}
		startupPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
	    gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        startupPanel.add(startupLabel,gbc);
        gbc.gridy++;
        startupPanel.add(startupText,gbc);
		gbc.gridy++;
		
		//Card Exchange View
		String cardData = CardExchange.cardViewData;
		if(cardData!=null && (!cardData.isEmpty())) {
			cardsText.append(cardData);
			cardsText.setLineWrap(true);
			cardsText.setWrapStyleWord(true);
			cardsText.setEditable(false);
		}
		cardsPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
	    gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        cardsPanel.add(cardsLabel,gbc);
        gbc.gridy++;
        cardsPanel.add(cardsText,gbc);
		gbc.gridy++;
		
		//setting scrollable panes
		scrollPane.setViewportView(dominationPanel);
		scrollPane2.setViewportView(startupPanel);
		scrollPane3.setViewportView(cardsPanel);
		
		//Adding all the scrollPanes to one main panel
		mainPanel.add(scrollPane);
		mainPanel.add(scrollPane2);
		mainPanel.add(scrollPane3);
		
		//Adding all the data to frame
		frame.setContentPane(mainPanel);
		frame.setVisible(true);
		frame.setSize(800, 1000);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
	}
}
