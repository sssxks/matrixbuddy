package io.xks.fabricmod.matrixbuddy.eventbus.events;

import net.minecraft.entity.damage.DamageSource;

public class PlayerDamageEvent implements Event{
    public DamageSource getDamageSource() {
        return damageSource;
    }

    public double getAmount() {
        return amount;
    }

    private final DamageSource damageSource;
    private final double amount;

    public PlayerDamageEvent(DamageSource damageSource, double amount) {
        this.damageSource = damageSource;
        this.amount = amount;
    }


}
