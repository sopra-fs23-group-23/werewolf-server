package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.function.BiConsumer;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class AddPlayerToRolePollCommand extends PollCommand{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Class<? extends Role> roleClass;

    public AddPlayerToRolePollCommand(BiConsumer<Player, Class<? extends Role>> addPlayerToRole, Player player, Class<? extends Role> roleClass) {
        super(player);
        this.addPlayerToRole = addPlayerToRole;
        this.roleClass = roleClass;
    }

    @Override
    public void execute() {
        super.execute();
        addPlayerToRole.accept(getAffectedPlayer(), roleClass);
    }

    @Override
    public String toString() {
        return String.format("%s is now a %s", getAffectedPlayer().getName(), roleClass.getSimpleName());
    }
    
}
