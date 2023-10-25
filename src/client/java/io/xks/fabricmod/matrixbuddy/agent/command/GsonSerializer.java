package io.xks.fabricmod.matrixbuddy.agent.command;

import com.google.gson.Gson;

public class GsonSerializer implements ObjectSerializer{
    @Override
    public String serialize(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    @Override
    public Object deserialize(String string) {
        return null;
    }
}
