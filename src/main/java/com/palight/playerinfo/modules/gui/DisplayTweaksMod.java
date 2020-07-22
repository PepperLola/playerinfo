package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.gui.screens.options.modules.gui.DisplayTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisplayTweaksMod extends Module {
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
    public void onRenderFireOverlay(RenderBlockOverlayEvent event) {
        if (!this.isEnabled()) return;
        if (event.overlayType == RenderBlockOverlayEvent.OverlayType.FIRE) {

        } else if (event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER) {
            event.setCanceled(ModConfiguration.disableWaterOverlay);
        }
    }
}
