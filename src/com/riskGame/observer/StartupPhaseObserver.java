package com.riskGame.observer;

import java.util.ArrayList;

public class StartupPhaseObserver extends PhaseViewObserver{

	 
	
	public StartupPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "STARTUP PHASE";
	}

	@Override
	public void update(String action) {
		this.actions.add(action);
		System.out.println(action);
		
	}

	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
