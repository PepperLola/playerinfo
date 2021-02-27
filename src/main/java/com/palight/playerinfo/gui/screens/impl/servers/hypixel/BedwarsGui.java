package com.palight.playerinfo.gui.screens.impl.servers.hypixel;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

public class BedwarsGui extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiDropdown modes;
    private GuiButton playButton;

    private String[] bedwars_names = new String[]{"Solos", "Doubles", "Threes", "Fours"};
    private String[] bedwars_modes = new String[]{"bedwars_eight_one", "bedwars_eight_two", "bedwars_four_three", "bedwars_four_four"};

    public BedwarsGui() {
        super("screen.hypixel");
    }


    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        this.modes = new GuiDropdown(0, buttonX, buttonY, 80, 16, bedwars_names);

        String bedwars_mode = getBedwarsMode();

        modes.setSelectedItem(bedwars_names[ArrayUtils.indexOf(bedwars_modes, bedwars_mode)]);

        playButton = new GuiButton(0, buttonX + 84, buttonY - 2, 64, 20, "Play");
        this.buttonList.add(playButton);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == 0) {
//            System.out.println(getBedwarsMode());
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play " + getBedwarsMode());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        modes.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        modes.mousePressed(mc, x, y, btn);

        String selectedItem = modes.getSelectedItem();

        setBedwarsMode(bedwars_modes[ArrayUtils.indexOf(bedwars_names, selectedItem)]);

        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void setBedwarsMode(String mode) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_HYPIXEL, "bedwarsMode", mode);
    }

    public static String getBedwarsMode() {
        return ModConfiguration.getString(ModConfiguration.CATEGORY_HYPIXEL, "bedwarsMode");
    }
}
