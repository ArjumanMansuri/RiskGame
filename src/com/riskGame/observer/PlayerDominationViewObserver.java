package com.riskGame.observer;

import com.riskGame.view.*;

/**
 * This is a PlayerDominationViewObserver class to get notified and display the domination data.
 * @author GouthamG
 *
 */
public  class PlayerDominationViewObserver {
	
	 /**
	 *String domniationViewData to display the domination details.
	 */
	 public String dominationViewData; 
	 GameView view = new GameView();
	 
	 /**
	 *UpdateDomination method for updating the log message.
	 */
	 public void updateDomination(String action) {
		 dominationViewData = action;
		 view.display();
		}
}
