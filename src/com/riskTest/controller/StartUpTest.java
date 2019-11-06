package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import com.riskGame.controller.*;
import com.riskGame.models.*;


public class StartUpTest {
    StartUpPhase st =  new StartUpPhase();
    HashMap<Integer,Player> temp =  new HashMap<Integer, Player>();
    Player playerTest = new Player();

    @Test
    public void testIfContains(){
        playerTest.setPlayerName("Abc");
        temp.put(1,playerTest);

        assertEquals(true, st.ifContains(temp,"Abc"));
    }

    @Test
    public void testInputValidator(){
        assertEquals(1, st.inputValidator("gameplayer -add a -add b"));
    }

    @Test
    public void testAllPlayerArmies(){
        playerTest.setPlayerName("abc");
        Player p2 = new Player();
        p2.setPlayerName("def");
        temp.put(1,playerTest);
        temp.put(2,p2);

        assertEquals("done",st.allPlayerArmies());
    }

    @Test
    public void testParser(){
        assertEquals("error",st.parser(""));
    }
    
    @Test
    public void testNumberOfPlayers() {
    	st.setNoOfPlayers(6);
    	assertEquals("done", st.parser("5"));
    }
    
}
