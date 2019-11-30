package com.riskGame.controller;
import com.riskGame.models.Country;
import com.riskGame.models.Map;

import java.util.HashMap;

public interface BaseMapFile {

    public static final String MAP_FILE_DIR = "maps/";

    // MAP FILE PARSER RESPONSE
    public static final int SAVE_MAP_COMMAND_ERROR = 0;
    public static final int SAVE_MAP_NO_CONTINENTS = 1;
    public static final int SAVE_MAP_INVALID = 2;
    public static final int SAVE_MAP_SUCCESS = 3;

    public Map read(String filename);
    public int write(String[] command);
    public boolean validateValidMapFile(String file);
    public String getSaveMapFileContent();
    public String getNeighborCSV(HashMap<String, Country> neighbours);
}
