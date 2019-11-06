package com.riskGame.models;

import java.util.Observable;
/**
 * This is a model class for the card that holds the properties for cards
 * @author Mudra-PC
 *
 */
public class Card extends Observable {
	public final static int ARTILLARY = 0;
	public final static int INFANTRY = 1;
	public final static int CAVALRY = 2;

	private static int id = 0;
	// 0 - art, 1 - inf, 2 - cav
	int type;
	Player owner;

	/**
	 * Parameterized constructor for card
	 * @param type type of the card
	 */
	public Card(int type) {
		id++;
		this.type = type;
	}

	/**
	 *  getter method to get the id to maintain cards
	 */
	public int getId() {
		return id;
	}

	/**
	 *  getter method that gets type of cards
	 * @return type of the card
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * getter method to get the owner of the card
	 * @return name of the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Method to change the owner after the attack phase and game play.
	 * @param newOwner name of the new owner
	 */
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
