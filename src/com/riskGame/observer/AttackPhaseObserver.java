package com.riskGame.observer;

public class AttackPhaseObserver extends PhaseViewObserver{

	@Override
	public void update(String action) {
		this.actions.add(action);
		
	}

	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
