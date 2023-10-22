package io.xks.fabricmod.matrixbuddy.agent.collect;

import baritone.api.process.IMineProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.PeriodicTimeSlicedTask;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TimeSlicedTask;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public class MineTask extends PeriodicTimeSlicedTask {
    private static IMineProcess mineProcess;
    private final String[] blocks;
    private final int quantity;


    public MineTask(int quantity, Consumer<TimeSlicedTask> callback, String... blocks) {
        super(callback);
        this.quantity = quantity;
        this.blocks = blocks;

    }

    @Override
    public void tick(){
        assert MinecraftClient.getInstance().player != null;
         if (!mineProcess.isActive()){
             complete();
         }

    }
    @Override
    public void run() {
        super.run();
        mineProcess = MatrixBuddyClient.instance.baritone.getMineProcess();

        mineProcess.mineByName(quantity, blocks);
    }
}
