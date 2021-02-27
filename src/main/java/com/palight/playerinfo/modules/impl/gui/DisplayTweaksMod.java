package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.screens.impl.options.modules.gui.DisplayTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
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
    public static boolean hardcoreHearts = false;

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

        String unformatted = event.message.getUnformattedText();
        String pattern = "BED DESTRUCTION > Your Bed";

        if (unformatted.startsWith(pattern)) {
            DisplayTweaksMod.hardcoreHearts = true;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        DisplayTweaksMod.hardcoreHearts = false;
    }
}
