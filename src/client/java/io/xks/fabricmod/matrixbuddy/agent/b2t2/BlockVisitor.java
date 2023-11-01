package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BlockVisitor {
    public LinkedList<BlockPos> breadthFirstSearch(BlockView startBlock, Map<BlockPos, Boolean> visited){
        assert startBlock.getBlock() == Blocks.OBSIDIAN;
        LinkedList<BlockPos> group = new LinkedList<>();
        Queue<BlockView> queue = new LinkedList<>();
        queue.add(startBlock);

        while (!queue.isEmpty()){
            BlockView block = queue.poll();
            group.add(block.getPos());
            visited.put(block.getPos(), true);

            for (BlockView neighbor : block.getAdjacent()) {
                if (!visited.getOrDefault(neighbor.getPos(), false)
                && neighbor.getBlock() == Blocks.OBSIDIAN){
                    queue.add(neighbor);
                }
            }
        }

        return group;
    }
}
