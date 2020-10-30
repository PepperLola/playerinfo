package net.themorningcompany.playerinfo.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.themorningcompany.playerinfo.util.MCUtil;

public class HitListener {
    @SubscribeEvent
    public void onPlayerHit(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        if (!(event.getEntity().equals(Minecraft.getInstance().player))) return;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        Entity damageSource = event.getSource().getTrueSource();
        double x_distance = Math.abs(damageSource.lastTickPosX - player.lastTickPosX);
        double y_distance = Math.abs(damageSource.lastTickPosY - player.lastTickPosY);
        double z_distance = Math.abs(damageSource.lastTickPosZ - player.lastTickPosZ);

        double total_distance = Math.sqrt((x_distance * x_distance) + (y_distance * y_distance) + (z_distance * z_distance));
        MCUtil.sendPlayerMessage(player, "Attacker Distance: " + total_distance);
    }
}
