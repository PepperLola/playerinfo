package com.palight.playerinfo.rendering.cosmetics;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public interface CapeHolder {
    public VerletSimulation getSimulation();

    public default void updateSimulation(EntityPlayer player, int partCount) {
        VerletSimulation simulation = getSimulation();
        boolean dirty = simulation.init(partCount);
        if (dirty) {
            for (int i = 0; i < 10; i++)
                simulate(player);
        }
    }

    public default void simulate(EntityPlayer player) {
        VerletSimulation simulation = getSimulation();
        if (simulation.getPoints().isEmpty()) return;

        double d = player.chasingPosX - player.posX;
        double m = player.chasingPosZ - player.posZ;
        double n = player.renderYawOffset;

        double o = Math.sin(n * 0.017453292F);
        double p = -Math.cos(n * 0.017453292F);

        final int heightMul = 6;
        simulation.getPoints().get(0).prevPosition.copy(simulation.getPoints().get(0).position);
        simulation.getPoints().get(0).position.x += (d * o + m * p) + MathHelper.clamp_double((simulation.getPoints().get(0).position.y - (player.posY * heightMul)), 0d, 1d);
        simulation.getPoints().get(0).position.y = (float) (player.posY * heightMul + (player.isSneaking() ? -4 : 0));
        int numPoints = simulation.getPoints().size();
        System.out.println("X1: " + simulation.getPoints().get(0).position.x + " | Y1: " + simulation.getPoints().get(0).position.y + " | Z1: " + simulation.getPoints().get(0).position.z);
        System.out.println("X2: " + simulation.getPoints().get(numPoints - 1).position.x + " | Y2: " + simulation.getPoints().get(numPoints - 1).position.y + " | Z2: " + simulation.getPoints().get(numPoints - 1).position.z);
        simulation.simulate();
    }
}
