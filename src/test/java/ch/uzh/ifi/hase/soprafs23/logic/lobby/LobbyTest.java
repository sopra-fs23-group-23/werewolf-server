package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.CoreMatchers.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LobbyTest {
    @Test
    void adminAddedOnCreationTest() {
        Player admin = new Player(1l, "admin");
        Lobby l = new Lobby(1l, admin);
        assertEquals(admin, l.getAdmin());
        assertEquals(admin, l.getPlayers().iterator().next());
    }

    @Test
    void addPlayerTest() {
        Player admin = new Player(12l, "admin");
        Player p = new Player(13l, "player");
        Lobby l = new Lobby(1l, admin);
        l.addPlayer(p);
        List<Player> actual = new ArrayList<>();
        List<Player> expected = Arrays.asList(p, admin);
        l.getPlayers().forEach(actual::add);
        assertThat("List equality without order", actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void testRemovePlayer() {
        Player admin = new Player(12l, "admin");
        Player p = new Player(13l, "player");
        Lobby l = new Lobby(1l, admin);
        l.addPlayer(p);
        l.removePlayer(p);
        assertThat(l.getPlayers(), not(hasItem(p)));
    }
}
