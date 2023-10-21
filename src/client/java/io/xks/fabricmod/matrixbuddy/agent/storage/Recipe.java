package io.xks.fabricmod.matrixbuddy.agent.storage;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class Recipe{
    public final Item[][] inputs;

    public Recipe(Item[][] inputs) {
        this.inputs = inputs;
    }

    public Map<Item, Integer> getIngredientsWithQuantities(){
        Map<Item, Integer> ingredientQuantity = new HashMap<>(9);
        for (Item[] row : inputs) {
            for (Item item : row) {
                //skip null items
                if (item == null) {
                    continue;
                }

                ingredientQuantity.put(item, ingredientQuantity.getOrDefault(item, 0) + 1);
            }
        }

        return ingredientQuantity;
    }
}
