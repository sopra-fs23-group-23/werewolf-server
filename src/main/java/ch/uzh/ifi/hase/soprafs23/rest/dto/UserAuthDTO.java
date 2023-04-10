package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserAuthDTO {
    private Long id;
    private String username;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() { return this.token;}

    public void setToken(String token) {this.token = token;}

}

