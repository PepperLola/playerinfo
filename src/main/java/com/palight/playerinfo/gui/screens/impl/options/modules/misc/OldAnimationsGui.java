package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class OldAnimationsGui extends CustomGuiScreenScrollable {
    private GuiCheckBox blockHitAnimationEnabled;
    private GuiCheckBox bowAnimationEnabled;
    private GuiCheckBox rodAnimationEnabled;
    private GuiCheckBox eatingAnimationEnabled;
    private GuiCheckBox swordAnimationEnabled;
    private GuiCheckBox heldAnimationEnabled;

    public OldAnimationsGui() {
        super(I18n.format("screen.oldAnimations"));
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonX = guiX + leftOffset + 6;
        int buttonY = guiY + headerHeight + 16;

        blockHitAnimationEnabled = new GuiCheckBox(0, buttonX, buttonY, "Block Hit Animation Enabled", ModConfiguration.blockHitAnimationEnabled);
        bowAnimationEnabled = new GuiCheckBox(1, buttonX, buttonY + 20, "Bow Animation Enabled", ModConfiguration.bowAnimationEnabled);
        rodAnimationEnabled = new GuiCheckBox(2, buttonX, buttonY + 40, "Rod Animation Enabled", ModConfiguration.rodAnimationEnabled);
        eatingAnimationEnabled = new GuiCheckBox(3, buttonX, buttonY + 60, "Eating Animation Enabled", ModConfiguration.eatingAnimationEnabled);
        swordAnimationEnabled = new GuiCheckBox(4, buttonX, buttonY + 80, "Sword Animation Enabled", ModConfiguration.swordAnimationEnabled);
        heldAnimationEnabled = new GuiCheckBox(5, buttonX, buttonY + 100, "Held Item Enabled", ModConfiguration.heldAnimationEnabled);

        this.guiElements.addAll(Arrays.asList(
                this.blockHitAnimationEnabled,
                this.bowAnimationEnabled,
                this.rodAnimationEnabled,
                this.eatingAnimationEnabled,
                this.swordAnimationEnabled,
                this.heldAnimationEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == blockHitAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "blockHitAnimationEnabled", blockHitAnimationEnabled.checked);
        } else if (widget.id == bowAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "bowAnimationEnabled", bowAnimationEnabled.checked);
        } else if (widget.id == rodAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "rodAnimationEnabled", rodAnimationEnabled.checked);
        } else if (widget.id == eatingAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "eatingAnimationEnabled", eatingAnimationEnabled.checked);
        } else if (widget.id == swordAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "swordAnimationEnabled", swordAnimationEnabled.checked);
        } else if (widget.id == heldAnimationEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_ANIMATIONS, "heldAnimationEnabled", heldAnimationEnabled.checked);
        }
        ModConfiguration.syncFromGUI();
    }
}
