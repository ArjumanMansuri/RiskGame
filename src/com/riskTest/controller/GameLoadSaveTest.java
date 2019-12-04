package com.riskTest.controller;

import com.riskGame.controller.*;
import com.riskGame.models.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GameLoadSaveTest {
	GameLoadSave gls = new GameLoadSave();
	/**
	 * Test if the card type is correctly set.
	 */
	@Test
	public void testSaveGame() {
		ArrayList<Card> card = new ArrayList<Card>();
		Card a = new Card(Card.ARTILLARY);
		card.add(a);
		Game.setCards(card);
		Game.setMap(new Map());
		Game.setPhase("Reinforcement");
		Game.setPlayersList(new HashMap<Integer, Player>());
		Game.setPlayerTurn(1);
		Country.setListOfCountries(new HashMap<String, Country>());
		try {
			assertEquals("done", GameLoadSave.save("savegame filename"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoadGame() {
		ArrayList<Card> card = new ArrayList<Card>();
		Card a = new Card(Card.ARTILLARY);
		card.add(a);
		Game.setCards(card);
		Game.setMap(new Map());
		Game.setPhase("Reinforcement");
		Game.setPlayersList(new HashMap<Integer, Player>());
		Game.setPlayerTurn(1);
		Country.setListOfCountries(new HashMap<String, Country>());
		try {
			gls.save("savegame abc");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			assertEquals("done",gls.load("loadgame abc"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
