package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class PlayerGetDTO {
    private Long id;
    private String name;
    private boolean isAlive;
    private String avatarUrl;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
