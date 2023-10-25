package io.xks.fabricmod.matrixbuddy.agent.command;

public interface ObjectSerializer {
    String serialize(Object obj);
    Object deserialize(String string);
}
