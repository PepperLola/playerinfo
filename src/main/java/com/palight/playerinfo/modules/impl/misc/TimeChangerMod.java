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
            Time time = Time.getTime(ModConfiguration.selectedTime);
            if (time == null) return;
            mc.theWorld.setWorldTime(getTime(time) % 24000);
        }
    }

    public long getTime(Time time) {
        switch (time) {
            case NIGHT:
                return 18000;
            case DAWN:
                return 0;
            case DUSK:
                return 12542;
            case DAY:
            default:
                return 6000;
        }
    }

    public enum Time {
        DAY,
        NIGHT,
        DAWN,
        DUSK;

        public static Time getTime(String time) {
            switch (time.toLowerCase()) {
                case "day":
                    return Time.DAY;
                case "night":
                    return Time.NIGHT;
                case "dawn":
                    return Time.DAWN;
                case "dusk":
                    return Time.DUSK;
                default:
                    return null;
            }
        }
    }

}
