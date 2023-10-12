package com.palight.playerinfo.training;

import com.palight.playerinfo.training.targets.TrackingTarget;
import com.palight.playerinfo.training.targets.TrainingTarget;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class AimTrainingController {
    public static Difficulty DIFFICULTY = Difficulty.MEDIUM;
    public static boolean ENABLED = false;

    public static Mode MODE = Mode.TRACKING;

    public static double START_TIME = 0;
    private static final double TRACKING_DURATION = 30 * 1000; // 30 seconds

    private static AbstractClientPlayer player;

    private static int SCORE = 0;
    private static int TOTAL_TICKS = 0;

    private static List<TrainingTarget> targets = new ArrayList<>();

    public static void addTarget(TrainingTarget target) {
        targets.add(target);
    }

    public static void removeTarget(TrainingTarget target) {
        targets.remove(target);
    }

    public static List<TrainingTarget> getTargets() {
        return targets;
    }

    public static void clearTargets() {
        targets.clear();
    }

    public static void start() {
        if (targets.isEmpty())
            addTarget(new TrackingTarget(0, 10, 0, 2.0F));
        TOTAL_TICKS = 0;
        START_TIME = System.currentTimeMillis();
        ENABLED = true;
    }

    public static void end() {
        clearTargets();
        ENABLED = false;
        MCUtil.sendPlayerinfoMessage(
                Minecraft.getMinecraft().thePlayer,
                EnumChatFormatting.GREEN +
                        "Your score was " + ((int) Math.floor((double) SCORE / TOTAL_TICKS * 100D)) + "%!"
        );
    }

    public static void update() {
        if (!ENABLED) return;
        if (System.currentTimeMillis() - AimTrainingController.START_TIME > TRACKING_DURATION) {
            end();
            return;
        }

        targets.forEach(TrainingTarget::update);
        updateScoring();
    }

    public static void updateScoring() {
        if (player == null) player = Minecraft.getMinecraft().thePlayer;

        TOTAL_TICKS ++;
        targets.forEach(target -> {
            // TODO: add more tracked data for analysis
            if (target.isPlayerLooking(player)) {
                SCORE ++;
            }
        });
    }

    public static void render() {
        if (!ENABLED) return;
        targets.forEach(TrainingTarget::render);
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public enum Mode {
        TRACKING,
        FLICKING
    }
}
