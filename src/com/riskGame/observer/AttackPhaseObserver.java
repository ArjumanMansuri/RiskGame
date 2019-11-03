package com.riskGame.observer;

public class AttackPhaseObserver extends PhaseViewObserver{

	public AttackPhaseObserver() {
		super();
		this.gamePhaseName = "ATTACK PHASE";
	}

	@Override
	public void update(String action) {
		if(! (action == null))
			this.actions.add(action);
		else
			this.actions.clear();
		
	}

	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
