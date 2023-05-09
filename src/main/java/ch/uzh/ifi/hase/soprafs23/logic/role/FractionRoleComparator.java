package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.Comparator;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Lover;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;

public class FractionRoleComparator implements Comparator<FractionRole>{
    private static final List<Class<? extends FractionRole>> winCheckOrder = List.of(
        Lover.class,
        Werewolf.class,
        Villager.class
    );

    private int getWinCheckIndex(FractionRole role) {
        return winCheckOrder.indexOf(role.getClass());
    }

    @Override
    public int compare(FractionRole o1, FractionRole o2) {
        return getWinCheckIndex(o1) - getWinCheckIndex(o2);
    }


    
}
