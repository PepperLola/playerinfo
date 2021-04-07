package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.TimeChangerGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeChangerMod extends Module {

    @ConfigOption
    public String selectedTime = "time";
    @ConfigOption
    public double fastTimeMultiplier = 1.0D;
    @ConfigOption
    public boolean lockDay = false;
    @ConfigOption
    public boolean lockNight = false;
    @ConfigOption
    public boolean bounce = false;

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
                long start = 0; // beginning of day
                long stop = 24000; // end of day
                if (lockDay) stop = 12000; // sunset
                if (lockNight) {
                    start = 14000; // before midnight but after first monster spawn tick
                    stop = 22000; // after midnight but before last monster spawn tick
                }
                return bounce ?
                        (long) Math.floor((stop - start) / 2D * Math.sin((2 * System.currentTimeMillis() * fastTimeMultiplier) / (stop - start) - 1.5 * Math.PI) + start + (stop - start) / 2D) :
                        (long) (System.currentTimeMillis() * fastTimeMultiplier) % (stop - start) + start;
            case REAL:
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
                long minutes = ChronoUnit.MINUTES.between(startOfDay, now);
                long totalMinutes = 1440; // minutes in a day
                double ratio = minutes * 1f / totalMinutes;
                long minecraftRatio = (long) Math.floor(24000 * ratio);
                long minecraftTime = (minecraftRatio + 18000) % 24000;
                return minecraftTime;
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
        FAST,
        REAL;

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
                case "real":
                    return Time.REAL;
                default:
                    return null;
            }
        }
    }

}
