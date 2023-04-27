package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {
    public static final String USERAUTH_HEADER = "token";

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    // TODO split up this method & call methods in controller
    public User createUser(User newUser) {
        validateUsername(newUser);
        validatePassword(newUser);
        validateUsernameNotExists(newUser);
        newUser.setToken(UUID.randomUUID().toString());

        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User loginUser(User userToLogin) throws ResponseStatusException{
        User userByUsername = userRepository.findByUsername(userToLogin.getUsername());
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("The user %s is not yet registered. Please first sign up before trying to log in.", userToLogin.getUsername()));
        }
        else if (!Objects.equals(userByUsername.getPassword(), userToLogin.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The password provided is not correct.");
        }
        else {
            userByUsername.setToken(UUID.randomUUID().toString());
            userRepository.save(userByUsername);
            userRepository.flush();
            return userByUsername;
        }
    }

    public User getUser(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            System.out.println("MS1");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with userId %d does not exist.", id));
        }
        return user.get();
    }

    public User getUserByToken(String token) {
        validateToken(token);
        return userRepository.findByToken(token);
    }

    public void updateUser(User updatedUser, Long id) throws ParseException {
        User userById = getUser(id);

        if (updatedUser.getUsername() != null && !userById.getUsername().equals(updatedUser.getUsername())){
            validateUsername(updatedUser);
            validateUsernameNotExists(updatedUser);
            userById.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !userById.getPassword().equals(updatedUser.getPassword())){
            validatePassword(updatedUser);
            userById.setPassword(updatedUser.getPassword());
        }

        userRepository.save(userById);
        userRepository.flush();
    }

    public void validateToken(String token){
        User userByToken = userRepository.findByToken(token);
        if (userByToken == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. User with this token does not exist.");
        }
    }

    public void validateTokenMatch(User user, String token){
        if (!(Objects.equals(user.getToken(), token))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. User token does not match with user.");
        }
    }

    public void validateUsernameNotExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        String baseErrorMessage = "Sorry, there already exists a User with username %s";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, userToBeCreated.getUsername()));
        }
    }

    public boolean validateUserIsPlayer(User user, Player player){
        if(Objects.equals(user.getId(), player.getId())){
            return true;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "User does not match to the player.");
    }

    public void validateUsername(User user) {
        if (user.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please set username field");
        }
        if (user.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a username with at least 1 symbol.");
        }
        if (user.getUsername().length() > 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a username that has 16 or less symbols.");
        }
    }

    public void validatePassword(User user) {
        if (user.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please set password field");
        }
        if (user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a password with at least 1 symbol.");
        }
        if (user.getPassword().length() > 36) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a password that has 36 or less symbols.");
        }
    }
}