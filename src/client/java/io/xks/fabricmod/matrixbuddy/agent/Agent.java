package io.xks.fabricmod.matrixbuddy.agent;

import io.xks.fabricmod.matrixbuddy.agent.b2t2.ObsidianCleanTask;
import io.xks.fabricmod.matrixbuddy.agent.command.Commander;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TaskExecutor;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;
import io.xks.fabricmod.matrixbuddy.eventbus.events.PlayerDamageEvent;

/**
 * Agent
 * attach logger
 * attach commander
 */
public class Agent {
    Commander commander;
    TaskExecutor taskExecutor;

    public Agent() {
        this.commander = new Commander();
        this.taskExecutor = new TaskExecutor();
        taskExecutor.add(new ObsidianCleanTask(task -> {}), 1);
        EventBus.subscribe(PlayerDamageEvent.class, this::onPlayerDamage);
    }

    public void onPlayerDamage(Event event){
        event = (PlayerDamageEvent) event;
//        ((PlayerDamageEvent) event).getDamageSource().getAttacker()

    }




}
