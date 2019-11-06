package com.riskGame.models;

import java.util.Observable;

public class Card extends Observable {
	public final static int ARTILLARY = 0;
	public final static int INFANTRY = 1;
	public final static int CAVALRY = 2;
	
	private static int id = 0;
	// 0 - art, 1 - inf, 2 - cav
	int type;
	Player owner;
	
	public Card(int type) {
		id++;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return this.type;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void changeOwner(Player newOwner) {
		
		if(newOwner == null) {
			this.owner = newOwner;
			setChanged();
			notifyObservers("removed");
			return;
		}
		
		if(this.owner == null) {
			this.owner = newOwner;
			this.addObserver(newOwner);
			setChanged();
			notifyObservers("success");
		}
		else {
			setChanged();
			notifyObservers("failure");
		}
	}
}
