package com.palight.playerinfo.gui.screens.options;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.input.Mouse;
import java.util.Arrays;

import java.io.IOException;

public class GuiOptions extends CustomGuiScreen {
    private float amountScrolled = 0;
    private int slotHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiCheckBox blurEnabled;
    private GuiCheckBox pumpkinDisabled;

    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 32;
        buttonY = (this.height - ySize) / 2 + 32;

        blurEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable background blur", ModConfiguration.getBoolean("enableBlur", ModConfiguration.CATEGORY_GENERAL));
        pumpkinDisabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Disable pumpkin overlay", ModConfiguration.getBoolean("pumpkinOverlayDisabled", ModConfiguration.CATEGORY_GENERAL));

        this.buttonList.addAll(Arrays.asList(this.blurEnabled, this.pumpkinDisabled));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiButton button : this.buttonList) {
            button.yPosition += getScrollAmount();
        }

        amountScrolled = 0;
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        System.out.println(b.id);
    }

    @Override
    public void handleMouseInput() {
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = Integer.signum(scrollAmount);
            amountScrolled = (float)(scrollAmount * slotHeight / 2);
        }
    }

    public int getScrollAmount() {
        return (int) Math.floor(amountScrolled);
    }
}
