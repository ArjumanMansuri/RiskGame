package com.riskGame.observer;

import java.util.ArrayList;

import com.riskGame.view.GameView;

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
	static public String startupViewData;
	static public String dominationViewData;
	static public GameView view = new GameView();

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		if(! (action == null)) {
			if(action.contains("Domination")) {
				dominationViewData = action;
			}
			else {
				startupViewData = action;
			}
			view.display();	
			dominationViewData="";
		}
		
		else {
			startupViewData = "";
			dominationViewData="";
		}
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
