package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
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
import java.text.SimpleDateFormat;

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
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = formatter.format(date);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(strDate);
        checkIfUserExists(newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User loginUser(User userToLogin) {
        User userByUsername = userRepository.findByUsername(userToLogin.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("The user %s is not yet registered. Please first sign up before trying to log in.", userToLogin.getUsername()));
        }
        else if (!Objects.equals(userByUsername.getPassword(), userToLogin.getPassword())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The password provided is not correct.");
        }
        else {
            userByUsername.setToken(UUID.randomUUID().toString());
            userByUsername.setStatus(UserStatus.ONLINE);
            userRepository.save(userByUsername);
            userRepository.flush();
            return userByUsername;
        }
    }

    public void logoutUser(Long id) {
        User userById = userRepository.findById(id);
        userById.setStatus(UserStatus.OFFLINE);
        userRepository.save(userById);
    }

    public void getUserByToken(String token){
        User userByToken = userRepository.findByToken(token);
        if (userByToken == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized.");
        }
    }

    public User getUserById(Long id){
        User userById = userRepository.findById(id);
        if (userById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found.");
        }
        return userById;
    }

    public void validateTokenMatch(User user, String token){
        if (!(Objects.equals(user.getToken(), token))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. User token is not valid.");
        }
    }

    public void updateUser(User updatedUser, Long id) throws ParseException {
        User userById = getUserById(id);
        User userByUsername = userRepository.findByUsername(updatedUser.getUsername());

        // check if username actually changed
        if (!(Objects.equals(userById.getUsername(), updatedUser.getUsername()))){
            if(!(userByUsername == null)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("User with username %s already exists, please choose a different name.", updatedUser.getUsername()));
            }else{
                userById.setUsername(updatedUser.getUsername());
            }
        }
        // check if birthday changed
        if  (!(Objects.equals(userById.getBirthday(), updatedUser.getBirthday())) && updatedUser.getBirthday() != null) {
            final String OLD_FORMAT = "yyyy-MM-dd";
            final String NEW_FORMAT = "dd.MM.yyyy";
            String oldDateString = updatedUser.getBirthday();
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            String newDateString = sdf.format(d);
            userById.setBirthday(newDateString);
        }
        userRepository.save(userById);
        userRepository.flush();
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }
}