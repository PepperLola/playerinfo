package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.GuiIngameCustom;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.ScoreboardGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScoreboardMod extends Module {

    private static GuiIngame ingame = null;

    public ScoreboardMod() {
        super("scoreboard", "Scoreboard", "Customize your scoreboard!", ModuleType.GUI, new ScoreboardGui(), new ScoreboardWidget(Minecraft.getMinecraft()));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // if Minecraft initialized and ingame gui is still default, set it to ours
        if (isItemRendererInitialized() && !isIngameGuiOurs()) {
            setIngameGuiToOurs();
        }
    }

    private synchronized static GuiIngame getCustomGui() {
        if (ingame == null) {
            ingame = new GuiIngameCustom(Minecraft.getMinecraft());
        }
        return ingame;
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.scoreboardModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "scoreboardModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    void setIngameGuiToOurs() {
        Minecraft.getMinecraft().ingameGUI = ScoreboardMod.getCustomGui();
    }

    boolean isItemRendererInitialized() {
        return Minecraft.getMinecraft().getRenderItem() != null;
    }

    boolean isIngameGuiOurs() {
        return Minecraft.getMinecraft().ingameGUI instanceof GuiIngameCustom;
    }
}
