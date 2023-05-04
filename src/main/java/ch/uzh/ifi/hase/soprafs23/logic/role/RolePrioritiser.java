package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;

public class RolePrioritiser {
    private static List<Class<? extends Role>> priority = List.of(
        Mayor.class,
        Villager.class,
        Werewolf.class
    );

    public static int getPriority(Role role) {
        assert priority.contains(role.getClass());
        return priority.indexOf(role.getClass());
    }
}
