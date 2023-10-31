package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3i;

import java.util.LinkedList;
import java.util.Queue;

public class BlockVisitor {
    public LinkedList<Vec3i> breadthFirstSearch(BlockView startBlock, int[][][] visited){
        assert startBlock.getBlock() == Blocks.OBSIDIAN;
        LinkedList<Vec3i> group = new LinkedList<>();
        Queue<BlockView> queue = new LinkedList<>();
        queue.add(startBlock);

        while (!queue.isEmpty()){
            BlockView block = queue.poll();
            group.add(block.getPos());

            for (BlockView neighbor : block.getAdjacent()) {
                if (visited[neighbor.getPos().getX()][neighbor.getPos().getY()][neighbor.getPos().getZ()] == 0
                && neighbor.getBlock() == Blocks.OBSIDIAN){
                    queue.add(neighbor);
                }
            }
        }

        return group;
    }
}
