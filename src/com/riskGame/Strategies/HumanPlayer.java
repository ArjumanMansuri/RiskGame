package com.riskGame.Strategies;

import com.riskGame.models.Player;

public class HumanPlayer extends Player {

	public HumanPlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new HumanAttack();
		fortifyType = new HumanFortify();
		reinforceType = new HumanReinforce();
	}

}
