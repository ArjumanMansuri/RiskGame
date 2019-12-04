package com.riskGame.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.riskGame.models.Card;
import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;
import com.riskGame.observer.FortificationPhaseObserver;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;
import com.riskGame.observer.PlayerDominationViewObserver;
import com.riskGame.observer.PlayerDominationViewPublisher;
import com.riskGame.observer.ReinforcementPhaseObserver;

/**
 * This class contains the business logic of the Reinforcement Phase.
 * @author GouthamG
 *
 */
public class ReinforcementPhase implements PhaseViewPublisher, PlayerDominationViewPublisher{
	
	private PhaseViewObserver newObserver;
	private PlayerDominationViewObserver newDomiantionObserver;
	
	/**
	 * constructor for this class
	 */
	public ReinforcementPhase() {
		newObserver = new ReinforcementPhaseObserver();
		newDomiantionObserver = new PlayerDominationViewObserver();
		
	}
	
	/**
	 * This method is used to  calculate the number of Reinforcement armies and set to access globally.
	 * @param playerNumber turn of player.
	 * 
	 */
	public void calculateReinforcementArmies(int playerNumber) {		
		Player p = Game.getPlayersList().get(playerNumber);
		this.notifyObserver(p.getPlayerName()+" : Calculating reinforcement armies");
		int newArmies =  p.getOwnedCountries().size() /3;

		for(Map.Entry<String, Continent> entry : Game.getMap().getContinents().entrySet()) {
		    String key = entry.getKey();
		    Continent value = entry.getValue();
		    if(value.checkOwnership(p) == true) {
		    	newArmies += value.getControlValue();
		    }
		}
		
		this.notifyObserver("Number of new armies is " + newArmies + " for player " + p.getPlayerName());
		p.setPlayerNumOfArmy(newArmies);
	}
	/**
	 * This function is to process the player's command and for reinforcement
	 * @param player  id of player
	 * @param commandComponents command in string array
	 * @return "playerRinforced" if successful else throws an Error
	 */
	public String processReinforceCmd(int player, String[] commandComponents) {
		if(commandComponents.length != 3) {
			return "Player not reinforced";
		}
		
		if(commandComponents[0].equalsIgnoreCase("showmap")) {
			MapFileEdit.gamePlayShowMap();
			return "";
		}
		
		String countryName = commandComponents[1];
		String strNum = commandComponents[2];
		
		if(!strNum.matches("\\d+")) {
			return "Error : Please enter a valid number of armies";
		}
		
		int num = Integer.parseInt(strNum);
		
		Player p = Game.getPlayersList().get(player);
		if(!p.getOwnedCountries().contains(countryName)) {
			return "Error : Country not owned by "+p.getPlayerName()+" or invalid country ";
		}
		
		
		
		if(!(p.getPlayerNumOfArmy()>=num)) {
			return "Error : Insufficient armies to move";
		}
		
		p.setPlayerNumOfArmy(p.getPlayerNumOfArmy()-num);
		this.notifyObserver("Calculating Reinforcement armies for the player " + p.getPlayerName());
		Country.getListOfCountries().get(countryName).setNumberOfArmies(Country.getListOfCountries().get(countryName).getNumberOfArmies()+num);
		this.notifyObserver("New armies for the country " + countryName + " is " + Country.getListOfCountries().get(countryName).getNumberOfArmies());
	
		return "Player reinforced";
	}
	
