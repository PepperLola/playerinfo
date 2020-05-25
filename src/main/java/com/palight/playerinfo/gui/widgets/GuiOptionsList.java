package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.gui.screens.options.GuiOptions;
import com.palight.playerinfo.options.ModOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiOptionsList extends GuiListExtended {

    private Minecraft mc;
    private IGuiListEntry[] optionEntries;

    public GuiOptionsList(GuiOptions guiOptions, Minecraft mc, ModOption[] options) {
        super(mc, guiOptions.width, guiOptions.height, 63, guiOptions.height - 32, 20);
        this.mc = mc;

        optionEntries = new OptionEntry[options.length];

        for (int i = 0; i < options.length; i++) {
            optionEntries[i] = new OptionEntry(options[i]);
        }
    }

    public class OptionEntry implements IGuiListEntry {

        private ModOption option;

        public OptionEntry(ModOption modOption) {
            this.option = modOption;
        }

        public ModOption getOption() {
            return option;
        }

        public void setOption(ModOption option) {
            this.option = option;
        }

        @Override
        public void setSelected(int i, int i1, int i2) {

        }

        @Override
        public void drawEntry(int entry, int x, int y, int width, int height, int mouseX, int mouseY, boolean mouseDown) {
            ModOption optionEntry = ((OptionEntry) optionEntries[entry]).getOption();

            String name = optionEntry.getName();
            String desc = optionEntry.getDesc();
            String value = optionEntry.getValue().toString();

            mc.fontRendererObj.drawString(name, x, y, 0xffffffff);
            mc.fontRendererObj.drawString(desc, x, y + 10, 0xffffffff);
            mc.fontRendererObj.drawString(value, width, y, 0xffffffff);
        }

        @Override
        public boolean mousePressed(int i, int i1, int i2, int i3, int i4, int i5) {
            return false;
        }

        @Override
        public void mouseReleased(int i, int i1, int i2, int i3, int i4, int i5) {

        }
    }

    @Override
    public IGuiListEntry getListEntry(int i) {
        return optionEntries[i];
    }

    @Override
    protected int getSize() {
        return optionEntries.length;
    }
}
