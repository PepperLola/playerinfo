package com.palight.playerinfo.mixins.gui;


import com.palight.playerinfo.events.ServerJoinEvent;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiConnecting.class)
public class MixinGuiConnecting {
    @Inject(method = "connect", at = @At(value = "HEAD"))
    private void connect(final String ip, final int port, CallbackInfo ci) {
        System.out.println("PLAYER JOINED " + ip);
        MinecraftForge.EVENT_BUS.post(new ServerJoinEvent(ip, port));
    }
}