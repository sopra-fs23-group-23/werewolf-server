package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class AddPlayerToRolePollCommand implements PollCommand{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Player player;
    private Class<? extends Role> roleClass;

    public AddPlayerToRolePollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole, Player player, Class<? extends Role> roleClass) {
        this.addPlayerToRole = addPlayerToRole;
        this.player = player;
        this.roleClass = roleClass;
    }

    @Override
    public void execute() {
        addPlayerToRole.accept(player, roleClass);
    }

    @Override
    public String toString() {
        return String.format("%s is now a %s", player.getName(), roleClass.getSimpleName());
    }

    @Override
    public Player getAffectedPlayer() {
        return player;
    }
    
}
