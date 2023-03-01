package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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
    private final UserRepository userRepository;

    UserController(UserService userService,
                   UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserDTO> userDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userDTOs.add(DTOMapper.INSTANCE.convertEntityToUserDTO(user));
        }
        return userDTOs;
    }
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getSingleUser(@PathVariable("id") String id){
        User user = userRepository.findById(Long.parseLong(id));
        if (user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id: %s could not be found."));
        }
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserDTO(createdUser);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // call logIn method
        User user = userService.loginUser(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logoutUser(@PathVariable("id") String id) {
        userService.logoutUser(Long.parseLong(id));
    }
}

/*  TODO:
 *      -create logout where user status is set to OFFLINE
 *      -
 */

