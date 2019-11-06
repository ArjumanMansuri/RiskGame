package com.riskGame.observer;

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
		this.gamePhaseName = "STARTUP PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		this.actions.add(action);
		System.out.println(action);
		
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
