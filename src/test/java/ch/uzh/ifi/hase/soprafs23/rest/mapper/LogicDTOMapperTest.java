package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.rest.dto.FractionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleWithPlayersGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;

public class LogicDTOMapperTest {
    Lobby mockLobby = mock(Lobby.class);

    @Test
    public void testConvertFractionToFractionGetDTO() {
        FractionRole fraction = new Werewolf(0, mockLobby::getAlivePlayers);

        FractionGetDTO fractionGetDTO = LogicDTOMapper.convertFractionToFractionGetDTO(fraction);

        assertEquals(fractionGetDTO.getWinner(), fraction.getName());
    }

    @Test
    public void testConvertRoleToRoleWithPlayersGetDTO() {
        Role role = new Werewolf(0, mockLobby::getAlivePlayers);
        Player player = new Player(1L, "TestPlayer");

        role.addPlayer(player);

        RoleWithPlayersGetDTO roleWithPlayersGetDTO = LogicDTOMapper.convertRoleToRoleWithPlayersGetDTO(role);

        assertEquals(roleWithPlayersGetDTO.getRole().getRoleName(), role.getName());
        assertEquals(roleWithPlayersGetDTO.getRole().getAmount(), role.getPlayers().size());
        assertEquals(roleWithPlayersGetDTO.getRole().getDescription(), role.getDescription());
        assertEquals(roleWithPlayersGetDTO.getPlayers().get(0).getId(), role.getPlayers().get(0).getId());
    }
}
