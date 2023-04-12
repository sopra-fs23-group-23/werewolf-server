package ch.uzh.ifi.hase.soprafs23.constant;

public enum VoiceChatRole {
    Role_Publisher(1),
    Role_Subscriber(2),
    Role_Admin(101);

    public int initValue;
    VoiceChatRole(int initValue) {
        this.initValue = initValue;
    }
}
