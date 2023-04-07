package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserAuthDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserAuthDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOToEntity(userPostDTO);
        // create user
        User createdUser = userService.createUser(userInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserAuthDTO(createdUser);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getSingleUser(@PathVariable("id") String id, @RequestHeader("token") String token){
        User user = userService.getUser(Long.parseLong(id));
        userService.getUserByToken(token);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Secured("authenticateUser(header.token)")
    @ResponseBody
    public List<UserAuthDTO> getAllUsers(@RequestHeader("token") String token) {

        // check if a user with this token exists
        userService.getUserByToken(token);

        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserAuthDTO> userDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userDTOs.add(DTOMapper.INSTANCE.convertEntityToUserDTO(user));
        }
        return userDTOs;
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserData(@PathVariable("id") String id, @RequestBody UserPutDTO userPutDTO, @RequestHeader("token") String token) throws ParseException {
        User user = userService.getUser(Long.parseLong(id));

        userService.validateTokenMatch(user, token);

        User updatedUser = DTOMapper.INSTANCE.convertUserPutDTOToEntity(userPutDTO);
        if (updatedUser.getUsername() == null){
            updatedUser.setUsername(user.getUsername());
        }
        userService.updateUser(updatedUser, Long.parseLong(id));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // call logIn method
        User user = userService.loginUser(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

}