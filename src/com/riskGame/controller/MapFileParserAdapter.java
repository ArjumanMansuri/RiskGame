package com.riskGame.controller;
import com.riskGame.models.Country;
import com.riskGame.models.Map;

import java.util.HashMap;

public class MapFileParserAdapter implements BaseMapFile {

    MapFileParser map;

    public MapFileParserAdapter() {
        map = new MapFileParser();
    }

    @Override
    public Map read(String filename) {
        return map.readFileData(filename);
    }

    @Override
    public boolean validateValidMapFile(String file) {
        return map.validateValidMapFile(file);
    }

    @Override
    public String getSaveMapFileContent() {
        return map.getSaveMapFileContent();
    }

    @Override
    public String getNeighborCSV(HashMap<String, Country> neighbours) {
        return map.getNeighborCSV(neighbours);
    }

    @Override
    public int write(String[] command) {
        return map.saveMap(command);
    }
}
