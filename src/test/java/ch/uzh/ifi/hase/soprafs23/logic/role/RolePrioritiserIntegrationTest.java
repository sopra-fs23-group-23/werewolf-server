package ch.uzh.ifi.hase.soprafs23.logic.role;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.StageVoter;

public class RolePrioritiserIntegrationTest {
    

    @Test
    void testAllRolesInPriorityList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Player admin = mock(Player.class);
        Lobby lobby = new Lobby(1l, admin);
        lobby.instantiateRoles(null, null, null, null, null);
        Collection<Role> roles = lobby.getRoles();

        Field priority = RolePrioritiser.class.getDeclaredField("priority");
        priority.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Class<? extends Role>> priorityList = (List<Class<? extends Role>>) priority.get(null);
        roles = roles.stream().filter(role -> role instanceof StageVoter).toList();
        for (Role role : roles) {
            assertTrue(priorityList.contains(role.getClass()), "Role " + role.getClass().getSimpleName() + " is not in priority list");
        }
    }
}
