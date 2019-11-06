package com.riskGame.observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * This is a PlayerDominationViewObserver class to get notified and display the domination data.
 * @author GouthamG
 *
 */
public  class PlayerDominationViewObserver {
	
		JFrame frame = new JFrame();
		final JTextArea textArea = new JTextArea(40,40);
		JScrollPane scroll = new JScrollPane(textArea);
	 /**
	 *String domniationViewData to display the domination details.
	 */
	 public String dominationViewData; 
	 
	 /**
	 *UpdateDomination method for updating the log message.
	 */
	 public void updateDomination(String action) {
		 dominationViewData = action;
		 

		 textArea.append(dominationViewData);
		 textArea.append("\n-------------------------\n");
		 textArea.setLineWrap(true);
		 textArea.setWrapStyleWord(true);
		 textArea.setEditable(false);
		 
		 scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		 scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		 
		 frame.setContentPane(scroll);
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		 frame.setResizable(false);
		 frame.pack();
	     frame.setVisible(true);
		}
}