	/**
	 * Exchange card logic
	 * @param playerNumber id of player
	 * @param commandComponents command in string array
	 * @return "Exchange Done" if successful else throws an Error
	 */
	public String processExchangeCardCmd(int playerNumber, String[] commandComponents) {
		Player p = Game.getPlayersList().get(playerNumber);
		if(commandComponents[1].contains("none")) {
			return "No card exchange was made";
		}
		else {
			int a = Integer.parseInt(commandComponents[1]);
			int b = Integer.parseInt(commandComponents[2]);
			int c = Integer.parseInt(commandComponents[3]);
			
			if(a == 1 && b == 1 && c == 1) {
				Card afound = null;
				Card bfound = null;
				Card cfound = null;
				
				for(Card card : Game.getCards()) {
					if(card.getOwner() == p && card.getType() == Card.ARTILLARY) {
						afound = card;
					}
					else if(card.getOwner() == p && card.getType() == Card.CAVALRY) {
						bfound = card;
					}
					else if(card.getOwner() == p && card.getType() == Card.INFANTRY) {
						cfound = card;
					}
				}
				
				if(afound != null && bfound != null && cfound != null) {
					afound.changeOwner(null);
					bfound.changeOwner(null);
					cfound.changeOwner(null);
					
					int cardArmy = 0;
					
					if(Game.exchanges_made < 7) {
						cardArmy = 15;
					}
					else {
						cardArmy = 15 + (5 * (Game.exchanges_made - 6));
					}
					
					this.notifyObserver("Number of new armies after card exchange is " + cardArmy + " for player " + p.getPlayerName());
					System.out.println("Player army before card army: " + p.getPlayerNumOfArmy());
					p.setPlayerNumOfArmy(p.getPlayerNumOfArmy() + cardArmy);
					System.out.println("Player army after card army: " + p.getPlayerNumOfArmy());
					System.out.println("Player received extra armies after card exchange: " + cardArmy);
					return "Exchange Done";
				}
				else {
					this.notifyObserver("Exchange Not Possible as incorrect card selection made");
					return "Exchange Not Possible as incorrect card selection made";
				}
			}
			else if (a == 3) {
				Card[] cards = new Card[3];
				int ctr = 0;
				
				for(Card card : Game.getCards()) {
					if(ctr == 3) break;
					if(card.getOwner() == p && card.getType() == Card.ARTILLARY) {
						cards[ctr++] = card;
					}
				}
				
				if(ctr == 3) {
					cards[0].changeOwner(null);
					cards[1].changeOwner(null);
					cards[2].changeOwner(null);
					int cardArmy = 0;
					
					if(Game.exchanges_made < 7) {
						cardArmy = 15;
					}
					else {
						cardArmy = 15 + (5 * (Game.exchanges_made - 6));
					}
					
					this.notifyObserver("Number of new armies after card exchange is " + cardArmy + " for player " + p.getPlayerName());
					p.setPlayerNumOfArmy(p.getPlayerNumOfArmy() + cardArmy);
					System.out.println("Player army after card army: " + p.getPlayerNumOfArmy());
					System.out.println("Player received extra armies after card exchange: " + cardArmy);
					return "Exchange Done";
				}
				else {
					this.notifyObserver("Exchange Not Possible as incorrect card selection made");
					return "Exchange Not Possible as incorrect card selection made";
				}
			}
			else if (b == 3) {
				Card[] cards = new Card[3];
				int ctr = 0;
				
				for(Card card : Game.getCards()) {
					if(ctr == 3) break;
					if(card.getOwner() == p && card.getType() == Card.ARTILLARY) {
						cards[ctr++] = card;
					}
				}
				
				if(ctr == 3) {
					cards[0].changeOwner(null);
					cards[1].changeOwner(null);
					cards[2].changeOwner(null);
					int cardArmy = 0;
					
					if(Game.exchanges_made < 7) {
						cardArmy = 15;
					}
					else {
						cardArmy = 15 + (5 * (Game.exchanges_made - 6));
					}
					
					this.notifyObserver("Number of new armies after card exchange is " + cardArmy + " for player " + p.getPlayerName());
					p.setPlayerNumOfArmy(p.getPlayerNumOfArmy() + cardArmy);
					return "Exchange Done";
				}
				else {
					this.notifyObserver("Exchange Not Possible as incorrect card selection made");
					return "Exchange Not Possible as incorrect card selection made";
				}
			}
			else if (c == 3) {
				Card[] cards = new Card[3];
				int ctr = 0;
				
				for(Card card : Game.getCards()) {
					if(ctr == 3) break;
					if(card.getOwner() == p && card.getType() == Card.ARTILLARY) {
						cards[ctr++] = card;
					}
				}
				
				if(ctr == 3) {
					cards[0].changeOwner(null);
					cards[1].changeOwner(null);
					cards[2].changeOwner(null);
					int cardArmy = 0;
					
					if(Game.exchanges_made < 7) {
						cardArmy = 15;
					}
					else {
						cardArmy = 15 + (5 * (Game.exchanges_made - 6));
					}
					
					this.notifyObserver("Number of new armies after card exchange is " + cardArmy + " for player " + p.getPlayerName());
					p.setPlayerNumOfArmy(p.getPlayerNumOfArmy() + cardArmy);
					return "Exchange Done";
				}
				else {
					this.notifyObserver("Exchange Not Possible as incorrect card selection made");
					return "Exchange Not Possible as incorrect card selection made";
				}
			}
			else {
				this.notifyObserver("Exchange Not Possible as incorrect card selection made");
				return "Exchange Not Possible as incorrect card selection made";
			}
		}
	}
	
	
	/**
	 * This method is used to calculate reinforcement armies for testing.
	 * @param currentPlayer turn of the current player.
	 * @return number of reinforced armies.
	 * 
	 */
	public int calculateReinforcementArmies(Player currentPlayer) {
		int reinformentArmies =  currentPlayer.getOwnedCountries().size() /3;
		return reinformentArmies;
	}
	
	/**
	 * This method is to notify the observer pattern
	 * @param action string to notify the observer
	 */
	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);
		
	}
	
	/**
	 * This method is to notify the Domination observer pattern
	 * @param action string to notify the observer
	 */
	public void notifyDominationObserver(String action) {
		//this.newDomiantionObserver.updateDomination(action);
	}
}
