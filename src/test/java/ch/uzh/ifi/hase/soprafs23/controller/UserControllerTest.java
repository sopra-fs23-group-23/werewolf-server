
package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
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

  private User createBasicUser(){
      User user = new User();
      user.setId(1L);
      user.setUsername("username");
      user.setPassword("password");
      user.setToken("12345");
      return user;
  }

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = createBasicUser();

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    @Test
    public void createUser_duplicateUser_throwsError() throws Exception{
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("password");
        userPostDTO.setUsername("username");

        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Sorry, there already exists a User with username %s"));

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
      User user = createBasicUser();
      Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);


      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder getRequest = get("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345");

      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(user.getId().intValue())))
              .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  @Test
  public void getSingleUser_throwResponseStatusException() throws Exception {

      given(userService.getUser(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found."));

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder getRequest = get("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345");
      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isNotFound());
  }

  @Test
  public void updateUser_success() throws Exception{
      //given
      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setUsername("Willy");
      userPostDTO.setPassword("1818");

      //when
      MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/" + 1)
              .contentType(MediaType.APPLICATION_JSON)
              .header("token", "12345")
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(putRequest)
              .andExpect(status().isNoContent());
  }

    @Test
    public void loginUser_success() throws Exception{
        // given
        User user = createBasicUser();

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");

        given(userService.loginUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}
