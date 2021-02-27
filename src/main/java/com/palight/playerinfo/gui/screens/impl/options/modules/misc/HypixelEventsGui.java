package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.modules.impl.misc.HypixelEventsMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.Arrays;

public class HypixelEventsGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiDropdown alertSound;
    private GuiButton setSoundButton;
    private GuiCheckBox friendAlertCheckbox;

    private final HypixelEventsMod module;

    public HypixelEventsGui() {
        super("screen.hypixelEventsGui");
        module = ((HypixelEventsMod) PlayerInfo.getModules().get("hypixelEvents"));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        alertSound = new GuiDropdown(0, buttonX, buttonY, new String[]{"None", "Ping"});
        setSoundButton = new GuiButton(1, buttonX + 64, buttonY - 2, 64, 20, "Set Sound");

        friendAlertCheckbox = new GuiCheckBox(2, buttonX, buttonY + 48, "Enable friend alerts", module.friendAlerts);

        this.guiElements.addAll(Arrays.asList(
                this.alertSound,
                this.setSoundButton,
                this.friendAlertCheckbox
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);

        if (widget.id == friendAlertCheckbox.id) {
            module.friendAlerts = friendAlertCheckbox.checked;
        } else if (widget.id == setSoundButton.id) {
            String sound = "none";
            switch (alertSound.getSelectedItem()) {
                case "Ping":
                    sound = "note.pling";
            }
            module.alertSound = sound;
        }

        ModConfiguration.syncFromGUI();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        alertSound.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
