package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.TimeChangerGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimeChangerMod extends Module {


    public TimeChangerMod() {
        super("time-changer", "Time Changer", "Lets you change the in game time for you.", Module.ModuleType.MISC, new TimeChangerGui(), null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.timeChangerModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "timeChangerModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null && ModConfiguration.selectedTime != null) {
            mc.theWorld.setWorldTime(getTime(ModConfiguration.selectedTime) % 24000);
        }
    }

    public long getTime(String timeString) {
        switch (timeString.toLowerCase()) {
            case "day":
                return 6000;
            case "night":
                return 18000;
            case "dawn":
                return 0;
            case "dusk":
                return 12542;
            default:
                return 6000;
        }
    }

}
