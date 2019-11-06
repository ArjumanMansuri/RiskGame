package com.riskGame.observer;

import java.util.ArrayList;

/**
 * This is a StartupPhase class for Reinforcement phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */
public class StartupPhaseObserver extends PhaseViewObserver{

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
			System.out.println(action);
			this.actions.add(action);
		}	
		else
			this.actions.clear();
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
