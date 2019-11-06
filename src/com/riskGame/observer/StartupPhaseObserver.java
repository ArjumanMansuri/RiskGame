package com.riskGame.observer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * This is a StartupPhase class for Reinforcement phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */
public class StartupPhaseObserver extends PhaseViewObserver{

	public static JFrame frame = new JFrame();
	public static JTextArea textArea = new JTextArea(30,40);
	public static JScrollPane scroll = new JScrollPane(textArea);
	
	/**
	 * Constructor for StartupPhaseObserver sets the gamePhaseName.
	 */
	public StartupPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "STARTUP PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		if(! (action == null))
		{
			textArea.append(action);
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
		else
			textArea.setText("");
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
