package ch.uzh.ifi.hase.soprafs23.logic.role;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Lover;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;

public class FractionRoleTest {
    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected.stream().filter(Player::isAlive).toList();
            }
            
        };
    }

    private Player createMockPlayer() {
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(true);
        return player;
    }

    private List<Player> getAlivePlayers() {
        return List.of(
            createMockPlayer(),
            createMockPlayer(),
            createMockPlayer(),
            createMockPlayer()
        );
    }

    @Test
    void testHasWon() {
        List<Player> expected = getAlivePlayers();
        Villager villager = new Villager(0, null, createMockAlivePlayersGetter(expected), mock(TiedPollDecider.class));
        villager.addPlayer(expected.get(0));
        villager.addPlayer(expected.get(1));
        villager.addPlayer(expected.get(3));
        assertFalse(villager.hasWon());
        when(expected.get(2).isAlive()).thenReturn(false);
        assertTrue(villager.hasWon());
    }

    @Test
    void testHasWon_multipleFractions() {
        List<Player> expected = getAlivePlayers();
        Villager villager = new Villager(0, null, createMockAlivePlayersGetter(expected), mock(TiedPollDecider.class));
        Werewolf werewolf = new Werewolf(0, createMockAlivePlayersGetter(expected));
        Lover lover = new Lover(createMockAlivePlayersGetter(expected), null);
        villager.addPlayer(expected.get(0));
        villager.addPlayer(expected.get(1));
        werewolf.addPlayer(expected.get(2));
        werewolf.addPlayer(expected.get(3));
        lover.addPlayer(expected.get(1));
        lover.addPlayer(expected.get(2));

        assertFalse(villager.hasWon());
        when(expected.get(0).isAlive()).thenReturn(false);
        when(expected.get(3).isAlive()).thenReturn(false);
        assertTrue(lover.hasWon());
    }
}
