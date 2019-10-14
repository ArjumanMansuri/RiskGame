package models;

import java.util.ArrayList;

public class Territory {
	
	private String territoryName;
	private int numberOfArmies;
	private ArrayList<Territory> neighbours = new ArrayList<Territory>();
	private String owner;
	
	public Territory() {
		neighbours = new ArrayList<Territory>();
	}
	
	public String getTerritoryName() {
		return territoryName;
	}
	public void setTerritoryName(String terrotoryName) {
		this.territoryName = terrotoryName;
	}
	public int getNumberOfArmies() {
		return numberOfArmies;
	}
	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}
	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(ArrayList<Territory> neighbours) {
		this.neighbours = neighbours;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
