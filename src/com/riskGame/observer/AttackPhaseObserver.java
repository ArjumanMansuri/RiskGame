package com.riskGame.observer;


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
		this.gamePhaseName = "ATTACK PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		if(! (action == null))
			this.actions.add(action);
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
