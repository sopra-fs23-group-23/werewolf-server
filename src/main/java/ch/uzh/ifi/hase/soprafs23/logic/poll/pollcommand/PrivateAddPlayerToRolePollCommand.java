package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class PrivateAddPlayerToRolePollCommand extends PrivatePollCommand{
    private final AddPlayerToRolePollCommand addPlayerToRolePollCommand;

    public PrivateAddPlayerToRolePollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole,
            Player player, Class<? extends Role> roleClass, Player owner) {
        super(player, owner);
        addPlayerToRolePollCommand = new AddPlayerToRolePollCommand(addPlayerToRole, player, roleClass);
    }

    @Override
    public void execute() {
        super.execute();
        addPlayerToRolePollCommand.execute();
    }

    @Override
    public String toString() {
        return addPlayerToRolePollCommand.toString();
    }
    
}
