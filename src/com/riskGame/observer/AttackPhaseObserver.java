package com.riskGame.observer;

import java.util.ArrayList;

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
			StartupPhaseObserver.startupViewData = action;
			StartupPhaseObserver.view.display();
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
