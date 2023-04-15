package ch.uzh.ifi.hase.soprafs23.service.wrapper;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class EmitterWrapper {
    private SseEmitter emitter;
    private String token;

    public EmitterWrapper(SseEmitter emitter, String token) {
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