package it.unibo.modelTest;

import it.unibo.virtualCasino.model.Player;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest{
    Player p;

    @BeforeEach
    void setup(){
        System.out.println("!!! START Player TEST !!!");
        p = new Player("TESTPLAYER");
    }

    @Test void testInitBalance(){ //Test if the initial balance is 1000
        assertEquals(1000, p.getAccount());
    }
    
    @Test void testAddWin(){//Test the correct add amount of credit
        p.addWin(500);
        assertEquals(1500, p.getAccount());
    }

    @Test void testRemoveLoss(){//Test the correct remove the amount of credit
        p.removeLoss(1000);
        assertEquals(0, p.getAccount());
    }
}