package io.xks.fabricmod.matrixbuddy.eventbus.events;

import net.minecraft.client.MinecraftClient;

public record ClientTickEvent(MinecraftClient client) implements Event {


}
