package com.riskGame.models;

import java.util.HashMap;

/**
 *
 * @author Arjuman Mansuri
 * @author Mudra-PC
 * Model class for Game with map and playersList as its data members
 */
public class Game {

    /**
     * @param map Refers to the map used for the game play
     * @param playersList Refers to the all the players of a particular game
     */
    private static Map map = new Map();
    private static HashMap<Integer,Player> playersList = new HashMap<>();
    private static Map editMap = new Map();
    private static boolean editMapSet = false;

    public Game() {
        playersList = new HashMap<Integer, Player>();
        map = new Map();
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        Game.map = map;
    }

    public static HashMap<Integer, Player> getPlayersList() {
        return playersList;
    }

    public static void setPlayersList(HashMap<Integer, Player> playersList) {
        Game.playersList = playersList;
    }

    public static Map getEditMap() {
        return editMap;
    }

    public static void setEditMap(Map editMap) {
        Game.editMap = editMap;
    }

    public static boolean isEditMapSet() {
        return editMapSet;
    }

    public static void setEditMapSet(boolean editMapSet) {
        Game.editMapSet = editMapSet;
    }
}
