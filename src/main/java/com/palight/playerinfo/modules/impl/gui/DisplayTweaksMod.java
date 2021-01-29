package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.events.RenderTitleEvent;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.DisplayTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisplayTweaksMod extends Module {

    public static boolean hardcoreHearts = false;

    public DisplayTweaksMod() {
        super("displayTweaks", "Display Tweaks", "Some useful gui/display tweaks.", ModuleType.GUI, new DisplayTweaksGui(), null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "displayModEnabled", enabled);
        ModConfiguration.syncFromGUI();
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.displayModEnabled);
    }

    @SubscribeEvent
    public void onRenderWaterOverlay(RenderBlockOverlayEvent event) {
        if (!this.isEnabled()) return;
        if (event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER) {
            event.setCanceled(ModConfiguration.disableWaterOverlay);
        }
    }

    @SubscribeEvent
    public void onRenderTitle(RenderTitleEvent event) {
        if (!ModConfiguration.hardcoreHeartsEnabled) return;

        String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());

        if (unformattedTitle.equalsIgnoreCase("BED DESTROYED!")) {
            DisplayTweaksMod.hardcoreHearts = true;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        DisplayTweaksMod.hardcoreHearts = false;
    }
}
