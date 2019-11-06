package com.riskGame.observer;

/**
 * This is a PlayerDominationViewPublisher interface implemented by respective Publishers.
 * @author GouthamG
 *
 */
public interface PlayerDominationViewPublisher {
	/**
	 * notifyObserver method which collects the message in action String variable.
	 */
	public void notifyObserver(String action);
}
