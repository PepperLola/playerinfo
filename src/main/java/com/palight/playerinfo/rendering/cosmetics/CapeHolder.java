package com.palight.playerinfo.rendering.cosmetics;

import com.palight.playerinfo.util.math.Vector3d;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.MathHelper;

public interface CapeHolder {
    public VerletSimulation getSimulation();

    public default void updateSimulation(AbstractClientPlayer player, int partCount) {
        VerletSimulation simulation = getSimulation();
        boolean dirty = simulation.init(partCount);
        if (dirty) {
            simulation.applyMovement(new Vector3d(1f, 1f, 0));
            for (int i = 0; i < 5; i++)
                simulate(player);
        }
    }

    public default void simulate(AbstractClientPlayer player) {
        VerletSimulation simulation = getSimulation();
        if (simulation == null || simulation.getPoints().isEmpty()) return;
        double d = player.chasingPosX - player.posX;
        double m = player.chasingPosZ - player.posZ;
        double n = player.renderYawOffset;

        double o = Math.sin(n * 0.017453292F);
        double p = -Math.cos(n * 0.017453292F);

        float heightMul = 6;
        float strafeMul = 2;

        double fallHack = MathHelper.clamp_double((player.prevPosY - player.posY) * 10, 0, 1);
        Vector3d strafe = new Vector3d(player.posX - player.prevPosX, player.posY - player.prevPosY, player.posZ - player.prevPosZ);
        strafe.rotateDegrees(-player.rotationYaw);
        double changeX = (d * o + m * p) + fallHack + (player.isSneaking() && !simulation.isSneaking() ? 3 : 0);
        double changeY = (player.posY - player.prevPosY) * heightMul + (player.isSneaking() && !simulation.isSneaking() ? 1 : 0);
        double changeZ = -strafe.x * strafeMul;
        simulation.setSneaking(player.isSneaking());
        Vector3d change = new Vector3d(changeX, changeY, changeZ);
        simulation.applyMovement(change);
        simulation.simulate();
    }

//    public default void simulate(EntityPlayer player) {
//        VerletSimulation simulation = getSimulation();
//        if (simulation.getPoints().isEmpty()) return;
//
//        double d = player.chasingPosX - player.posX;
//        double m = player.chasingPosZ - player.posZ;
//        double n = player.renderYawOffset;
//
//        double o = Math.sin(n * 0.017453292F);
//        double p = -Math.cos(n * 0.017453292F);
//
//        final int heightMul = 6;
//        double fallHack = MathHelper.clamp_double((-player.posY) * 10, 0, 1);
//        Vector3d strafe = new Vector3d(player.posX - player.prevPosX, player.posY - player.prevPosY, player.posZ - player.prevPosZ);
//        strafe.rotateDegrees(-player.rotationYaw);
//        double changeX = (d * o + m * p) + fallHack;
//        double changeY = (player.posY - player.prevPosY) * heightMul;
//        double changeZ = -strafe.x;
//        Vector3d change = new Vector3d(changeX, changeY, changeZ);
//
////        simulation.getPoints().get(0).prevPosition.copy(simulation.getPoints().get(0).position);
////        simulation.getPoints().get(0).position.x += (d * o + m * p) + MathHelper.clamp_double((simulation.getPoints().get(0).position.y - (player.posY * heightMul)), 0d, 1d);
////        simulation.getPoints().get(0).position.y = (float) (player.posY * heightMul + (player.isSneaking() ? -4 : 0));
////        int numPoints = simulation.getPoints().size();
////        System.out.println("X1: " + simulation.getPoints().get(0).position.x + " | Y1: " + simulation.getPoints().get(0).position.y + " | Z1: " + simulation.getPoints().get(0).position.z);
////        System.out.println("X2: " + simulation.getPoints().get(numPoints - 1).position.x + " | Y2: " + simulation.getPoints().get(numPoints - 1).position.y + " | Z2: " + simulation.getPoints().get(numPoints - 1).position.z);
//        simulation.getPoints().get(0).prevPosition.copy(simulation.getPoints().get(0).position);
//        simulation.getPoints().get(0).position.add(new Vector3d(change.x, change.y, change.z));
//        simulation.simulate();
//    }
}
