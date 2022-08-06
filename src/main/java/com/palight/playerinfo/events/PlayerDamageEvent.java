package com.palight.playerinfo.events;

import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDamageEvent extends Event {
    private final DamageSource damageSource;
    private final double damage;

    public PlayerDamageEvent(DamageSource damageSource, double damage) {
        this.damageSource = damageSource;
        this.damage = damage;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public double getDamage() {
        return damage;
    }
}
