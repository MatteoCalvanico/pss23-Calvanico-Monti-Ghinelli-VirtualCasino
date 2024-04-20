package it.unibo.modelTest;

import it.unibo.virtualCasino.model.Player;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlayerTest{
    Player p = new Player("pippo");
    @Test void testInitBalance(){
        assertEquals(1000, p.getAccount());
    } 
}