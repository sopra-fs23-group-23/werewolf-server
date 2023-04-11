package ch.uzh.ifi.hase.soprafs23.agora;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
