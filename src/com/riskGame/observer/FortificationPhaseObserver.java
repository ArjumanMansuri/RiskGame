package com.riskGame.observer;

public class FortificationPhaseObserver extends PhaseViewObserver{

	public FortificationPhaseObserver() {
		super();
		this.gamePhaseName = "FORTIFICATION PHASE";
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
