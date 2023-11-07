package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BlockVisitor {
    public LinkedList<BlockPos> breadthFirstSearch(BlockView startBlock, Map<BlockPos, Boolean> visited) {
        assert startBlock.getBlock() == Blocks.OBSIDIAN;
        LinkedList<BlockPos> group = new LinkedList<>();
        Queue<BlockView> queue = new LinkedList<>();
        queue.add(startBlock);

        while (!queue.isEmpty()) {
            BlockView block = queue.poll();
            group.add(block.getPos());
            visited.put(block.getPos(), true);

            for (BlockView neighbor : block.getAdjacent()) {
                if (neighbor.getBlock() == Blocks.OBSIDIAN
                        && !visited.getOrDefault(neighbor.getPos(), false)
                        && !queue.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        return group;
    }
}
