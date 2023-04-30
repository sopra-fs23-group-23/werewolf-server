package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;

public class AddPlayerToRolePollCommandTest {
    private class MockPlayerAdder {
        private boolean called = false;
        private Class<? extends Role> expectedRoleClass;
        private Player expectedPlayer;

        public MockPlayerAdder(Class<? extends Role> expectedRoleClass, Player expectedPlayer) {
            this.expectedRoleClass = expectedRoleClass;
            this.expectedPlayer = expectedPlayer;
        }

        public boolean isCalled() {
            return called;
        }

        public void verifyPlayerWasAdded(Player player, Class<? extends Role> roleClass) {
            called = true;
            assertEquals(expectedRoleClass, roleClass);
            assertEquals(expectedPlayer, player);
        }
    }

    @Test
    void testExecute() {
        Player player = mock(Player.class);
        Class<? extends Role> roleClass = Mayor.class;
        MockPlayerAdder mockPlayerAdder = new MockPlayerAdder(roleClass, player);
        AddPlayerToRolePollCommand addPlayerToRolePollCommand = new AddPlayerToRolePollCommand(mockPlayerAdder::verifyPlayerWasAdded, player, roleClass);
        addPlayerToRolePollCommand.execute();
        assertTrue(mockPlayerAdder.isCalled());

    }
}
