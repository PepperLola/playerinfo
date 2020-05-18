package com.palight.playerinfo.listeners;

import com.palight.playerinfo.util.MCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.PacketLoggingHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.FileWriter;

public class HitListener {

    //@SubscribeEvent
    public void onClientConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
//        System.out.println("(PLAYERINFO) CLIENT CONNECTED TO SERVER");
        event.manager.channel().pipeline().addFirst(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                try {
//                     System.out.println("(PLAYERINFO) MESSAGE RECEIVED: " + msg.getClass().getSimpleName());
                    if (msg instanceof C02PacketUseEntity) {
                        C02PacketUseEntity packet = (C02PacketUseEntity) msg;
                        System.out.println("(PLAYERINFO) MESSAGE RECEIVED: " + msg.getClass().getSimpleName());
                        if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                            System.out.println(packet.getEntityFromWorld(Minecraft.getMinecraft().thePlayer.getEntityWorld()));
                        }
                    }
                    if (msg instanceof S20PacketEntityProperties) {
                        S20PacketEntityProperties packet = (S20PacketEntityProperties) msg;
                        System.out.println("(PLAYERINFO) MESSAGE RECEIVED: " + packet.func_149441_d());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ctx.fireChannelRead(msg);
                }
            }
        });
    }

    @SubscribeEvent
    public void onPlayerHit(LivingHurtEvent event) {
//        System.out.println("LIVING HURT EVENT TRIGGERED");
        if (!(event.entity instanceof EntityPlayerMP)) return;
        if (!(event.entity.equals(Minecraft.getMinecraft().thePlayer))) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        Entity damageSource = event.source.getSourceOfDamage();
        if (damageSource == null) return;
        if (damageSource instanceof EntityArrow) {
            damageSource = ((EntityArrow) damageSource).shootingEntity;
        }
        float distance = damageSource.getDistanceToEntity(player);

        MCUtil.sendPlayerMessage(player, "src=" + damageSource.getName() + " damage=" + event.ammount + " dst=" + distance);
    }
}
