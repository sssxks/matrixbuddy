package io.xks.fabricmod.matrixbuddy.agent.collect;

import baritone.api.process.IMineProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public class MineTask extends Task {
    private static IMineProcess mineProcess;
    private final String[] blocks;
    private final int quantity;


    public MineTask(int quantity, Consumer<Task> callback, String... blocks) {
        super(callback);
        this.quantity = quantity;
        this.blocks = blocks;

    }

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

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void proceed() {
        super.proceed();
    }


    @Override
    public void complete() {
        super.complete();
    }
}
