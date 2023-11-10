package io.xks.fabricmod.matrixbuddy.agent.collect;

import baritone.api.process.IMineProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public class MineTask extends Task {
    private static IMineProcess mineProcess;
    private final int quantity;
    private final Block[] blocks;


    public MineTask(int quantity, Consumer<Task> callback, Block... blocks) {
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
        mineProcess = MatrixBuddyClient.INSTANCE.baritone.getMineProcess();


        mineProcess.mine(quantity, blocks);
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void resume() {
        super.resume();
    }


    @Override
    public void complete() {
        super.complete();
    }
}
