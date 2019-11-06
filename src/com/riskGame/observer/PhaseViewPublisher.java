package com.riskGame.observer;

/**
 * This is a PhaseViewPublisher interface implemented by all Publishers in each Phase.
 * @author GouthamG
 *
 */
public interface PhaseViewPublisher {

	/**
	 * notifyObserver method which collects the message in action String variable.
	 */
	public void notifyObserver(String action);
		
		
}
