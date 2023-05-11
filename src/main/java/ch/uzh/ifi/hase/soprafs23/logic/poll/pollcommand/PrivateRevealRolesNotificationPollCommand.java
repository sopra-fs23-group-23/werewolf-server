package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class PrivateRevealRolesNotificationPollCommand extends PrivatePollCommand {
    private final Function<Player, Collection<Role>> rolesPerPlayer;
    private Collection<Role> rolesOfPlayer = new ArrayList<>();

    public PrivateRevealRolesNotificationPollCommand(Player affectedPlayer, Player informationOwner, Function<Player, Collection<Role>> rolesPerPlayer) {
        super(affectedPlayer, informationOwner);
        this.rolesPerPlayer = rolesPerPlayer;
    }

    @Override
    public void execute() {
        super.execute();
        rolesOfPlayer = rolesPerPlayer.apply(getAffectedPlayer());
    }

    @Override
    public String toString() {
        return "[" + rolesOfPlayer.stream().map(Role::getName).collect(Collectors.joining(", ")) + "]";
    }
    
}
