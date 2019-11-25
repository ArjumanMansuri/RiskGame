package com.riskGame.controller;
import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;

import java.io.*;
import java.util.HashMap;

public class DominationMapParser implements BaseMapFile {

    static HashMap<Integer,String> countryIndexes = new HashMap<Integer,String>();  // variable to retain the country indexes
    static HashMap<String,String> countryContinentIndexes = new HashMap<String,String>();  // variable to retain the country continents
    static HashMap<String, Integer> continentsIndex = new HashMap<String, Integer>();
    MapFileEdit fileEdit;

    public DominationMapParser() {
        fileEdit = new MapFileEdit();
    }

    @Override
    public Map read(String fileName) {
        if(!validateValidMapFile(fileName)) {
            return null;
        }
        Map map = new Map();
        HashMap<String, Continent> continents = new HashMap<String, Continent>();

        int continentCounter = 1;
        try {
            boolean countriesLineFound = false,continentsLineFound = false,bordersLineFound = false;
            BufferedReader readMap = new BufferedReader(new FileReader(fileName));
            while(readMap.ready()) {
                String line = readMap.readLine().trim();

                // Skip the comment line in the countries lines containing the semicolon
                if(line.contains(";")){
                    continue;
                }

                if(line.isEmpty()) {
                    continue;
                }
                if(line.equals("[continents]")) {
                    continentsLineFound = true;
                    countriesLineFound = false;
                    bordersLineFound = false;
                    continue;
                }
                if(line.equals("[countries]")) {
                    countriesLineFound = true;
                    continentsLineFound = false;
                    bordersLineFound = false;
                    continue;
                }
                if(line.equals("[borders]")) {
                    bordersLineFound = true;
                    countriesLineFound = false;
                    continentsLineFound = false;
                    continue;
                }

                // Process the continent lines
                if(continentsLineFound) {
                    String[] continentLine = line.split(" ");
                    Continent continent = new Continent();
                    continent.setContinentName(continentLine[0]); // name
                    continent.setControlValue(Integer.parseInt(continentLine[1])); // control value
                    continents.put(continentLine[0], continent);

                    // temporary storage of continent and the index
                    continentsIndex.put(continentLine[0], continentCounter);

                    // this counter is the continent index used in the countries list
                    continentCounter++;
                }

                // countries lines parser
                if(countriesLineFound) {
                    String[] countryLine = line.split(" ");

                    /**
                     *   Check 'if' the territory line data has a valid continent name value
                     */
                    String countryContinent = countryLine[2]; // continent line

                    if(!continentsIndex.containsValue(countryContinent)){
                        // Continent doesn't exist - throw invalid map error
                    } else {
                        int countryIndex = Integer.parseInt(countryLine[0]);
                        String countryName = countryLine[1];

                        // Put to the country index pair
                        countryIndexes.put(countryIndex,countryName);
                        countryContinentIndexes.put(countryName,countryContinent);
                    }
                }

                // Borders list lines parser
                if(bordersLineFound){
                    String[] countryBorderLine = line.split(" ");

                    int countryIndex = Integer.parseInt(countryBorderLine[0]);
                    String countryName = countryIndexes.get(countryIndex);
                    String countryContinent = countryContinentIndexes.get(countryName); // continent line
                    HashMap<String,Country> neighbors = convertToTerritories(countryBorderLine);

                    // make the country object and push to the country static list and continent list
                    Country currentCountry = new Country();
                    currentCountry.setCountryName(countryName);
                    currentCountry.setContinent(countryContinent);
                    currentCountry.setNeighbours(neighbors);

                    // Update country to continent
                    Continent continentToUpdate = continents.get(countryContinent);
                    continentToUpdate.pushTerritory(currentCountry);

                    // Add country to static map in Country
                    Country.getListOfCountries().put(currentCountry.getCountryName(), currentCountry);
                }
            }

            readMap.close();
            map.setContinents(continents);
            if(!map.getContinents().isEmpty()) {
                return map;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int write(String[] commandInput) {
        if(commandInput.length != 2) {
            return BaseMapFile.SAVE_MAP_COMMAND_ERROR;
        }

        if(!fileEdit.validateMap()) {
            MapFileEdit.printMapStatusMessage(false);
            return BaseMapFile.SAVE_MAP_NO_CONTINENTS;
        }

        if(Game.getEditMap().getContinents().isEmpty()) {
            return BaseMapFile.SAVE_MAP_NO_CONTINENTS;
        }

        String fileName = BaseMapFile.MAP_FILE_DIR + commandInput[1];
        HashMap<String, Continent> continents = Game.getEditMap().getContinents();
        String fileContent;

        try {
            File file = new File(fileName);
            if(!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            fileContent = getSaveMapFileContent();
            if(fileContent.length() < 1) {
                // error in creating the file content
                writer.close();
                return BaseMapFile.SAVE_MAP_INVALID;
            }
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game.setEditMapSet(false); // last statement in this method
        return BaseMapFile.SAVE_MAP_SUCCESS;
    }

    @Override
    public boolean validateValidMapFile(String fileName) {
        boolean validResult = false;
        int findCount = 0;
        try {
            BufferedReader readMap = new BufferedReader(new FileReader(fileName));
            while(readMap.ready()) {
                String line = readMap.readLine().trim();
                if(line.isEmpty()) {
                    continue;
                }
                if(line.equals("[continents]")) {
                    findCount++;
                }
                if(line.equals("[countries]")) {
                    findCount++;
                }
                if(line.equals("[borders]")) {
                    findCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(findCount == 3) {
            validResult = true;
        }
        return validResult;
    }

    @Override
    public String getSaveMapFileContent() {
        HashMap<String, Integer> countryIndexesReversed = reverseCountryIndexes();

        // Print continents
        String fileContent = "[continents]\n";
        for(String printContinentKey : Game.getEditMap().getContinents().keySet()) {
            Continent continent = Game.getEditMap().getContinents().get(printContinentKey);
            fileContent += continent.getContinentName() + " " + continent.getControlValue() + "\n";
        }

        // Print countries
        fileContent += "\n";
        fileContent += "[countries]\n";

        for(String printTerritoryContinentKey : Game.getEditMap().getContinents().keySet()) {
            Continent printContinent = Game.getEditMap().getContinents().get(printTerritoryContinentKey);

            int continentIndex = continentsIndex.get(printContinent.getContinentName());

            for(Country printCountry : printContinent.getTerritories()) {
                String countryName = printCountry.getCountryName();
                int countryIndex = countryIndexesReversed.get(countryName);

                fileContent += countryIndex + " " + countryName + " " + continentIndex + " 0 0";
            }
            fileContent += "\n";
        }

        // Print Borders
        fileContent += "[borders]\n";

        for(String printTerritoryContinentKey : Game.getEditMap().getContinents().keySet()) {
            Continent currentContinent = Game.getEditMap().getContinents().get(printTerritoryContinentKey);

            for(Country printCountry : currentContinent.getTerritories()) {
                String countryName = printCountry.getCountryName();
                int countryIndex = countryIndexesReversed.get(countryName);

                String neighborSSV = getNeighborSSV(printCountry.getNeighbours(), countryIndexesReversed);
                fileContent += countryIndex + " " + neighborSSV + "\n";
            }
        }


        return fileContent;
    }

    private String getNeighborSSV(HashMap<String, Country> neighbours, HashMap<String, Integer> countryIndexesReversed) {
        String neighborSSV = "";
        for(String neighborCountryName : neighbours.keySet()) {
            int neighborCountryIndex = countryIndexesReversed.get(neighborCountryName);
            neighborSSV += neighborCountryIndex + " ";
        }

        neighborSSV = neighborSSV.replaceAll(",$", "");
        neighborSSV += "\n;";
        return neighborSSV;
    }

    /**
     * Reverse the countryindexes variable - with String as key and Integer as value
     * @return
     */
    private HashMap<String, Integer> reverseCountryIndexes() {
        return null;
    }

    @Override
    public String getNeighborCSV(HashMap<String, Country> neighbours) {
        return null;
    }

    /**
     * This method converts territoryLine into a list of territory instances.
     * @param territoryLine territory line from map file.
     * @return territories List of territories.
     *
     */
    private HashMap<String,Country> convertToTerritories(String[] territoryLine) {
        HashMap<String,Country> neighborList = new HashMap<String, Country>();
        for(int territoryLineIndex = 1;territoryLineIndex < territoryLine.length;territoryLineIndex++) {
            Country currentCountry = new Country();

            int countryIndex = Integer.parseInt(territoryLine[territoryLineIndex]);

            // Get the country name from the countryIndexes Hashmap using the country index
            String countryName = countryIndexes.get(countryIndex);

            currentCountry.setCountryName(countryName);
            neighborList.put(countryName, currentCountry);
        }
        return neighborList;
    }

}
