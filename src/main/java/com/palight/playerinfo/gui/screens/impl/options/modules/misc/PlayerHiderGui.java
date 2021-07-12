package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.modules.impl.misc.PlayerHiderMod;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.Arrays;

public class PlayerHiderGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiTextField patternField;
    private GuiButton setPattern;

    private PlayerHiderMod module;

    public PlayerHiderGui() {
        super("screen.playerHider");
    }

    @Override
    public void initGui() {
        super.initGui();
        if (module == null) {
            module = ((PlayerHiderMod) PlayerInfo.getModules().get("playerHider"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        patternField = new GuiTextField(0, this.fontRendererObj, buttonX + 4, buttonY, 128, 18);
        patternField.setText(module.showNamePattern);
        setPattern = new GuiButton(1, buttonX + 4 + 128, buttonY, 48, 20, "Set Pattern");

        this.guiElements.addAll(Arrays.asList(
            this.setPattern
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setPattern.id) {
            module.showNamePattern = patternField.getText();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        patternField.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        patternField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        patternField.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void keyTyped(char character, int p_keyTyped_2_) throws IOException {
        super.keyTyped(character, p_keyTyped_2_);
        patternField.textboxKeyTyped(character, p_keyTyped_2_);
    }
}
