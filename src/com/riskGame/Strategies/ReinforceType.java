package com.riskGame.Strategies;

import com.riskGame.controller.ReinforcementPhase;

public interface ReinforceType {
	String reinforce(int player,String command);
}

class HumanReinforce implements ReinforceType{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - command from the user input.
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	@Override
	public String reinforce(int player,String command) {
		// TODO Auto-generated method stub
		ReinforcementPhase rp = new ReinforcementPhase();
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}
		//check if it is a reinforcement command
		String[] commandComponents = command.split(" ");
		if(commandComponents.length < 2 || commandComponents.length > 4) {
			return "Error : Number of arguments does not match";
		}
		
		String commandName = commandComponents[0];
		if(commandName.equalsIgnoreCase("reinforce")) {
			return rp.processReinforceCmd(player, commandComponents);
		}
		else if(commandName.equalsIgnoreCase("exchangecards")) {
			return rp.processExchangeCardCmd(player, commandComponents);
		}
		else {
			return "Error : Please enter reinforcement command";
		}
	}
}


class BenevolentReinforce implements ReinforceType{

	@Override
	public String reinforce(int player,String command) {
		// TODO Auto-generated method stub
		return "";
	}
}


class AggresiveReinforce implements ReinforceType{

	@Override
	public String reinforce(int player,String command)  {
		// TODO Auto-generated method stub
		return "";
	}
}


class RandomReinforce implements ReinforceType{

	@Override
	public String reinforce(int player,String command)  {
		// TODO Auto-generated method stub
		return "";
	}
}


class CheaterReinforce implements ReinforceType{

	@Override
	public String reinforce(int player,String command)  {
		// TODO Auto-generated method stub
		return "";
	}
}

