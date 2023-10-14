package io.xks.fabricmod.matrixbuddy;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import io.xks.fabricmod.matrixbuddy.decision.BeatMinecraftTask;
import io.xks.fabricmod.matrixbuddy.decision.PeriodicTaskRunner;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.EventListener;
import io.xks.fabricmod.matrixbuddy.eventbus.events.*;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


public class MatrixBuddyClient implements ClientModInitializer {
	private int decisionCDPeriod;
	public static MatrixBuddyClient instance;

	public IBaritone baritone;


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//this.client = MinecraftClient.getInstance();

		EventBus.subscribe(TitleScreenEntryEvent.class, event -> init());
	}

	private void init(){
		instance = this;
		this.baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

		EventBus.subscribe(ClientTickEvent.class, new EventListener() {
			private boolean firstFire = true;
			@Override
			public void onEvent(Event event) {
				MinecraftClient client = ((ClientTickEvent)event).getClient();
				if (client.player != null && client.getNetworkHandler() != null)  {
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

		EventBus.subscribe(DecisionStartEvent.class, event -> {
			new BeatMinecraftTask(task -> {}).run();
		});

	}



}