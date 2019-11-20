package com.riskGame.Strategies;

import com.riskGame.models.Player;

public class BenevolentPlayer extends Player {

	public BenevolentPlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new BenevolentAttack();
		fortifyType = new BenevolentFortify();
		reinforceType = new BenevolentReinforce();
	}

}
