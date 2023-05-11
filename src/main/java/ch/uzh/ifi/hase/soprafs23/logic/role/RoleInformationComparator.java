package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.Comparator;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Cupid;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Hunter;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Lover;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Seer;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Witch;
// TODO TEST
public class RoleInformationComparator implements Comparator<Role>{
    private static final List<Class<? extends Role>> roleInformationOrder = List.of(
        Cupid.class,
        Hunter.class,
        Witch.class,
        Seer.class,
        Werewolf.class,
        Villager.class,
        Lover.class,
        Mayor.class
    );

    private int getRoleInformationOrderIndex(Role role) {
        return roleInformationOrder.indexOf(role.getClass());
    }

    @Override
    public int compare(Role o1, Role o2) {
        return getRoleInformationOrderIndex(o1) - getRoleInformationOrderIndex(o2);
    }
}
