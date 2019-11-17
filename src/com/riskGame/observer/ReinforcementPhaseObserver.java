package com.riskGame.observer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * This is a Observable class for Reinforcement phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */
public class ReinforcementPhaseObserver extends PhaseViewObserver{
	
	/**
	 * Constructor for ReinforcementPhaseObserver sets the gamePhaseName.
	 */
	public ReinforcementPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "REINFORCEMENT PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		if(! (action == null))
		{
			StartupPhaseObserver.startupViewData= action;
		}	
		else
			StartupPhaseObserver.startupViewData ="";
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
