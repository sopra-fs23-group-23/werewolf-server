package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/lobbies/{lobbyId}/roles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<RoleGetDTO> getAllRoles(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId)
    {
        return new ArrayList<>();
    }

    @GetMapping("/lobbies/{lobbyId}/roles/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoleGetDTO getOwnRole(@PathVariable("lobbyId") Long LobbyId, @PathVariable("uid") Long userId)
    {
        return new RoleGetDTO();
    }

    @PutMapping("/lobbies/{lobbyId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void assignRoles(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId)
    {

    }
}
