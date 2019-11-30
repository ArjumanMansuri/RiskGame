package com.riskGame.strategies;

import com.riskGame.models.Player;

public class RandomPlayer extends Player {

	public RandomPlayer() {
		// TODO Auto-generated constructor stub
		super();
		attackType = new RandomAttack();
		fortifyType = new RandomFortify();
		reinforceType = new RandomReinforce();
	}

}
