package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
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
        super("backgroundBlur", "Background Blur", "Blurs the backgrounds of menus.", ModuleType.MISC, null, null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "blurModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.blurModEnabled);
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
