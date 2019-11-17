package com.riskGame.observer;

/**
 * This is a Observable class for Fortification phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */
import java.util.ArrayList;

public class FortificationPhaseObserver extends PhaseViewObserver{
	
	/**
	 * Constructor for FortificationPhaseObserver sets the gamePhaseName.
	 */
	public FortificationPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "FORTIFICATION PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		if(! (action == null))
		{
			StartupPhaseObserver.startupViewData = action;
		}	
		else
			StartupPhaseObserver.startupViewData = "";
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
