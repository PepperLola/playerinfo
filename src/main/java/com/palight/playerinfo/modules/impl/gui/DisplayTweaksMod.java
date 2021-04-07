package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.screens.impl.options.modules.gui.DisplayTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisplayTweaksMod extends Module {

    @ConfigOption
    public boolean disableWaterOverlay = false;
    @ConfigOption
    public boolean lowerFire = false;
    @ConfigOption
    public boolean windowedFullscreen = false;
    @ConfigOption
    public boolean hardcoreHeartsEnabled = false;
    @ConfigOption
    public boolean renderPingAsText = false;
    @ConfigOption
    public boolean unicodeFontRendererEnabled = false;
    @ConfigOption
    public boolean stackChatMessages = false;
    @ConfigOption
    public boolean transparentChat = false;
    @ConfigOption
    public boolean renderOwnName = false;
    @ConfigOption
    public boolean disableInventoryShift = false;

    public static boolean hardcoreHearts = false;

    private String lastMessage = "";
    private int line;
    private int amount;

    public DisplayTweaksMod() {
        super("displayTweaks", "Display Tweaks", "Some useful gui/display tweaks.", ModuleType.GUI, new DisplayTweaksGui(), null);
    }

    @SubscribeEvent
    public void onRenderWaterOverlay(RenderBlockOverlayEvent event) {
        if (!this.isEnabled()) return;
        if (event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER) {
            event.setCanceled(disableWaterOverlay);
        }
    }

//    @SubscribeEvent
//    public void onRenderTitle(RenderTitleEvent event) {
//        System.out.println("TITLE IN LISTENER: " + event.getTitle() + " | " + EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle()));
//        if (!ModConfiguration.hardcoreHeartsEnabled) return;
//
//        String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());
//
//        if (unformattedTitle.equalsIgnoreCase("BED DESTROYED!")) {
//            DisplayTweaksMod.hardcoreHearts = true;
//        }
//    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!hardcoreHeartsEnabled) return;

        String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        String pattern = "BED DESTRUCTION > Your Bed";

        if (unformatted.startsWith(pattern)) {
            DisplayTweaksMod.hardcoreHearts = true;
        }
    }

    @SubscribeEvent
    public void onChatMessageToStack(ClientChatReceivedEvent event) {
        if (stackChatMessages && !event.isCanceled() && event.type == 0) {
            GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            if (this.lastMessage.equals(event.message.getUnformattedText())) {
                guiNewChat.deleteChatLine(this.line);
                this.amount ++;
                this.lastMessage = event.message.getUnformattedText();
                event.message.appendText(EnumChatFormatting.RED + " [" + EnumChatFormatting.GRAY + "x" + this.amount + EnumChatFormatting.RED + "]");
            } else {
                this.amount = 1;
                this.lastMessage = event.message.getUnformattedText();
            }

            this.line ++;
            if (!event.isCanceled()) {
                guiNewChat.printChatMessageWithOptionalDeletion(event.message, this.line);
            }

            if (this.line > 256) {
                this.line = 0;
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        DisplayTweaksMod.hardcoreHearts = false;
    }
}
