package com.riskGame.observer;

/**
 * This is a PlayerDominationViewObserver class to get notified and display the domination data.
 * @author GouthamG
 *
 */
public  class PlayerDominationViewObserver {

	 /**
	 *String domniationViewData to display the domination details.
	 */
	 public String domniationViewData; 
	 
	 /**
	 *Update method for updating the log message.
	 */
	 public void update(String action) {
		 domniationViewData = action;
		  System.out.println(action);
			
		}
}
