package models;

import java.util.ArrayList;
import java.util.List;

public class Continent {
	
	private String continentName;
	private List<Country> territories;
	private int controlValue;
	
	public Continent() {
		territories = new ArrayList<Country>();
	}
	
	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public int getControlValue() {
		return controlValue;
	}

	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	public List<Country> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Country> territories) {
		this.territories = territories;
	}
	
	public void pushTerritory(Country territory) {
		this.territories.add(territory);
	}
}
