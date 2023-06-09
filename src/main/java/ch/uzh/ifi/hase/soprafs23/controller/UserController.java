package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserAuthDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserAuthDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOToEntity(userPostDTO);

        User createdUser = userService.createUser(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserAuthDTO(createdUser);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getSingleUser(@PathVariable("id") String id, @RequestHeader("token") String token){
        userService.validateToken(token);

        User user = userService.getUser(Long.parseLong(id));

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserData(@PathVariable("id") String id, @RequestBody UserPostDTO userPostDTO, @RequestHeader("token") String token) throws ParseException {
        User user = userService.getUser(Long.parseLong(id));

        userService.validateTokenMatch(user, token);

        User updatedUser = DTOMapper.INSTANCE.convertUserPostDTOToEntity(userPostDTO);
        userService.updateUser(updatedUser, Long.parseLong(id));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserAuthDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOToEntity(userPostDTO);
        // call logIn method
        User user = userService.loginUser(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserAuthDTO(user);
    }

}