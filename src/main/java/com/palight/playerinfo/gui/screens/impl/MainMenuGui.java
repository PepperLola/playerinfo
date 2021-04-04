package com.palight.playerinfo.gui.screens.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.impl.GuiTexturedButton;
import com.palight.playerinfo.modules.impl.gui.CustomMainMenuMod;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;

public class MainMenuGui extends GuiScreen {

    private double timeExisted = 0;
    private int ticksExisted = 0;

    private GuiButton singlePlayerButton;
    private GuiButton multiPlayerButton;
    private GuiButton quitButton;
    private GuiTexturedButton optionsButton;

    public MainMenuGui() {

    }

    @Override
    public void initGui() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        // setup buttons
        this.singlePlayerButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, I18n.format("menu.singleplayer"));
        this.multiPlayerButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48 + 24, I18n.format("menu.multiplayer"));
        this.optionsButton = new GuiTexturedButton(3, this.width - 20 - 2, this.height - 30 - 2, 20, 20, 0, 0);
        this.quitButton = new GuiTexturedButton(4, res.getScaledWidth() - 22, 2, 20, 20, 1, 0);


        this.buttonList.addAll(Arrays.asList(
                this.singlePlayerButton,
                this.multiPlayerButton,
                this.optionsButton,
                this.quitButton
        ));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
//        System.out.println("CHROMA: " + ModConfiguration.mainMenuChroma);

        CustomMainMenuMod mainMenuMod = (CustomMainMenuMod) PlayerInfo.getModules().get("mainMenu");

        if (mainMenuMod != null && mainMenuMod.mainMenuChroma) {
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

        mc.getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "icons/playerinfo_icon.png"));
        int xOffset, yOffset, targetWidth, targetHeight, scaledWidth, scaledHeight, imageWidth, imageHeight;
        xOffset = yOffset = 0;
        targetWidth = targetHeight = 1024;
        scaledWidth = scaledHeight = 96;
        imageWidth = imageHeight = 1024;
        /**
         * Arguments
         * 1: X position
         * 2: Y position
         * 3: X offset of target portion from top left of image
         * 4: Y offset "
         * 5: Width of target portion
         * 6: Height of target portion
         * 7: Scaled width to display
         * 8: Scaled height to display
         * 9: Total width of image (specified in resource location)
         * 10: Total height of image
         */
        Gui.drawScaledCustomSizeModalRect(
                (this.width - scaledWidth) / 2,
                this.height / 4 + 48 - scaledHeight - 16,
                xOffset,
                yOffset,
                targetWidth,
                targetHeight,
                scaledWidth,
                scaledHeight,
                imageWidth,
                imageHeight
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == singlePlayerButton.id) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (b.id == multiPlayerButton.id) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (b.id == optionsButton.id) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (b.id == quitButton.id) {
            this.mc.shutdown();
        }

        super.actionPerformed(b);
    }
}
