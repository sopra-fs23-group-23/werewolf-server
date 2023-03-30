package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void getNameTest() {
        Player player = new Player(12l, "Test");
        assertEquals("Test", player.getName());
    }

    @Test
    public void getIdTest() {
        Player player = new Player(12l, "Test");
        assertEquals(12l, player.getId());
    }

    @Test
    public void killPlayerTest() {
        Player player = new Player(12l, "Test");
        assertTrue(player.isAlive());
        player.killPlayer();
        assertFalse(player.isAlive());
    }

    private class MockObserver implements PlayerObserver {
        private boolean playerAlive = true;

        @Override
        public void onPlayerKilled() {
            playerAlive = false;
        }

        public boolean isPlayerAlive() {
            return playerAlive;
        }

    }

    @Test
    public void observerTest() {
        Player player = new Player(12l, "Test");
        MockObserver observer = new MockObserver();
        player.addObserver(observer);
        assertTrue(observer.isPlayerAlive());
        player.killPlayer();
        assertFalse(observer.isPlayerAlive());

    }
}
