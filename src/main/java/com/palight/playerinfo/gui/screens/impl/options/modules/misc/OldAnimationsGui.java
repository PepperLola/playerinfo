package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.misc.OldAnimationsMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class OldAnimationsGui extends CustomGuiScreenScrollable {
    private GuiCheckBox blockHitAnimationEnabled;
    private GuiCheckBox bowAnimationEnabled;
    private GuiCheckBox rodAnimationEnabled;
    private GuiCheckBox eatingAnimationEnabled;

    private OldAnimationsMod module;

    public OldAnimationsGui() {
        super("screen.oldAnimations");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = ((OldAnimationsMod) PlayerInfo.getModules().get("oldAnimations"));
        }

        int buttonX = guiX + leftOffset + 6;
        int buttonY = guiY + headerHeight + 16;

        blockHitAnimationEnabled = new GuiCheckBox(0, buttonX, buttonY, "Block Hit Animation Enabled", module.blockHitAnimationEnabled);
        bowAnimationEnabled = new GuiCheckBox(1, buttonX, buttonY + 20, "Bow Animation Enabled", module.bowAnimationEnabled);
        rodAnimationEnabled = new GuiCheckBox(2, buttonX, buttonY + 40, "Rod Animation Enabled", module.rodAnimationEnabled);
        eatingAnimationEnabled = new GuiCheckBox(3, buttonX, buttonY + 60, "Eating Animation Enabled", module.eatingAnimationEnabled);

        this.guiElements.addAll(Arrays.asList(
                this.blockHitAnimationEnabled,
                this.bowAnimationEnabled,
                this.rodAnimationEnabled,
                this.eatingAnimationEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == blockHitAnimationEnabled.id) {
            module.blockHitAnimationEnabled = blockHitAnimationEnabled.checked;
        } else if (widget.id == bowAnimationEnabled.id) {
            module.bowAnimationEnabled = bowAnimationEnabled.checked;
        } else if (widget.id == rodAnimationEnabled.id) {
            module.rodAnimationEnabled = rodAnimationEnabled.checked;
        } else if (widget.id == eatingAnimationEnabled.id) {
            module.eatingAnimationEnabled = eatingAnimationEnabled.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
