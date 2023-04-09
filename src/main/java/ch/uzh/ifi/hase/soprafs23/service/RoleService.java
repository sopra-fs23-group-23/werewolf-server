package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

import static ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper.convertRoleToRoleGetDTO;

@Service
@Transactional
public class RoleService {

    public Collection<RoleGetDTO> getAllRoleInformation (Lobby lobby) {
        ArrayList<Role> roles = new ArrayList<>(lobby.getRoles());
        ArrayList<RoleGetDTO> roleGetDTOS = new ArrayList<>();
        for (Role role : roles) {
            roleGetDTOS.add(roleToRoleGetDTO(lobby, role));
        }
        return roleGetDTOS;
    }

    public Collection<RoleGetDTO> getOwnRoleInformation (Player player, Lobby lobby) {
        ArrayList<Role> roles = new ArrayList<>(lobby.getRolesOfPlayer(player));
        ArrayList<RoleGetDTO> roleGetDTOS = new ArrayList<>();
        for (Role role : roles) {
            roleGetDTOS.add(roleToRoleGetDTO(lobby, role));
        }
        return roleGetDTOS;
    }

    public void assignRoles(User user, Lobby lobby) {
        if (!user.getId().equals(lobby.getAdmin().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the admin can trigger the role assignment");
        }
        lobby.instantiateRoles();
    }

    private RoleGetDTO roleToRoleGetDTO(Lobby lobby, Role role) {
        Iterable<Player> playersOfThatRole = lobby.getPlayersByRole(role.getClass());
        int amount = 0;
        for(Player player: playersOfThatRole) {
            amount++;
        }
        return convertRoleToRoleGetDTO(role, amount);
    }
}
