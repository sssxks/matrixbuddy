package io.xks.fabricmod.matrixbuddy.mixin.client;

import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.ClientTickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinClient {

    @Shadow
    public static MinecraftClient getInstance() {
        return null;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void clientTick(CallbackInfo ci) {
        EventBus.publish(new ClientTickEvent(getInstance()));
    }

}
