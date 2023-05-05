package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class AddPlayerToRoleInstantPollCommand implements InstantPollCommand{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Player player;
    private Class<? extends Role> roleClass;

    public AddPlayerToRoleInstantPollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole, Player player, Class<? extends Role> roleClass) {
        this.addPlayerToRole = addPlayerToRole;
        this.player = player;
        this.roleClass = roleClass;
    }

    @Override
    public void execute_instantly() {
        addPlayerToRole.accept(player, roleClass);
    }

    @Override
    public String toString() {
        return String.format("%s is now a %s", player.getName(), roleClass.getSimpleName());
    }
    
}
