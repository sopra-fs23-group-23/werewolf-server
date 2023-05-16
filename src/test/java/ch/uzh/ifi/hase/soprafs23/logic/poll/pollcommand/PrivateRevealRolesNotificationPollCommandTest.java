package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Lover;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Witch;

public class PrivateRevealRolesNotificationPollCommandTest {

    private class MockFunctions {
        private Player expectedPlayer;

        public MockFunctions(Player expectedPlayer) {
            this.expectedPlayer = expectedPlayer;
        }

        private List<Role> mockRolesPerPlayerConsumer(Player player) {
            assertEquals(expectedPlayer, player);
            return List.of(
                new Villager(0, null, null, null),
                new Witch(0, null, null, null),
                new Lover(null, null)
            );
        }
    }

    @Test
    void testExecuteAndToString() {
        Player player = mock(Player.class);
        MockFunctions mockFunctions = new MockFunctions(player);
        PrivateRevealRolesNotificationPollCommand privateRevealRolesNotificationPollCommand = new PrivateRevealRolesNotificationPollCommand(player, null, mockFunctions::mockRolesPerPlayerConsumer);
        privateRevealRolesNotificationPollCommand.execute();
        assertEquals("[Witch, Villager, Lover]", privateRevealRolesNotificationPollCommand.toString());
    }
}
