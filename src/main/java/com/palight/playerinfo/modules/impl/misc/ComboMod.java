package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.impl.ComboWidget;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ComboMod extends Module {
    private int lastAttackedEntity = -1;
    public int combo = 0;

    public ComboMod() {
        super("combo", ModuleType.MISC, null, new ComboWidget());
    }

    // multiplayer
    public void onEntityStatusPacket (S19PacketEntityStatus packet) {
        if (packet.getOpCode() == 2) {
            Entity entity = packet.getEntity(Minecraft.getMinecraft().theWorld);
            if (entity != null) {
                if (this.lastAttackedEntity != -1 && entity.hashCode() == lastAttackedEntity) {
                    this.lastAttackedEntity = -1;
                    combo ++;
                } else if (entity.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                    this.combo = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.entity.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
            this.lastAttackedEntity = event.target.hashCode();
        }
    }

    // singleplayer
    @SubscribeEvent
    public void onTakeDamage(LivingHurtEvent event) {
        if (event.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            if (event.source.getSourceOfDamage() != null) {
                combo = 0;
            }
        } else {
            if (event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage().equals(Minecraft.getMinecraft().thePlayer)) {
                combo ++;
            }
        }
    }
}
