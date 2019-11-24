package com.riskGame.Strategies;

import com.riskGame.models.Player;

public class AggressivePlayer extends Player {

	public AggressivePlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new AggresiveAttack();
		fortifyType = new AggresiveFortify();
		reinforceType = new AggresiveReinforce();
	}

}
