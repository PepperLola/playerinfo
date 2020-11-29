package com.palight.playerinfo.gui.screens.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.screens.impl.options.modules.misc.LifxGui;
import com.palight.playerinfo.gui.screens.impl.servers.ServerSelector;
import com.palight.playerinfo.gui.screens.impl.util.CalculatorGui;
import com.palight.playerinfo.gui.widgets.impl.GuiMenuBar;
import com.palight.playerinfo.gui.widgets.impl.GuiModuleEntry;
import com.palight.playerinfo.gui.widgets.impl.GuiTexturedButton;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGui extends CustomGuiScreenScrollable {

    private final int buttonWidth = 64;
    private final int buttonHeight = 20;

    private final int hPadding = 2;
    private final int columns = 2;
    private int columnWidth;
    private final int rowHeight = 32;

    private int buttonX;
    private int buttonY;

    private GuiMenuBar menuBar;

    private GuiTexturedButton settingsGuiButton;
    private GuiButton editModsButton;

    private GuiTextField filterTextInput;

    List<GuiModuleEntry> modules = new ArrayList<>();

    public MainGui() {
        super(I18n.format("screen.main"));
    }


    @Override
    public void initGui() {
        super.initGui();

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        columnWidth = ((xSize - (3 * leftOffset)) - (2 * hPadding)) / columns;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        this.filterTextInput = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, (this.width + xSize) / 2 - 128, (this.height - ySize) / 2 - 18, 128, 18);

        renderModules();

        this.menuBar = new GuiMenuBar(1, (width - xSize) / 2 + 67, (height - ySize) / 2 + 5, 180, 16, new CustomGuiScreen[]{new InfoGui(), new ServerSelector(), new LifxGui(), new CalculatorGui()});

        this.settingsGuiButton = new GuiTexturedButton(2, (width - xSize) / 2 + 226 - 24, (this.height + ySize) / 2 - 24, 20, 20, 0, 0);
        this.editModsButton = new GuiButton(3, guiX + leftOffset + 2, (height + ySize) / 2 - 24, 48, 20, "Edit GUI");

        this.buttonList.addAll(Arrays.asList(
                this.settingsGuiButton,
                this.editModsButton
        ));
    }

    private void renderModules() {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(gui);
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);

        modules.clear();

        List<Module> filteredModules = new ArrayList<>();

        System.out.println("FILTER TEXT: " + filterTextInput.getText());
        if (filterTextInput.getText().equals("")) {
            filteredModules.addAll(PlayerInfo.getModules().values());
        } else {
            for (Module module : PlayerInfo.getModules().values()) {
                String filterText = filterTextInput.getText().toLowerCase();
                if (module.getName().toLowerCase().contains(filterText) || module.getDescription().toLowerCase().contains(filterText)) {
                    filteredModules.add(module);
                }
            }
        }

        for (int i = 0; i < filteredModules.size(); i++) {
            Module module = filteredModules.get(i);
            int xPosition = guiX + leftOffset + hPadding + (columnWidth * (i % columns));
            int yPosition = guiY + headerHeight + (i - (i % columns)) * rowHeight + 4;
            GuiModuleEntry entry = new GuiModuleEntry(this, i, module, xPosition, yPosition, columnWidth, rowHeight);
            entry.init();
            modules.add(entry);
        }

        this.guiElements.clear();
        this.guiElements.addAll(modules);
    }

    @Override
    protected void keyTyped(char typed, int modifier) throws IOException {
        super.keyTyped(typed, modifier);
        if (filterTextInput.isFocused()) {
            filterTextInput.textboxKeyTyped(typed, modifier);
            renderModules();
        }
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        System.out.println(b.id);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == settingsGuiButton.id) {
            player.openGui(PlayerInfo.instance, GuiHandler.SETTINGS_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == editModsButton.id) {
            player.openGui(PlayerInfo.instance, GuiHandler.EDIT_MODS_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
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
        menuBar.drawWidget(mc, mouseX, mouseY);
        this.filterTextInput.drawTextBox();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        menuBar.mouseClicked(x, y);
        filterTextInput.mouseClicked(x, y, btn);
        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void openGui() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();
        player.openGui(PlayerInfo.instance, GuiHandler.MAIN_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
    }

    public static void closeGui() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.closeScreen();
    }
}
