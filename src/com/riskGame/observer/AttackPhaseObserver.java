package com.riskGame.observer;

import java.util.ArrayList;

public class AttackPhaseObserver extends PhaseViewObserver{

	public AttackPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
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
