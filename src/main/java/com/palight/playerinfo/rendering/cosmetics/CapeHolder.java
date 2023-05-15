package com.palight.playerinfo.rendering.cosmetics;

import net.minecraft.entity.player.EntityPlayer;

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

        simulation.getPoints().get(0).prevPosition.copy(simulation.getPoints().get(0).position);
        double d = player.chasingPosX - player.posX;
        double m = player.chasingPosZ - player.posZ;
        double n = player.renderYawOffset;

        double o = Math.sin(n * 0.017453292F);
        double p = -Math.cos(n * 0.017453292F);

        simulation.getPoints().get(0).position.x += (d * o + m * p);
        simulation.getPoints().get(0).position.y = (float) (player.posY + (player.isSneaking() ? -4 : 0));
        simulation.simulate();
    }
}
