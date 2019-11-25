package com.riskGame.controller;
import com.riskGame.models.Country;
import com.riskGame.models.Map;

import java.util.HashMap;

public class DominationMapParser implements BaseMapFile {

    @Override
    public Map read(String filename) {
        return null;
    }

    @Override
    public int write(String[] command) {
        return 1;
    }

    @Override
    public boolean validateValidMapFile(String file) {
        return false;
    }

    @Override
    public String getSaveMapFileContent() {
        return null;
    }

    @Override
    public String getNeighborCSV(HashMap<String, Country> neighbours) {
        return null;
    }
}
