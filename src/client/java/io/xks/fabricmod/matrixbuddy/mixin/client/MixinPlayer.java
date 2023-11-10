package io.xks.fabricmod.matrixbuddy.mixin.client;

import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.PlayerDamageEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinPlayer {

    @Inject(
            method = "applyDamage",
            at = @At("HEAD")
    )
    protected void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        EventBus.publish(new PlayerDamageEvent(source, amount));
    }
}
