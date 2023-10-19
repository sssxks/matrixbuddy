package io.xks.fabricmod.matrixbuddy;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import io.xks.fabricmod.matrixbuddy.decision.tasking.EmptyTestTask;
import io.xks.fabricmod.matrixbuddy.decision.tasking.PeriodicTaskRunner;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.EventListener;
import io.xks.fabricmod.matrixbuddy.eventbus.events.*;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


/**
 * this class is responsible for initializing the mod. It maintains the stage of whether the game has entered the title screen for the first time.
 */
public class MatrixBuddyClient implements ClientModInitializer {
	private int decisionCDPeriod;
	public static MatrixBuddyClient instance;
	private static boolean hasEnteredTitleScreen = false;

	public IBaritone baritone;


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//this.client = MinecraftClient.getInstance();

		EventBus.subscribe(TitleScreenEntryEvent.class, this::init);
	}

	private void init(Event _unused){
		if (hasEnteredTitleScreen){
			return;
		}

//		EventBus.unsubscribe(TitleScreenEntryEvent.class, this::init);
		instance = this;
		this.baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

		EventBus.subscribe(ClientTickEvent.class, new EventListener() {
			private boolean firstFire = true;
			@Override
			public void onEvent(Event event) {
				MinecraftClient client = ((ClientTickEvent)event).client();
				if (isInGame(client))  {
					decisionCDPeriod++;
					if (decisionCDPeriod % 30 == 0){
						decisionCDPeriod = 0;

						if (firstFire) {
							EventBus.publish(new DecisionStartEvent());
							firstFire = false;
						}

						EventBus.publish(new DecisionTickEvent());

					}

				} /*else {
					firstFire = true;
				}*/
			}
		} );

		EventBus.subscribe(DecisionTickEvent.class, event -> PeriodicTaskRunner.tick());

//		EventBus.subscribe(DecisionStartEvent.class, event -> new TestCraftTask().run());
		EventBus.subscribe(DecisionStartEvent.class, event -> new EmptyTestTask().run());


	}

	private static boolean isInGame(MinecraftClient client) {
		return client.player != null && client.getNetworkHandler() != null;
	}


}