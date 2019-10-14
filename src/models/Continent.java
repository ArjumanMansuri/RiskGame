package models;

import java.util.ArrayList;
import java.util.List;

public class Continent {
	
	private String continentName;
	private List<Territory> territories;
	private int controlValue;
	
	public Continent() {
		territories = new ArrayList<Territory>();
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

	public List<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}
	
	public void pushTerritory(Territory territory) {
		this.territories.add(territory);
	}
}
