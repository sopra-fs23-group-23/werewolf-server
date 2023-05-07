package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
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

    @Test
    void testGetLobbySize(){
        Player admin = new Player(12l, "admin");
        Lobby l = new Lobby(1l, admin);
        int actual = l.getLobbySize();
        int expected = 1;
        assertEquals(actual, expected);
    }

    @Test
    void testGetAlivePlayers(){
        Player admin = new Player(12l, "admin");
        Player p = new Player(13l, "player");
        Lobby l = new Lobby(1l, admin);
        l.addPlayer(p);
        List<Player> actual = l.getAlivePlayers();
        List<Player> expected = Arrays.asList(p, admin);
        assertThat("List equality without order", actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void testInstantiateRoles(){
        Player admin = new Player(12l, "admin");
        Player p1 = new Player(13l, "player1");
        Player p2 = new Player(14l, "player2");
        Lobby l = new Lobby(1l, admin);
        l.addPlayer(p1);
        l.addPlayer(p2);
        l.instantiateRoles(mock(Game.class));
        Collection<Role> roles = l.getRoles();

        for (Player player : l.getPlayers()) {
            assertThat("Player is assigned a role", !l.getRolesOfPlayer(player).isEmpty());
            for (Role role : l.getRolesOfPlayer(player)) {
                assertTrue(roles.contains(role));
            }
        }
    }

    @Test
    void testShufflePlayer(){
        Player admin = new Player(12l, "admin");
        Player p1 = new Player(13l, "player1");
        Player p2 = new Player(14l, "player2");
        Player p3 = new Player(15l, "player3");
        Lobby l = new Lobby(1l, admin);
        l.addPlayer(p1);
        l.addPlayer(p2);
        l.addPlayer(p3);

        ArrayList<Player> actual = l.shufflePlayers();
        List<Player> expected = Arrays.asList(p1,p2,p3,admin);
        assertThat("List equality without order", actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void testDissolveLobby() {
        LobbyObserver observer = mock(LobbyObserver.class);
        Lobby l = new Lobby(1l, mock(Player.class));
        l.addObserver(observer);
        l.dissolve();
        verify(observer).onLobbyDissolved(l);
    }
}
