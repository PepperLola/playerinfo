package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.modules.impl.misc.TimeChangerMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;
import java.util.Arrays;

public class TimeChangerGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiDropdown timePicker;
    private GuiButton setTimeButton;

    private GuiSlider fastTimeMultiplierSlider;

    public TimeChangerGui() {
        super(I18n.format("screen.timeChanger"));
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        int timesLength = TimeChangerMod.Time.values().length;
        String[] times = new String[TimeChangerMod.Time.values().length];

        for (int i = 0; i < timesLength; i++) {
            times[i] = TimeChangerMod.Time.values()[i].toString();
        }

        timePicker = new GuiDropdown(0, buttonX, buttonY, times);
        setTimeButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Time");

        fastTimeMultiplierSlider = new GuiSlider(2, buttonX, buttonY + 32, 128, 20, "Time Multiplier: ", "", 0D, 4D, ModConfiguration.fastTimeMultiplier, true, true);

        this.guiElements.addAll(Arrays.asList(
                this.timePicker,
                this.setTimeButton
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        fastTimeMultiplierSlider.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setTimeButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_PARTICLE, "selectedTime", timePicker.getSelectedItem().toLowerCase());
            ModConfiguration.syncFromGUI();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        timePicker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
        fastTimeMultiplierSlider.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
        fastTimeMultiplierSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_);
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_TIME, "fastTimeMultiplier", fastTimeMultiplierSlider.sliderValue);
        ModConfiguration.syncFromGUI();
    }
}
