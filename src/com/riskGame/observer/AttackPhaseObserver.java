package com.riskGame.observer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
/**
 * This is a Observable class for Attack phase that is extended from PhaseViewObserver abstract class.
 * @author GouthamG
 *
 */


public class AttackPhaseObserver extends PhaseViewObserver{
	
	public static JFrame frame = new JFrame();
	public static JTextArea textArea = new JTextArea(30,40);
	public static JScrollPane scroll = new JScrollPane(textArea);

	/**
	 * Constructor for AttackPhaseObserver sets the gamePhaseName.
	 */
	public AttackPhaseObserver() {
		super();
		this.actions = new ArrayList<String>();
		this.gamePhaseName = "ATTACK PHASE";
	}

	/**
	 * Override method of update which handles the complete logs of the phase.
	 */
	@Override
	public void update(String action) {
		//String attackPhaseData = action;
		
		if(! (action == null))
		{
			textArea.append(action);
			textArea.append("\n-------------------------\n");
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);
			
			 scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			 scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			 
			 frame.setContentPane(scroll);
			 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			 frame.setResizable(false);
			 frame.pack();
		     frame.setVisible(true);
		}	
		else
			textArea.setText("");
	}

	/**
	 * Override method of setData - sets the currentPlayerName.
	 */
	@Override
	public void setData(String playerName) {
		this.currentPlayerName = playerName;
		
	}

}
