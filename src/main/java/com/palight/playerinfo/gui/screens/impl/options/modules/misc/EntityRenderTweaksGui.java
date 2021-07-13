package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiColorPicker;
import com.palight.playerinfo.modules.impl.misc.EntityRenderTweaksMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.Arrays;

public class EntityRenderTweaksGui extends CustomGuiScreenScrollable {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiColorPicker colorPicker;
    private GuiButton colorButton;
    private EntityRenderTweaksMod module;

    public EntityRenderTweaksGui() {
        super("screen.entityRenderTweaks");
    }

    @Override
    public void initGui() {
        if (module == null) {
            module = ((EntityRenderTweaksMod) PlayerInfo.getModules().get("entityRenderTweaks"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        colorPicker = new GuiColorPicker(0, buttonX + 8, buttonY + 8, 48, 64);
        colorPicker.setColor(module.hitTintColor);

        colorButton = new GuiButton(1, buttonX + 8, buttonY + 84, 48, 20, "Set Color");

        this.guiElements.addAll(Arrays.asList(
                this.colorPicker,
                this.colorButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == colorButton.id) {
            module.hitTintColor = colorPicker.getColor();
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1);
        colorPicker.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        if (module == null) {
            module = ((EntityRenderTweaksMod) PlayerInfo.getModules().get("entityRenderTweaks"));
        }

        colorPicker.mousePressed();

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
}
