package ch.uzh.ifi.hase.soprafs23.agora;

public interface PackableEx extends Packable{
    void unmarshal(ByteBuf in);
}
