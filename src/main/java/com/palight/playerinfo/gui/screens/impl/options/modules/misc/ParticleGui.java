package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.modules.impl.misc.ParticleMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiDropdown particlePicker;
    private GuiButton setParticleButton;

    private GuiSlider multiplierSlider;
    private GuiCheckBox forceSharp;
    private GuiCheckBox forceCrit;

    private ParticleMod module;

    private static int MIN = 0;
    private static int MAX = 100;

    public ParticleGui() {
        super("screen.particle");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = ((ParticleMod) PlayerInfo.getModules().get("particle"));
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        List<String> particleNames = new ArrayList<>();

        Arrays.asList(MCUtil.ParticleTypes.values()).forEach(type -> { particleNames.add(type.name.substring(0, 1).toUpperCase() + type.name.substring(1));});
        particleNames.add("Random");

        particlePicker = new GuiDropdown(0, buttonX, buttonY, particleNames.toArray(new String[0]));
        setParticleButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Particle");
        multiplierSlider = new GuiSlider(2, buttonX, buttonY + 32, this.xSize - (3 * (buttonX - guiX)), 20, "Particle Multiplier: ", "x", MIN, MAX, module.multiplier, false, true);
        forceSharp = new GuiCheckBox(3, buttonX, buttonY + 64, "Always Show Sharp Particles", module.forceSharp);
        forceCrit = new GuiCheckBox(4, buttonX, buttonY + 96, "Always Show Crit Particles", module.forceCrit);

        this.guiElements.addAll(Arrays.asList(
                this.particlePicker,
                this.setParticleButton,
                this.forceSharp,
                this.forceCrit
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        multiplierSlider.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setParticleButton.id) {
            module.selectedParticle = particlePicker.getSelectedItem().toLowerCase();
        } else if (widget.id == forceSharp.id) {
            module.forceSharp = forceSharp.checked;
        } else if (widget.id == forceCrit.id) {
            module.forceCrit = forceCrit.checked;
        }
        ModConfiguration.syncFromGUI();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        particlePicker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
        multiplierSlider.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
        multiplierSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_);
        module.multiplier = multiplierSlider.getValueInt();
        System.out.println("SETTING MULTIPLIER TO " + module.multiplier + " | " + multiplierSlider.sliderValue);
        ModConfiguration.syncFromGUI();
    }
}
