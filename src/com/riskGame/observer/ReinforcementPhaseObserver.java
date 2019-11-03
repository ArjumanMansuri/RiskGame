package com.riskGame.observer;

public class ReinforcementPhaseObserver extends PhaseViewObserver{

	public ReinforcementPhaseObserver() {
		super();
		this.gamePhaseName = "REINFORCEMENT PHASE";
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
