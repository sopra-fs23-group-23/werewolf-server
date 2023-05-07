package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.agora.RTCTokenBuilder;
import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Service
public class AgoraService {

    //creates Token for VoiceChannel. Same procedure for admin and normal players, admin's token generation automatically creates agora channel
    public String createVoiceChannelToken(Lobby lobby, User user) {
        RTCTokenBuilder newToken = new RTCTokenBuilder();
        String token = newToken.buildTokenWithUserAccount(lobby.getId().toString(), user.getId().toString(), VoiceChatRole.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }
}
