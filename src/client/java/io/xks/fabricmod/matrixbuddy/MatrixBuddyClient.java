package io.xks.fabricmod.matrixbuddy;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import io.xks.fabricmod.matrixbuddy.agent.Agent;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.EventListener;
import io.xks.fabricmod.matrixbuddy.eventbus.events.*;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


/**
 * this class is responsible for initializing the mod. It keeps track of the stage of whether the game has entered the title screen for the first time.
 */
public class MatrixBuddyClient implements ClientModInitializer {
    public static MatrixBuddyClient instance;
    private static boolean hasInitialized = false;
    private final int decisionPeriodTicks = 10;
    public IBaritone baritone;
    private int decisionCooldown;
    private Agent agent;

    private static boolean isInGame(MinecraftClient client) {
        return client.player != null && client.getNetworkHandler() != null;
    }

    @Override
    public void onInitializeClient() {
        EventBus.subscribe(TitleScreenEntryEvent.class, this::init);
    }

    private void init(Event _unused) {
        if (hasInitialized) {
            return;
        }
        hasInitialized = true;
//		EventBus.unsubscribe(TitleScreenEntryEvent.class, this::init);
        instance = this;
        this.baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

        EventBus.subscribe(ClientTickEvent.class, new EventListener() {
            private boolean firstFire = true;

            //			long startTime = 0;
            @Override
            public void onEvent(Event event) {
                MinecraftClient client = ((ClientTickEvent) event).client();
                if (isInGame(client)) {
                    decisionCooldown++;
                    if (decisionCooldown % decisionPeriodTicks == 0) {
                        decisionCooldown = 0;

                        //calculate the MSP30T
//						long endTime = System.nanoTime();
//						double elapsedMilliseconds = (double) (startTime - endTime) / 1_000_000.0;
//						System.out.println( "MS/30tick "+ elapsedMilliseconds);
//						startTime = endTime;

                        if (firstFire) {
                            EventBus.publish(new DecisionStartEvent());
                            firstFire = false;
                        }

                        EventBus.publish(new DecisionTickEvent());

                    }

                } else {
                    firstFire = true;
                }
            }
        });

        EventBus.subscribe(DecisionStartEvent.class, event -> agent = new Agent());


    }


}