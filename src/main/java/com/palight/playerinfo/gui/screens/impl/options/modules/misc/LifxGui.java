package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiColorPicker;
import com.palight.playerinfo.modules.impl.misc.LifxMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.HttpUtil;
import com.palight.playerinfo.util.HttpUtilResponseHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LifxGui extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiColorPicker colorPicker;
    private GuiButton colorButton;
    private GuiButton selectorSubmit;
    private GuiButton tokenSubmit;

    private GuiTextField tokenField;
    private GuiTextField selectorField;

    private GuiCheckBox teamMode;

    public static int TEAM_COLOR;
    public static boolean TEAM_MODE;

    private LifxMod module;

    public LifxGui() {
        super("screen.lifx");
    }

    @Override
    public void initGui() {
        TEAM_MODE = getTeamMode();

        if (module == null) {
            module = ((LifxMod) PlayerInfo.getModules().get("lifx"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        colorPicker = new GuiColorPicker(0, buttonX + 8, buttonY + 8, 48, 64);

        colorButton = new GuiButton(0, buttonX + 8, buttonY + 84, 48, 20, "Submit");

        selectorField = new GuiTextField(0, this.fontRendererObj, buttonX + 4, (this.height + ySize) / 2 - 64, 128, 18);
        tokenField = new GuiTextField(0, this.fontRendererObj, buttonX + 4, (this.height + ySize) / 2 - 32, 128, 18);
        tokenField.setMaxStringLength(64);

        selectorSubmit = new GuiButton(1, buttonX + 134, (this.height + ySize) / 2 - 65, 64, 20, "Set Selector");
        tokenSubmit = new GuiButton(2, buttonX + 134, (this.height + ySize) / 2 - 33, 64, 20, "Set Token");

        this.buttonList.addAll(Arrays.asList(this.colorButton, this.selectorSubmit, this.tokenSubmit));

        teamMode = new GuiCheckBox(0, buttonX + 8, (height + ySize) / 2 - 20, "Team Mode", getTeamMode());

        this.guiElements.addAll(Arrays.asList(
            colorPicker, teamMode
        ));

    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == colorButton.id) {
            int color = colorPicker.getColor();
            setColor(color);
        } else if (b.id == selectorSubmit.id) {
            if (selectorField.getText().equals("")) return;

            setSelector(selectorField.getText());
        } else if (b.id == tokenSubmit.id) {
            if (tokenField.getText().equals("")) return;
            setToken(tokenField.getText());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tokenField.updateCursorCounter();
        selectorField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1);
        colorPicker.drawWidget(mc, mouseX, mouseY);

        selectorField.drawTextBox();
        tokenField.drawTextBox();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        colorPicker.mousePressed();
        selectorField.mouseClicked(x, y, btn);
        tokenField.mouseClicked(x, y, btn);
        teamMode.mouseClicked(x, y);

        setTeamMode(teamMode.checked);

        super.mouseClicked(x, y, btn);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        colorPicker.mouseReleased();
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char p1, int p2) throws IOException {
        super.keyTyped(p1, p2);
        selectorField.textboxKeyTyped(p1, p2);
        tokenField.textboxKeyTyped(p1, p2);
    }

    public static void setTeamColor(int color) {
        if (TEAM_COLOR == color) return;

        TEAM_COLOR = color;

        if (TEAM_MODE) {
            setColor(color);
        }
    }

    public static void setColor(int color) {

        int red = (int) Math.floor((color >> 16) & 255);
        int green = (int) Math.floor((color >> 8) & 255);
        int blue = (int) Math.floor(color & 255);
        int alpha = (int) Math.floor((color >> 24) & 255);

        String token = getToken();

        if (token.equals("")) return;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);

        HttpUtil.httpPut(String.format("https://api.lifx.com/v1/lights/%s/state", ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxSelector), headers, String.format("{\"power\": \"on\", \"color\": \"rgb:%d,%d,%d\",\"brightness\":%f}", red, green, blue, alpha / 255.0), new HttpUtilResponseHandler() {
            @Override
            public void handleResponse(HttpResponse response) {
                System.out.println(response.getStatusLine().getStatusCode());
            }
        });
    }

    public static void setToken(String token) {
        ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxToken = token;
        ModConfiguration.syncFromGUI();
    }

    public static String getToken() {
        return ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxToken;
    }

    public static void setSelector(String selector) {
        ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxSelector = selector;
        ModConfiguration.syncFromGUI();
    }

    public static String getSelector() {
        return ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxSelector;
    }

    public static void setTeamMode(boolean teams) {
        TEAM_MODE = teams;
        ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxTeamMode = teams;
        ModConfiguration.syncFromGUI();
    }

    public static boolean getTeamMode() {
        return ((LifxMod) PlayerInfo.getModules().get("lifx")).lifxTeamMode;
    }
}
