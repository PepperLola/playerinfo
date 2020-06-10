package com.palight.playerinfo.listeners;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.LoginGui;
import com.palight.playerinfo.modules.NoteBlockUtil;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class RenderListener {

    public static final int LOGIN_BUTTON_ID = PlayerInfo.MODID.hashCode();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        boolean enableBlur = ModConfiguration.enableBlur;
        if (enableBlur) {
            if (Minecraft.getMinecraft().theWorld != null) {
                if (!(event.gui instanceof GuiChat)) {
                    EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
                    er.loadShader(new ResourceLocation(PlayerInfo.MODID, "shaders/post/blur.json"));
                }
            }
        }
        if (event.gui instanceof GuiOptions) {
            GuiScreen gui = event.gui;

            int buttonSpacing = 4;

            int buttonWidth = 50;
            int buttonHeight = 20;

            int buttonX = gui.width - buttonWidth - buttonSpacing;
            int buttonY = gui.height - buttonHeight - buttonSpacing;

            while (true) {
                if (buttonX > gui.width) {
                    break;
                }

                int increment = 0;

                Rectangle btn = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
                boolean intersects = false;

                for (int i = 0; i < event.buttonList.size(); i++) {
                    GuiButton currentButton = event.buttonList.get(i);
                    if (!intersects) {
                        intersects = btn.intersects(new Rectangle(currentButton.xPosition, currentButton.yPosition, currentButton.width, currentButton.height));
                    }
                    if (intersects) {
                        increment = currentButton.xPosition + currentButton.width + buttonSpacing;
                    }
                }

                if (!intersects) {
                    break;
                }

                buttonX = increment;
            }

            GuiButton loginGuiButton = new GuiButton(LOGIN_BUTTON_ID, buttonX, buttonY, buttonWidth, buttonHeight, "Login");
            event.buttonList.add(loginGuiButton);
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

    @SubscribeEvent
    public void onNoteBlockChange(NoteBlockEvent.Change event) {
        if (ModConfiguration.noteBlockHelper) {
            if (event.getVanillaNoteId() != NoteBlockUtil.getNoteId()) {
                NoteBlockUtil.setNoteId(event.getVanillaNoteId());
                MCUtil.sendPlayerMessage(
                        Minecraft.getMinecraft().thePlayer,
                        EnumChatFormatting.AQUA +
                                "Note Block now set to " +
                                EnumChatFormatting.RESET + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD +
                                NoteBlockUtil.getNoteString(event.getNote()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onButtonPressPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiOptions && event.button.id == LOGIN_BUTTON_ID) {
            Minecraft.getMinecraft().displayGuiScreen(new LoginGui());
            event.setCanceled(true);
        }
    }
}
