package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class AddPlayerToRolePollCommand implements PollCommand{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Player player;

    public AddPlayerToRolePollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole, Player player) {
        this.addPlayerToRole = addPlayerToRole;
        this.player = player;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
