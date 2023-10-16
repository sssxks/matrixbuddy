package io.xks.fabricmod.matrixbuddy.eventbus.events;

import net.minecraft.client.MinecraftClient;

public class ClientTickEvent implements Event{


    private final MinecraftClient client;
    public ClientTickEvent(MinecraftClient client){
        this.client = client;
    }

    public MinecraftClient getClient() {
        return client;
    }
}
