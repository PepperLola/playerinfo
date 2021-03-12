package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.TimeChangerGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimeChangerMod extends Module {

    @ConfigOption
    public String selectedTime = "time";
    @ConfigOption
    public double fastTimeMultiplier = 1.0D;

    public TimeChangerMod() {
        super("timeChanger", "Time Changer", "Lets you change the in game time for you.", Module.ModuleType.MISC, new TimeChangerGui(), null);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isEnabled() && mc.theWorld != null && selectedTime != null && Time.getTime(selectedTime) == Time.FAST) {
            Minecraft.getMinecraft().theWorld.setWorldTime(getTime(Time.FAST));
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
            case FAST:
                return (long) (System.currentTimeMillis() * fastTimeMultiplier) % 24000;
            case DAY:
            default:
                return 6000;
        }
    }

    public enum Time {
        DAY,
        NIGHT,
        DAWN,
        DUSK,
        FAST;

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
                case "fast":
                    return Time.FAST;
                default:
                    return null;
            }
        }
    }

}
