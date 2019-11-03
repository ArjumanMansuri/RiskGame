package com.riskGame.observer;

public class StartupPhaseObserver extends PhaseViewObserver{

	 
	
	public StartupPhaseObserver() {
		super();
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
