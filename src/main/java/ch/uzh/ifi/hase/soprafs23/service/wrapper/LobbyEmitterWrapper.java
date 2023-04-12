package ch.uzh.ifi.hase.soprafs23.service.wrapper;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class LobbyEmitterWrapper {
    private SseEmitter emitter;
    private String token;

    public LobbyEmitterWrapper(SseEmitter emitter, String token) {
        this.emitter = emitter;
        this.token = token;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }

    public String getToken() {
        return token;
    }   
}