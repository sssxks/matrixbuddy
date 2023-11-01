package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public interface BlockView {
    BlockPos getPos();
    Block getBlock();
    List<BlockView> getAdjacent();
}
