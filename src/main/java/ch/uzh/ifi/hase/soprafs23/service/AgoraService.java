package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

// HTTP basic authentication example in Java using the <Vg k="VSDK" /> Server RESTful API

@Service
@Transactional
public class AgoraService {

    private final String authorizationHeader = "Basic MTVhNjhhYzliNjU1NDI3ZDk0YTQ4MjNiZTI5MDFhM2Q6Yjg5MmU5M2M4NjVkNDZhZGI3NzBiN2M1YmUwMjE0N2Y=";

    private final String appId = "348d6a205d75436e916896366c5e315c";

    public void kickVillager(String playerId) throws IOException, InterruptedException{

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("appid", appId);
        requestBodyMap.put("uid", playerId); // Placeholder for the dynamic value
        requestBodyMap.put("time_in_seconds", 10);
        List<String> privileges = Arrays.asList("join_channel");
        requestBodyMap.put("privileges", privileges);
        requestBodyMap.put("reason", 1);

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }

    public void muteDeadPlayer(String playerId) throws IOException, InterruptedException{

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("appid", appId);
        requestBodyMap.put("uid", playerId); // Placeholder for the dynamic value
        requestBodyMap.put("time_in_seconds", 10);
        List<String> privileges = Arrays.asList("publish_audio");
        requestBodyMap.put("privileges", privileges);
        requestBodyMap.put("reason", 2);

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }

    public void muteTroll(String playerId) throws IOException, InterruptedException{

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("appid", appId);
        requestBodyMap.put("uid", playerId); // Placeholder for the dynamic value
        requestBodyMap.put("time_in_seconds", 10);
        List<String> privileges = Arrays.asList("publish_audio");
        requestBodyMap.put("privileges", privileges);
        requestBodyMap.put("reason", 3);

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.headers());
    }



    public void kickAll(String cname) throws IOException, InterruptedException{

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("appid", appId);
        requestBodyMap.put("cname", cname); // Placeholder for the dynamic value
        requestBodyMap.put("time", 120);
        List<String> privileges = Arrays.asList("join_channel");
        requestBodyMap.put("privileges", privileges);
        requestBodyMap.put("reason", 4);

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

    }
}
