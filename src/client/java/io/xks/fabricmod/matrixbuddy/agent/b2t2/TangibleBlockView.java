package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TangibleBlockView implements BlockView {
    private final BlockPos pos;
    private final BlockState block;

    public TangibleBlockView(BlockPos pos) {
        this.pos = pos;
        this.block = Objects.requireNonNull(MinecraftClient.getInstance().world).getBlockState(new BlockPos(pos));
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public Block getBlock() {
        return block.getBlock();
    }

    @Override
    public List<BlockView> getAdjacent() {
        ArrayList<BlockView> adjacentBlocks = new ArrayList<>(6);

        adjacentBlocks.add(new TangibleBlockView(pos.add(-1, 0, 0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(1, 0, 0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0, 0, 1)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0, 0, -1)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0, -1, 0)));
        adjacentBlocks.add(new TangibleBlockView(pos.add(0, 1, 0)));

        return adjacentBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TangibleBlockView that = (TangibleBlockView) o;
        return Objects.equals(pos, that.pos) && Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
