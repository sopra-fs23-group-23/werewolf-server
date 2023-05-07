package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class AgoraServiceTest {

    AgoraService agoraService = new AgoraService();

    private User createTestUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
    @Test
    void testGetVoiceChannelToken() throws Exception{
        User user = createTestUser(1l, "test");
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(user));
        String token = agoraService.createVoiceChannelToken(lobby, user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
