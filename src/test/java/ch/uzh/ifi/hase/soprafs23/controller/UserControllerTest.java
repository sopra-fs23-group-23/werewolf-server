
package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setPassword("password");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("12345");
    user.setCreationDate("20.03.2023");


    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
      given(userRepository.findByToken(Mockito.any())).willReturn(user);
    given(userService.getUsers()).willReturn(allUsers);


    // when
    MockHttpServletRequestBuilder getRequest = get("/users")
            .header("token", "12345")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setPassword("password");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("password");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.password", is(user.getPassword())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_duplicateUser_throwsError() throws Exception{
      // given
      User user = new User();
      user.setUsername("username");
      user.setPassword("password");
      user.setId(1L);
      user.setStatus(UserStatus.ONLINE);

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setPassword("password");
      userPostDTO.setUsername("username");

      given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, String.format("The %s provided %s not unique. Therefore, the user could not be created!", "username", "is")));

      // do request and validate result
      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isConflict());
  }

  @Test
  public void getSingleUser_success_returnJsonArray() throws Exception{
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("username");
      user.setCreationDate("20.04.1999");
      user.setStatus(UserStatus.ONLINE);
      user.setBirthday("20.04.1999");
      user.setPassword("password");
      user.setToken("12345");

      Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

      //given(userRepository.findById(Mockito.any())).willReturn(user);
      given(userRepository.findByToken(Mockito.any())).willReturn(user);


      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder getRequest = get("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345");

      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(user.getId().intValue())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.creation_date", is(user.getCreationDate())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
              .andExpect(jsonPath("$.birthday", is(user.getBirthday())));
  }

  @Test
  public void getSingleUser_throwResponseStatusException() throws Exception {

      given(userRepository.findByToken(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be found."));

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder getRequest = get("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345");
      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isBadRequest());
  }

  @Test
  public void updateUser_success() throws Exception{
      //given
/*      User user = new User();
      user.setId(1L);
      user.setUsername("username");
      user.setCreationDate("20.04.1999");
      user.setStatus(UserStatus.ONLINE);
      user.setBirthday("20.04.2000");
      user.setPassword("password");
      user.setToken("12345");

      given(userRepository.save(Mockito.any())).willReturn(user);*/

      UserPutDTO userPutDTO = new UserPutDTO();
      userPutDTO.setUsername("username2");
      userPutDTO.setBirthday("20.04.1999");

      // TODO HTTP 400 Bad Request instead of 204

      MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345")
              .content(asJsonString(userPutDTO));

      mockMvc.perform(putRequest)
              .andExpect(status().isNoContent());
  }
    @Test
    public void updateUser_throwError() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("username");
        userPutDTO.setBirthday("20.04.1999");

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found.")).when(userService).updateUser(Mockito.any(), Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "12345")
                .content(asJsonString(userPutDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
  }

/**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}
