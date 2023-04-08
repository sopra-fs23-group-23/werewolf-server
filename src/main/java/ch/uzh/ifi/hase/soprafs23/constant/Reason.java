package ch.uzh.ifi.hase.soprafs23.constant;

public enum Reason {
    Kick_Villager(1),
    Got_Killed(2),
    Is_Troll(3),
    Kick_All(4);
    public int initValue;
    Reason(int initValue){
        this.initValue = initValue;
    }
}
