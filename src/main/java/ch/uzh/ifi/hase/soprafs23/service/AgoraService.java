package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.agora.RTCTokenBuilder;
import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static javax.xml.bind.DatatypeConverter.parseString;

// HTTP basic authentication example in Java using the <Vg k="VSDK" /> Server RESTful API

@Service
@Transactional
public class AgoraService {

    private final String authorizationHeader = "Basic MTVhNjhhYzliNjU1NDI3ZDk0YTQ4MjNiZTI5MDFhM2Q6Yjg5MmU5M2M4NjVkNDZhZGI3NzBiN2M1YmUwMjE0N2Y=";

    private final String appId = "348d6a205d75436e916896366c5e315c";

    private String createRequestBody(Optional<Player> player, Optional<String> cname, String privilege, Reason reason) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("appid", appId);
        player.ifPresent(value -> requestBodyMap.put("uid", value.getId()));
        cname.ifPresent(s -> requestBodyMap.put("cname", s));
        requestBodyMap.put("time", 120);
        List<String> privileges = Arrays.asList(privilege);
        requestBodyMap.put("privileges", privileges);
        requestBodyMap.put("reason", (reason.ordinal() + 1));
        System.out.println("Reason: " + (reason.ordinal() + 1));

        return objectMapper.writeValueAsString(requestBodyMap);
    }

    JsonNode createHttpRequest(HttpMethod method, String requestBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request builder object
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader);

        if (method == HttpMethod.GET) {
            requestBuilder.uri(URI.create("https://api.agora.io/dev/v1/kicking-rule?appid=" + appId));
            requestBuilder.GET();
        } else if (method == HttpMethod.DELETE) {
            HttpRequest.BodyPublisher requestBodyPublisher = HttpRequest.BodyPublishers.ofString(requestBody);
            requestBuilder.method("DELETE", requestBodyPublisher);
        } else if (method == HttpMethod.POST){
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
        } else {
            throw new IllegalArgumentException("HTTP method is not allowed " + method);
        }
        HttpRequest request = requestBuilder.build();

        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readTree(response.body()));
        return objectMapper.readTree(response.body());
    }

    //deletes rules that were made by certain reasons
    //Optional Player can be added to just delete a rule concerning one player
    public void deleteRules(Reason reason, Optional<Player> player) throws IOException, InterruptedException {
        JsonNode allRules = createHttpRequest(HttpMethod.GET, "").get("rules");
        List<JsonNode> reasonRules = StreamSupport.stream(allRules.spliterator(), false)
                                            .filter(r -> r.get("reason").asInt() == reason.ordinal() + 1)
                                            .map(JsonNode.class::cast)
                                            .collect(Collectors.toList());

        if (player.isPresent()){
            reasonRules = StreamSupport.stream(reasonRules.spliterator(), false)
                    .filter(r -> r.get("uid").asInt() == player.get().getId())
                    .map(JsonNode.class::cast)
                    .collect(Collectors.toList());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        for (JsonNode rule : reasonRules) {
            System.out.println("Deleting JSON Node " + rule);
            String requestBody = objectMapper.writeValueAsString(Map.of("appid", appId, "id", String.valueOf(rule.get("id"))));
            createHttpRequest(HttpMethod.DELETE, requestBody);
        }
    }

    public void kickVillager(Player player) throws IOException, InterruptedException{
        String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "join_channel", Reason.KICK_VILLAGER);
        createHttpRequest(HttpMethod.POST, requestBody);
    }

    public void kickAll(String cname) throws IOException, InterruptedException{
        String requestBody = createRequestBody(Optional.empty(), Optional.of(cname), "join_channel", Reason.KICK_ALL);
        createHttpRequest(HttpMethod.POST, requestBody);
    }

    public void muteDeadPlayer(Player player) throws IOException, InterruptedException {
        String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "publish_audio", Reason.MUTE_DEAD);
        createHttpRequest(HttpMethod.POST, requestBody);
    }

    public void muteTroll(Player player) throws IOException, InterruptedException {
        String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "publish_audio", Reason.MUTE_TROLL);
        createHttpRequest(HttpMethod.POST, requestBody);
    }

    public String createVoiceChannelToken(Lobby lobby, User user) {
        RTCTokenBuilder newToken = new RTCTokenBuilder();
        String token = newToken.buildTokenWithUserAccount(lobby.getId().toString(), user.getId().toString(), VoiceChatRole.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }


}
