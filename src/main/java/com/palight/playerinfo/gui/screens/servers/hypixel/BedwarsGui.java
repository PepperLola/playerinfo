package com.palight.playerinfo.gui.screens.servers.hypixel;

import com.google.gson.Gson;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.InfoCommand;
import com.palight.playerinfo.data.PlayerProperties;
import com.palight.playerinfo.gui.widgets.GuiDropdown;
import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.palight.playerinfo.PlayerInfo.gson;

public class BedwarsGui extends GuiScreen {
    private int xSize = 176;
    private int ySize = 166;

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiDropdown modes;
    private GuiButton playButton;

    private String[] bedwars_names = new String[]{"Solos", "Doubles", "Threes", "Fours"};
    private String[] bedwars_modes = new String[]{"bedwars_eight_one", "bedwars_eight_two", "bedwars_four_three", "bedwars_four_four"};


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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/infogui.png"));
        drawTexturedModalRect((this.width - xSize) / 2, (this.height - ySize) / 2, 0, 0, xSize, ySize);

        modes.drawDropdown(mc, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
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
        Map<String, Object> servers = gson.fromJson(gson.toJson(PlayerInfo.getConfigValue("SERVERS")), HashMap.class);
        Map<String, Object> hypixel = gson.fromJson(gson.toJson(servers.get("Hypixel")), HashMap.class);

        hypixel.put("BEDWARS_MODE", mode);

        servers.put("Hypixel", hypixel);

        PlayerInfo.addConfigValue("SERVERS", servers);
    }

    public static String getBedwarsMode() {
        Map<String, Object> servers = gson.fromJson(gson.toJson(PlayerInfo.getConfigValue("SERVERS")), HashMap.class);
        Map<String, Object> hypixel = gson.fromJson(gson.toJson(servers.get("Hypixel")), HashMap.class);
        String bedwars_mode = (String) hypixel.get("BEDWARS_MODE");

        return bedwars_mode;
    }
}
