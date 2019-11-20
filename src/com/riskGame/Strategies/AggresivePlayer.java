package com.riskGame.Strategies;

import com.riskGame.models.Player;

public class AggresivePlayer extends Player {

	public AggresivePlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new AggresiveAttack();
		fortifyType = new AggresiveFortify();
		reinforceType = new AggresiveReinforce();
	}

}
