package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.modules.impl.misc.AutoGGMod;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.Arrays;

public class AutoGGGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiTextField messageField;
    private GuiButton setMessage;
    private GuiTextField delaysField;
    private GuiButton setDelays;

    private AutoGGMod module;

    public AutoGGGui() {
        super("screen.autogg");
    }

    @Override
    public void initGui() {
        super.initGui();
        if (module == null) {
            module = ((AutoGGMod) PlayerInfo.getModules().get("autogg"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        messageField = new GuiTextField(0, this.fontRendererObj, buttonX + 4, buttonY, 128, 18);
        messageField.setText(module.messages);
        setMessage = new GuiButton(1, buttonX + 4 + 128, buttonY, 48, 20, "Set Message");

        delaysField = new GuiTextField(2, this.fontRendererObj, buttonX + 4, buttonY + 32, 128, 18);
        delaysField.setText(module.delays);
        setDelays = new GuiButton(3, buttonX + 4 + 128, buttonY + 32, 48, 20, "Set Delays");

        this.guiElements.addAll(Arrays.asList(
            this.setMessage,
            this.setDelays
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setMessage.id) {
            module.messages = messageField.getText();
        } else if (widget.id == setDelays.id) {
            module.delays = delaysField.getText();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        messageField.drawTextBox();
        delaysField.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        messageField.updateCursorCounter();
        delaysField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        messageField.mouseClicked(mouseX, mouseY, btn);
        delaysField.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void keyTyped(char character, int p_keyTyped_2_) throws IOException {
        super.keyTyped(character, p_keyTyped_2_);
        messageField.textboxKeyTyped(character, p_keyTyped_2_);
        delaysField.textboxKeyTyped(character, p_keyTyped_2_);
    }
}
