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
        public void onPlayerKilled_Unrevivable(Player player) {
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
        assertTrue(observer.isPlayerAlive());
        player.setDeadPlayerUnrevivable();
        assertFalse(observer.isPlayerAlive());
    }

    @Test
    void setDeadPlayerUnrevivableTest_notDead() {
        Player player = new Player(12l, "Test");
        MockObserver observer = new MockObserver();
        player.addObserver(observer);
        player.setDeadPlayerUnrevivable();
        assertTrue(observer.isPlayerAlive());
    }

    @Test
    void testEquality() {
        Player p1 = new Player(1l, "a");
        Player p2 = new Player(1l, "a");
        assertTrue(p1.equals(p2), "Players, which are equal not considered equal by .equal()");
        assertTrue(p1.hashCode() == p2.hashCode(), "Equal players do not have equal hashcodes");
    }

    @Test
    void testInequality() {
        Player p1 = new Player(1l, "a");
        Player p2 = new Player(2l, "a");
        assertFalse(p1.equals(p2), "Players, which are not equal considered equal by .equal()");
        assertFalse(p1.hashCode() == p2.hashCode(), "Inequal players have equal hashcodes");
    }
}
