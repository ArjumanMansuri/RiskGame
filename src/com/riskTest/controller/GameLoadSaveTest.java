package com.riskTest.controller;

import com.riskGame.controller.*;
import com.riskGame.models.*;
import com.riskGame.builder.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GameLoadSaveTest {
	LoadGame lg = new LoadGame();
	SaveGame sg = new SaveGame();

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
			assertEquals("done", sg.buildGame("savegame filename"));
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
			sg.buildGame("savegame abc");
		} catch (IOException e) {
			// test
			e.printStackTrace();
		}
		try {
			assertEquals("done",lg.buildGame("loadgame abc"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
