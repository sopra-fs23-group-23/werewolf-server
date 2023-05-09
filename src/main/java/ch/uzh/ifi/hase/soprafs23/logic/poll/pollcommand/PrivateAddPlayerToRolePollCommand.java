package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class PrivateAddPlayerToRolePollCommand extends AddPlayerToRolePollCommand implements PrivatePollCommand{
    private Player owner;

    public PrivateAddPlayerToRolePollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole,
            Player player, Class<? extends Role> roleClass, Player owner) {
        super(addPlayerToRole, player, roleClass);
        this.owner = owner;
    }

    @Override
    public Player getInformationOwner() {
        return owner;
    }
    
}
