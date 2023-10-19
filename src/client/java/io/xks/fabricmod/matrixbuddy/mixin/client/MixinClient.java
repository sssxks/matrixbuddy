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

//    @Inject(method = "setWorld",at = @At("RETURN"))
//    private void worldEntry(ClientWorld world, CallbackInfo ci){
//        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
//
//        GoalXZ walkingGoal = new GoalXZ(-4000,4000);
//        ICustomGoalProcess customGoalProcess = baritone.getCustomGoalProcess();
//        customGoalProcess.setGoalAndPath(walkingGoal);

//        IMineProcess mineProcess = baritone.getMineProcess();
//        mineProcess.mineByName(32,"minecraft:diamond_ore");


//        System.out.println(walkingGoal);


//    }

//
//    @Inject(method = "run",at = @At("HEAD"))
//    private void startBotTask(CallbackInfo ci){
//        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
//
//        GoalXZ walkingGoal = new GoalXZ(8000,-3000);
//        ICustomGoalProcess customGoalProcess = baritone.getCustomGoalProcess();
//        customGoalProcess.setGoalAndPath(walkingGoal);
//
//        IMineProcess mineProcess = baritone.getMineProcess();
//        mineProcess.mineByName(32,"minecraft:diamond_ore");
//
//
//        System.out.println(walkingGoal);
//
//
//    }


}
