package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.Arrays;

public class MainMenuGui extends GuiScreen {

    private double timeExisted = 0;
    private int ticksExisted = 0;

    private GuiButton singlePlayerButton;
    private GuiButton multiPlayerButton;

    public MainMenuGui() {

    }

    @Override
    public void initGui() {

        // setup buttons
        this.singlePlayerButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, I18n.format("menu.singleplayer"));
        this.multiPlayerButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48 + 24, I18n.format("menu.multiplayer"));

        this.buttonList.addAll(Arrays.asList(this.singlePlayerButton, this.multiPlayerButton));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        this.drawDefaultBackground();
        this.drawGradientRect(0, 0, this.width, this.height, 0x00000000, 0x00000000);
        int intensity = NumberUtil.roundDown((int) Math.round(128 + (Math.sin(timeExisted) * 128)), 255);
        this.drawString(this.fontRendererObj, "Made by palight", 4, this.height - 10, ColorUtil.getColorInt(intensity, intensity, intensity));
        timeExisted = ((2 * Math.PI) / 200) * ticksExisted;
        ticksExisted++;
        if (ticksExisted > 200) {
            ticksExisted = 0;
        }
        String copyright = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRendererObj, copyright, this.width - this.fontRendererObj.getStringWidth(copyright), this.height - 10, 0xffffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == singlePlayerButton.id) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (b.id == multiPlayerButton.id) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        super.actionPerformed(b);
    }
}
