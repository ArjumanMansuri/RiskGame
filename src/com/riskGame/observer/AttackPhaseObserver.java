package com.riskGame.observer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
/**
 * This is a Observable class for Attack phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */


public class AttackPhaseObserver extends PhaseViewObserver{

	/**
	 * Constructor for AttackPhaseObserver sets the gamePhaseName.
	 */
	public AttackPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "ATTACK PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		//String attackPhaseData = action;
		
		if(! (action == null))
		{
			StartupPhaseObserver.textArea.append(action);
			StartupPhaseObserver.textArea.append("\n-------------------------\n");
			StartupPhaseObserver.textArea.setLineWrap(true);
			StartupPhaseObserver.textArea.setWrapStyleWord(true);
			StartupPhaseObserver.textArea.setEditable(false);
			
			StartupPhaseObserver.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			StartupPhaseObserver.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			 
			StartupPhaseObserver.frame.setContentPane(StartupPhaseObserver.scroll);
			StartupPhaseObserver.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			StartupPhaseObserver.frame.setResizable(false);
			StartupPhaseObserver.frame.pack();
			StartupPhaseObserver.frame.setVisible(true);
		}	
		else
			StartupPhaseObserver.textArea.setText("");
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
