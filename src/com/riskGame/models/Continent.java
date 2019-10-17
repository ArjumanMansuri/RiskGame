package com.riskGame.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a model class for the continent that holds the properties for a continent.
 * @author goumis
 *
 */
public class Continent {

	private String continentName;
	private List<Country> territories;
	private int controlValue;

	/**
	 * This is a default constructor which will create continent object.
	 *
	 */
	public Continent() {
		territories = new ArrayList<Country>();
	}

	/**
	 * getter method to get the name of the continent.
	 * @return continentName name of continent.
	 *
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * setter method to assign the continent name.
	 * @param continentName continent object.
	 *
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * getter method to get the control value of the continent.
	 * @return controlValue value.
	 *
	 */
	public int getControlValue() {
		return controlValue;
	}

	/**
	 * setter method to set the control value of the continent.
	 * @param controlValue player object.
	 *
	 */
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**
	 * getter method to get the list of territories.
	 * @return territories list of countries.
	 *
	 */
	public List<Country> getTerritories() {
		return territories;
	}

	/**
	 * setter method to set the list of territories.
	 * @param territories adds name of countries into list.
	 *
	 */
	public void setTerritories(List<Country> territories) {
		this.territories = territories;
	}

	/**
	 * This method adds the name of the new country into the existing list.
	 * @param territory new country to be appended in list.
	 *
	 */
	public void pushTerritory(Country territory) {
		this.territories.add(territory);
	}
}
