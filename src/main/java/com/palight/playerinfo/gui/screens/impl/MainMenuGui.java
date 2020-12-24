package com.palight.playerinfo.gui.screens.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.impl.GuiTexturedButton;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.Arrays;

public class MainMenuGui extends GuiScreen {

    private double timeExisted = 0;
    private int ticksExisted = 0;

    private GuiButton singlePlayerButton;
    private GuiButton multiPlayerButton;
    private GuiButton quitButton;

    public MainMenuGui() {

    }

    @Override
    public void initGui() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        // setup buttons
        this.singlePlayerButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, I18n.format("menu.singleplayer"));
        this.multiPlayerButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48 + 24, I18n.format("menu.multiplayer"));
        this.quitButton = new GuiTexturedButton(3, res.getScaledWidth() - 22, 2, 20, 20, 20, 0);

        this.buttonList.addAll(Arrays.asList(
                this.singlePlayerButton,
                this.multiPlayerButton,
                this.quitButton
        ));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
//        System.out.println("CHROMA: " + ModConfiguration.mainMenuChroma);
        if (ModConfiguration.mainMenuChroma) {
            this.drawGradientRect(0, 0, this.width, this.height, ColorUtil.getChromaColor(), ColorUtil.getChromaColor(1000));
            this.drawString(this.fontRendererObj, "Made by palight", 4, this.height - 20, 0xffffffff);

            String commitText = "playerinfo (" + PlayerInfo.commitHash.substring(0, 7) + " / " + PlayerInfo.defaultBranchName + ")";
            this.drawString(this.fontRendererObj, commitText, 4, this.height - 10, 0xffffffff);
        } else {
            this.drawGradientRect(0, 0, this.width, this.height, 0xff000000, 0xff000000);
            int intensity = NumberUtil.roundDown((int) Math.round(128 + (Math.sin(timeExisted) * 128)), 255);
            this.drawString(this.fontRendererObj, "Made by palight", 4, this.height - 20, ColorUtil.getColorInt(intensity, intensity, intensity));

            String commitText = "playerinfo (" + PlayerInfo.commitHash.substring(0, 7) + " / " + PlayerInfo.defaultBranchName + ")";
            this.drawString(this.fontRendererObj, commitText, 4, this.height - 10, ColorUtil.getColorInt(intensity, intensity, intensity));

            timeExisted = ((2 * Math.PI) / 200) * ticksExisted;
            ticksExisted++;
            if (ticksExisted > 200) {
                ticksExisted = 0;
            }
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
        } else if (b.id == quitButton.id) {
            this.mc.shutdown();
        }

        super.actionPerformed(b);
    }
}
