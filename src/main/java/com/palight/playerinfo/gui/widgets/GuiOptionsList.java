package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.gui.screens.options.GuiCustomConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiOptionsList extends GuiListExtended {

    private Minecraft mc;
    private IGuiListEntry[] optionEntries;

    public GuiOptionsList(GuiCustomConfig guiCustomConfig, Minecraft mc/*, ModOptions.BooleanOptions[] options*/) {
        super(mc, guiCustomConfig.width, guiCustomConfig.height, 63, guiCustomConfig.height - 32, 20);
        this.mc = mc;

//        optionEntries = new BooleanEntry[options.length];
//
//        for (int i = 0; i < options.length; i++) {
//            optionEntries[i] = new BooleanEntry(options[i]);
//        }
    }

//    public class BooleanEntry implements IGuiListEntry {
//
//        private ModOptions.BooleanOptions option;
//        private GuiCheckBox checkBox;
//
//        public BooleanEntry(ModOptions.BooleanOptions modOption) {
//            this.option = modOption;
//        }
//
//        public ModOptions getOption() {
//            return option;
//        }
//
//        public void setOption(ModOptions.BooleanOptions option) {
//            this.option = option;
//        }
//
//        @Override
//        public void setSelected(int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void drawEntry(int entry, int x, int y, int width, int height, int mouseX, int mouseY, boolean mouseDown) {
//            ModOptions optionEntry = ((BooleanEntry) optionEntries[entry]).getOption();
//
//            String name = optionEntry.getName();
//            String desc = optionEntry.getDesc();
//            Object value = optionEntry.getValue();
//
//            mc.fontRendererObj.drawString(name, x, y, 0xffffffff);
//            mc.fontRendererObj.drawString(desc, x, y + 10, 0xffffffff);
//        }
//
//        @Override
//        public boolean mousePressed(int i, int i1, int i2, int i3, int i4, int i5) {
//            return false;
//        }
//
//        @Override
//        public void mouseReleased(int i, int i1, int i2, int i3, int i4, int i5) {
//
//        }
//    }

    @Override
    public IGuiListEntry getListEntry(int i) {
        return optionEntries[i];
    }

    @Override
    protected int getSize() {
        return optionEntries.length;
    }
}
