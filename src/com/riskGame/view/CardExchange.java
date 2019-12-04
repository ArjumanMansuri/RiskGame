package com.riskGame.view;
import java.util.Observable;
import java.util.Observer;

import com.riskGame.models.Card;
import com.riskGame.models.Player;
import com.riskGame.observer.StartupPhaseObserver;

public class CardExchange implements Observer {
	
	static public String cardViewData;
	
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
		
		String showData = "";
		
		if(((String)arg).equals("added")) {
			showData  = "\n"+ p.getPlayerName() + " received a new card.";			
		}
		else if(((String)arg).equals("removed")) {
			showData =  "\n" + p.getPlayerName() + " used a card.";			
		}
		
		// Display to Window 
		System.out.println(showData);
		cardViewData = showData;
		StartupPhaseObserver.view.display();

		showData = "\n Number of Artillary Cards = " + a + "\n Number of Cavalry Cards = " + ca + "\n Number of Infantry Cards = " + i;
		System.out.println(showData);
		cardViewData = showData;
		StartupPhaseObserver.view.display();			
	}
}
