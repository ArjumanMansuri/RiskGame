package com.riskGame.observer;

import java.util.ArrayList;

import com.riskGame.view.*;

/**
 * This is a PlayerDominationViewObserver class to get notified and display the domination data.
 * @author GouthamG
 *
 */
public  class PlayerDominationViewObserver extends PhaseViewObserver{
	
	 /**
	 *String domniationViewData to display the domination details.
	 */
 	 
	 public PlayerDominationViewObserver() {
			super();
			this.actions = new ArrayList<String>();
			this.gamePhaseName = "DOMINATION VIEW";
		}
	 /**
	 *UpdateDomination method for updating the log message.
	 */
	/* public void updateDomination(String action) {
		 dominationViewData = action;
		 StartupPhaseObserver.view.display();
		}
*/
	@Override
	public void update(String action) {
		//if(! (action == null))
		//{
			//StartupPhaseObserver.dominationViewData = action;
			//StartupPhaseObserver.view.display();
			
		//}	
	//	else
			//StartupPhaseObserver.dominationViewData = "";
	}

	@Override
	public void setData(String playerName) {
		
	}
}
