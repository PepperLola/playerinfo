package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlurMod extends Module {
    public BlurMod() {
        super("backgroundBlur", ModuleType.MISC, null, null);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent event) {
        if (isEnabled()) {
            if (Minecraft.getMinecraft().theWorld != null) {
                if (!(event.gui instanceof GuiChat)) {
                    EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
                    er.loadShader(new ResourceLocation(PlayerInfo.MODID, "shaders/post/blur.json"));
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            // stop blurring background because it isn't the background anymore
            if (event.phase == TickEvent.Phase.END) {
                EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
                if (er.isShaderActive()) {
                    er.stopUseShader();
                }
            }
        }
    }
}
