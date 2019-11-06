package com.riskGame.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.riskGame.models.Card;
import com.riskGame.models.Player;

public class CardExchange implements Observer {
	
	@Override
	public void update(Observable o, Object arg) {
		Player p = (Player)o;
		int a = 0;
		int ca = 0;
		int i = 0;
		
		for(Card c : p.getCards()) {
			if(c.getType() == Card.ARTILLARY) {
				a++;
			}
			else if(c.getType() == Card.INFANTRY) {
				i++;
			}
			else if(c.getType() == Card.CAVALRY) {
				ca++;
			}
		}
		
		System.out.println("Number of Artillary Cards = " + a + "\nNumber of Cavalry Cards = " + ca + "\nNumber of Infantry Cards = " + i);
		
		if(((String)arg).equals("added")) {
			
		}
		else if(((String)arg).equals("removed")) {
			
		}
	}
}
