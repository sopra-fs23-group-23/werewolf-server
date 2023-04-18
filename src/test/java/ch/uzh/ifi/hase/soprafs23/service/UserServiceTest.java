
package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("password");
    testUser.setUsername("testUsername");

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    User createdUser = userService.createUser(testUser);

    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
  }

  @Test
  public void createUser_duplicateName_throwsException() {
    userService.createUser(testUser);

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void loginUser_validInputs_success() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

      User loggedInUser = userService.loginUser(testUser);

      assertEquals(testUser.getId(), loggedInUser.getId());
      assertEquals(testUser.getPassword(), loggedInUser.getPassword());
      assertEquals(testUser.getUsername(), loggedInUser.getUsername());
      assertNotNull(loggedInUser.getToken());
  }

  @Test
  public void loginUser_UserNotFound_throwsException() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser));
      assertEquals(HttpStatus.CONFLICT, exception.getStatus());
  }

  @Test
  public void loginUser_Unauthorized_throwsException() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      User diffUser = new User();
      diffUser.setUsername("Richi");
      diffUser.setPassword("irgendöppis");

      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loginUser(diffUser));
      assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
  }

    @Test
    public void getUser_validInputs_success() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        User getUser = userService.getUser(testUser.getId());

        assertEquals(testUser.getId(), getUser.getId());
        assertEquals(testUser.getPassword(), getUser.getPassword());
        assertEquals(testUser.getUsername(), getUser.getUsername());
    }

    @Test
    public void updateUser_ValidUpdate_Success() throws ParseException {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(null);

        userService.updateUser(updatedUser, userId);

        // Verify that user was updated
        Mockito.verify(userRepository).save(existingUser);
        Mockito.verify(userRepository).flush();
        assertEquals(existingUser.getUsername(), updatedUser.getUsername());
        assertEquals(existingUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void updateUser_UsernameAlreadyExists_ThrowsException() throws ParseException {
        // Set up
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(existingUser);

        // Call function and verify that it throws an exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUser(updatedUser, userId));
        assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
    }

    @Test
    public void validateToken_validInputs_success() {
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);

        userService.validateToken("1234");
    }

    @Test
    public void
    validateToken_UserNotFound_throwsException() {
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser));
    }

    @Test
    public void validateTokenMatch_validInputs_success() {
        testUser.setToken("12345");
        userService.validateTokenMatch(testUser, "12345");
    }

    @Test
    public void validateTokenMatch_NoMatch_throwsException() {
        testUser.setToken("12345");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.validateTokenMatch(testUser, "1234"));
    }

    @Test
    public void checkIfUserExists_NoUser_success() throws Exception {
        Method method = UserService.class.getDeclaredMethod("checkIfUserExists", User.class);
        method.setAccessible(true);

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        method.invoke(userService, testUser);
    }





}

