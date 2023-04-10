package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        checkIfUserExists(newUser);
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

        if (!userById.getUsername().equals(updatedUser.getUsername())){
            checkIfUserExists(updatedUser);
            userById.setUsername(updatedUser.getUsername());
        }

        if (!userById.getPassword().equals(updatedUser.getPassword())){
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

    private void checkIfUserExists(User userToBeCreated) {
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
}