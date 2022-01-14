package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.modules.impl.misc.ItemPhysicsMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class ItemPhysicsGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiSlider rotateSpeedSlider;

    private ItemPhysicsMod module;

    public ItemPhysicsGui() {
        super("screen.itemPhysics");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (ItemPhysicsMod) PlayerInfo.getModules().get("itemPhysics");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        rotateSpeedSlider = new GuiSlider(2, buttonX, buttonY + 32, 128, 20, "Rotate Speed: ", "", 0D, 4D, module.rotateSpeed, true, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        rotateSpeedSlider.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        rotateSpeedSlider.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int btn) {
        super.mouseReleased(mouseX, mouseY, btn);
        rotateSpeedSlider.mouseReleased(mouseX, mouseY);
        module.rotateSpeed = rotateSpeedSlider.getValue();
        ModConfiguration.syncFromGUI();
    }
}
