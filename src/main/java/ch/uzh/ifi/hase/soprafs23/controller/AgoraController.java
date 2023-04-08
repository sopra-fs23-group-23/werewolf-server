package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

public class AgoraController {

    private final AgoraService agoraService;
    public AgoraController(AgoraService agoraService){
        this.agoraService = agoraService;
    }

    @PostMapping("/rules/audio/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void forceMutePlayerFromChannel(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
        agoraService.muteTroll(userId.toString());
    }
}
