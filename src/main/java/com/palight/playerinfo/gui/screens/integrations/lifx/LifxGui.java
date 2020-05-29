package com.palight.playerinfo.gui.screens.integrations.lifx;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiColorPicker;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.HttpUtil;
import com.palight.playerinfo.util.HttpUtilResponseHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.palight.playerinfo.PlayerInfo.gson;

public class LifxGui extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiColorPicker colorPicker;
    private GuiButton colorButton;
    private GuiButton submit;

    private GuiTextField tokenField;

    private GuiCheckBox teamMode;

    public static int TEAM_COLOR;
    public static boolean TEAM_MODE = getTeamMode();

    public LifxGui() {
        super("Lifx");
    }

    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2;
        buttonY = (this.height - ySize) / 2;

        colorPicker = new GuiColorPicker(0, buttonX + 8, buttonY + 16, 48, 64);

        colorButton = new GuiButton(0, buttonX + 8, buttonY + 84, 48, 20, "Submit");

        tokenField = new GuiTextField(0, this.fontRendererObj, buttonX + 1, (this.height + ySize) / 2 + 1, xSize - 49, 18);
        submit = new GuiButton(1, (this.width + xSize) / 2 - 46, (this.height + ySize) / 2, 46, 20, "Submit");

        this.buttonList.addAll(Arrays.asList(this.colorButton, this.submit));

        teamMode = new GuiCheckBox(0, buttonX + 8, (height + ySize) / 2 - 20, "Team Mode", getTeamMode());
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == 0) {
            int color = colorPicker.getColor();

            setColor(color);
        } else if (b.id == 1) {
            setToken(tokenField.getText());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tokenField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        colorPicker.drawPicker(mc, mouseX, mouseY);

        tokenField.drawTextBox();

        teamMode.drawButton(mc, mouseX, mouseY);

        fontRendererObj.drawString("LIFX Integration", (width - xSize) / 2 + 8, (height - ySize) / 2 + 8, 0);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        colorPicker.mousePressed();
        tokenField.mouseClicked(x, y, btn);
        teamMode.mousePressed(mc, x, y);

        setTeamMode(teamMode.isChecked());

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

        String token = getToken();

        if (token.equals("")) return;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);

        HttpUtil.httpPut("https://api.lifx.com/v1/lights/d073d527d7f3/state", headers, String.format("{\"power\": \"on\", \"color\": \"rgb:%d,%d,%d\"}", red, green, blue), new HttpUtilResponseHandler() {
            @Override
            public void handleResponse(HttpResponse response) {
                System.out.println(response.getStatusLine().getStatusCode());
            }
        });
    }

    public static void setToken(String token) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_LIFX, "lifxToken", token);
    }

    public static String getToken() {
        return ModConfiguration.getString(ModConfiguration.CATEGORY_LIFX, "lifxToken");
    }

    public static void setTeamMode(boolean teams) {
        TEAM_MODE = teams;
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_LIFX, "lifxTeamMode", teams);
    }

    public static boolean getTeamMode() {
        return ModConfiguration.getBoolean(ModConfiguration.CATEGORY_LIFX, "lifxTeamMode");
    }
}
