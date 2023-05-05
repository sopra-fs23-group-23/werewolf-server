package ch.uzh.ifi.hase.soprafs23.constant;

public enum Reason {
    KICK_VILLAGER(1),
    KICK_ALL(2),
    MUTE_DEAD(3);
    public int initValue;
    Reason(int initValue){
        this.initValue = initValue;
    }
}
