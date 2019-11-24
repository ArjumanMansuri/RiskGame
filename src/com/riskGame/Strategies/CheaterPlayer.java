package com.riskGame.Strategies;

import com.riskGame.models.Player;

public class CheaterPlayer extends Player {

	public CheaterPlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new CheaterAttack(getPlayerNumber());
		fortifyType = new CheaterFortify();
		reinforceType = new CheaterReinforce();
	}

}
