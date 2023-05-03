package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

// HTTP basic authentication example in Java using the <Vg k="VSDK" /> Server RESTful API

@Service
@Transactional
public class Agora {

    private static final String authorizationHeader = "Basic MTVhNjhhYzliNjU1NDI3ZDk0YTQ4MjNiZTI5MDFhM2Q6Yjg5MmU5M2M4NjVkNDZhZGI3NzBiN2M1YmUwMjE0N2Y=";

    private static final String appId = "348d6a205d75436e916896366c5e315c";

    //Creates RequestBody for ban rule. Ban is applied to either Optional player or Optional channelname to apply ban to whole channel. State privilege for ban and reason.
    //Privilege is either "join_channel" or "publish_audio"
    private static String createRequestBody(Optional<Player> player, Optional<String> cname, String privilege, Reason reason) throws IOException, InterruptedException {
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

    // Send Http Request by providing requestBody and method (GET, POST, DELETE allowed). Agora Server answer is given back (e.g. all Rules if GET)
    // For GET no requestBody is needed, therefore just pass "" as requestBody
    static JsonNode createHttpRequest(HttpMethod method, String requestBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request builder object
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agora.io/dev/v1/kicking-rule"))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader);

        switch (method) {
            case GET -> {
                requestBuilder.uri(URI.create("https://api.agora.io/dev/v1/kicking-rule?appid=" + appId));
                requestBuilder.GET();
            }
            case DELETE -> {
                HttpRequest.BodyPublisher requestBodyPublisher = HttpRequest.BodyPublishers.ofString(requestBody);
                requestBuilder.method("DELETE", requestBodyPublisher);
            }
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
            default -> throw new IllegalArgumentException("HTTP method is not allowed " + method);
        }
        HttpRequest request = requestBuilder.build();

        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readTree(response.body()));
        return objectMapper.readTree(response.body());
    }

    //deletes rules that match with provided filter for Reason and Player. If no player provided all rules by given Reason are deleted.
    public static void deleteRules(Reason reason, Optional<Player> player) throws IOException, InterruptedException {
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

    //creates "join_channel" ban for Player. Shall be used to kick villagers from channel during night
    public static void kickVillager(Player player){
        try {
        String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "join_channel", Reason.KICK_VILLAGER);
        createHttpRequest(HttpMethod.POST, requestBody);
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't kick all Villagers");
        }
    }

    //creates "join_channel" ban for whole channel. Shall be used to kick all Players quickly from channel in the morning
    public static void kickAll(String cname){
        try {
            String requestBody = createRequestBody(Optional.empty(), Optional.of(cname), "join_channel", Reason.KICK_ALL);
            createHttpRequest(HttpMethod.POST, requestBody);
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't kick all Players");
        }
    }

    //creates "publish_audio" ban for player who died and should be muted in death view.
    public static void muteDeadPlayer(Player player){
        try {
        String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "publish_audio", Reason.MUTE_DEAD);
        createHttpRequest(HttpMethod.POST, requestBody);
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player could not be muted");
        }
    }

    //creates "publish_audio" ban for Troll
    public static void muteTroll(Player player) {
        try {
            String requestBody = createRequestBody(Optional.of(player), Optional.empty(), "publish_audio", Reason.MUTE_TROLL);
            createHttpRequest(HttpMethod.POST, requestBody);
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Troll could not be muted");
        }
    }

    //creates Token for VoiceChannel. Same procedure for admin and normal players, admin's token generation automatically creates agora channel
    public static String createVoiceChannelToken(Lobby lobby, User user) {
        RTCTokenBuilder newToken = new RTCTokenBuilder();
        String token = newToken.buildTokenWithUserAccount(lobby.getId().toString(), user.getId().toString(), VoiceChatRole.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }
}