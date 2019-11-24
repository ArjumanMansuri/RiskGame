package com.riskGame.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This is a model class for the country that holds the properties for a country.
 * @author Mudra-PC
 *
 */
public class Country  implements Serializable{

	private static HashMap<String,Country> listOfCountries = new HashMap<>();

	private String countryName;
	private int numberOfArmies;
	private HashMap<String, Country> neighbours;
	private int owner;
	private String continent;

	/**
	 * getter method for list of countries
	 * @return list of countries
	 */
	public static HashMap<String, Country> getListOfCountries() {
		return listOfCountries;
	}
	
	/**
	 * setter method for list of countries
	 * @param listOfCountries list of countries
	 */
	public static void setListOfCountries(HashMap<String, Country> listOfCountries) {
		Country.listOfCountries = listOfCountries;
	}
	/**
	 * getter method for continent
	 * @return continent
	 */
	public String getContinent() {
		return continent;
	}
	/**
	 * setter method for name of continent
	 * @param continent continent's name
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}
	/**
	 * constructor for the class
	 */
	public Country() {
		neighbours = new HashMap();
	}
	/**
	 * getter method for country name
	 * @return countryName
	 */
	public String getCountryName() {
		return countryName;
	}
	/**
	 * setter method for country name
	 * @param countryName country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	/**
	 * getter method for number of armies
	 * @return number of armies
	 */
	public int getNumberOfArmies() {
		return numberOfArmies;
	}
	
	/**
	 * setter method for number of armies
	 * @param numberOfArmies number of armies
	 */
	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}
	
	/**
	 * getter method for neighbours
	 * @return neighbours
	 */
	public HashMap<String, Country> getNeighbours() {
		return neighbours;
	}
	
	/**
	 * setter method for neighbours
	 * @param neighbours neighbours
	 */
	public void setNeighbours(HashMap<String, Country> neighbours) {
		this.neighbours = neighbours;
	}
	
	/**
	 * getter method for Owner
	 * @return Owner
	 */
	public int getOwner() {
		return owner;
	}
	/**
	 * setter method for Owner
	 * @param owner owner
	 */
	public void setOwner(int owner) {
		this.owner = owner;
	}
	

}
