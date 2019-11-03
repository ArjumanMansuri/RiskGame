package com.riskGame.observer;

import java.util.ArrayList;

public abstract class PhaseViewObserver {
	
	String gamePhaseName;
	String currentPlayerName;
	ArrayList<String> actions;
	

	public abstract void update(String action);
	
	public abstract void setData(String playerName);

}