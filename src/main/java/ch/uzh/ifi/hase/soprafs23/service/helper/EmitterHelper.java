package ch.uzh.ifi.hase.soprafs23.service.helper;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

public final class EmitterHelper {
    private EmitterHelper() {}

    public static void sendEmitterUpdate(SseEmitter emitter, String data, String eventName) {
        // ordering matters!!! .name needs to be before .data
        SseEventBuilder event = SseEmitter.event()
            .name(eventName)
            .data( data + "\n", MediaType.APPLICATION_JSON)
            .id(UUID.randomUUID().toString());
        try {
            emitter.send(event);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
