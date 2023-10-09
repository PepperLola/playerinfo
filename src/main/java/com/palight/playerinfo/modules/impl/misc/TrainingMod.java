package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.training.AimTrainingController;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class TrainingMod extends Module {
    public TrainingMod() {
        super("training", ModuleType.MISC, null, null);
    }

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        AimTrainingController.render();
    }
}
