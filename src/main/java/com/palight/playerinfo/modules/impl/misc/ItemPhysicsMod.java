package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.ItemPhysicsGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemPhysicsMod extends Module {
    private long tick;
    @ConfigOption
    public double rotateSpeed = 1.0F;
    public ItemPhysicsMod() {
        super("itemPhysics", ModuleType.MISC, new ItemPhysicsGui(), null);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        this.tick = System.nanoTime();
    }

    public long getTick() {
        return this.tick;
    }
}
