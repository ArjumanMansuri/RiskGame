package com.riskGame.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import com.riskGame.models.Continent;
import com.riskGame.models.Country;

/**
 * @author Sharan Chitta
 * Class to validate country and continent connections. 
 */
public class MapConnected {

	private HashMap<String, Boolean> countriesVisited;
	private HashMap<String, Boolean> continentsVisited;	
	private HashMap<String,Country> countries;
	private HashMap<String,Continent> continents;

	/**
	 * Constructor for connected country check.
	 * @param countries
	 */
	public MapConnected(HashMap<String, Country> countries) {
		super();
		this.countries = countries;
		this.countriesVisited = new HashMap<>();

		for(Entry<String, Country> country : countries.entrySet()) {			
			this.countriesVisited.put(country.getValue().getCountryName(), false);			
		}		
	}

	/**
	 * Constructor for connected continents. 
	 * @param continents
	 * @param countries
	 */
	public MapConnected(HashMap<String, Continent> continents, HashMap<String, Country> countries) {
		super();
		this.continents = continents;
		this.countries = countries;
		this.continentsVisited = new HashMap<>();
		this.countriesVisited = new HashMap<>();

		for(Entry<String, Continent> continent : continents.entrySet()) {			
			this.continentsVisited.put(continent.getValue().getContinentName(), false);			
		}		

		for(Entry<String, Country> country : countries.entrySet()) {			
			this.countriesVisited.put(country.getValue().getCountryName(), false);			
		}	
	}


	/**
	 * Validate the countries for their connection inside a continent.
	 * @return true if all countries are connected.
	 */
	public boolean checkConnectedCountries() {		
		iterateCountries(countries.entrySet().iterator().next().getValue());		
		for(Entry<String, Country> country : countries.entrySet()) {			
			if (this.countriesVisited.get(country.getValue().getCountryName()) == false) {				
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate if the continents are connected. 
	 * @return true if all the continents are connected.
	 */
	public boolean checkConnectedContinents() {
		iterateContinent(countries.entrySet().iterator().next().getValue());				
		for(Entry<String, Country> country : countries.entrySet()) {			
			if (this.countriesVisited.get(country.getValue().getCountryName()) == false || this.continentsVisited.get(country.getValue().getContinent()) == false) {				
				return false;
			}
		}
		return true;
	}

	/**
	 * Iterate over all the countries and mark their visit. 
	 * @param iterateCountry
	 */
	private void iterateCountries(Country iterateCountry) {		
		this.countriesVisited.put(iterateCountry.getCountryName(), true);		
		iterateCountry = MapFileEdit.isCountryExists(iterateCountry.getCountryName());
		for(Entry<String, Country> neighborCountry : iterateCountry.getNeighbours().entrySet()) {			
			if (countries.containsKey(neighborCountry.getKey())) {				
				if (!this.countriesVisited.get(neighborCountry.getKey())) {
					iterateCountries(neighborCountry.getValue());
				}
			}
		}		
	}	

	/**
	 * Iterate over all the continents and mark their visit.
	 * @param iterateCountry
	 */
	private void iterateContinent(Country iterateCountry) {
		iterateCountry = MapFileEdit.isCountryExists(iterateCountry.getCountryName());		
		this.countriesVisited.put(iterateCountry.getCountryName(), true);		
		this.continentsVisited.put(iterateCountry.getContinent(), true);		
		for(Entry<String, Country> neighborCountry : iterateCountry.getNeighbours().entrySet()) {			
			if (!this.countriesVisited.get(neighborCountry.getKey())) {
				iterateContinent(neighborCountry.getValue());
			}			
		}	
	}	
}
