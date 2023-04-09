package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.RoleService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class RoleController {
    private final RoleService roleService;
    private final LobbyService lobbyService;
    private final UserService userService;

    public RoleController(RoleService roleService, LobbyService lobbyService, UserService userService) {
        this.roleService = roleService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @GetMapping("/lobbies/{lobbyId}/roles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<RoleGetDTO> getAllRoles(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("token") String token) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return roleService.getAllRoleInformation(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/roles/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<RoleGetDTO> getOwnRole(@PathVariable("lobbyId") Long LobbyId, @PathVariable("uid") Long userId,
                                             @RequestHeader("token") String token) {
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        User user = userService.getUserByToken(token);
        userService.validateTokenMatch(user, token);
        lobbyService.validateUserIsInLobby(user, lobby);
        Player player = lobbyService.getPlayerByUser(user, lobby);
        return roleService.getOwnRoleInformation(player, lobby);
    }

    @PutMapping("/lobbies/{lobbyId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void assignRoles(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("token") String token) {
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        User user = userService.getUserByToken(token);
        roleService.assignRoles(user, lobby);
    }
}
