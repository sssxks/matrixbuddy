package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TangibleBlockView implements BlockView{
    private final Vec3i pos;
    private final BlockState block;

    public TangibleBlockView(Vec3i pos) {
        this.pos = pos;
        this.block = Objects.requireNonNull(MinecraftClient.getInstance().world).getBlockState(new BlockPos(pos));
    }

    @Override
    public Vec3i getPos() {
        return pos;
    }

    @Override
    public Block getBlock() {
        return block.getBlock();
    }

    @Override
    public List<BlockView> getAdjacent() {
        ArrayList<BlockView> adjacentBlocks = new ArrayList<>(6);
        adjacentBlocks.add(new TangibleBlockView(pos.add(-1,0,0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(1,0,0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0,0,1)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0,0,-1)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0,-1,0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0,1,0)));

        return adjacentBlocks;
    }
}
