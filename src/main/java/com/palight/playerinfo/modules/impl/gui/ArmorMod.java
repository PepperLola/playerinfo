package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ArmorWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class ArmorMod extends Module {
    public ArmorMod() {
        super("armor", "Armor Status", "Shows which type of armor you're wearing, as well as its durability.", ModuleType.GUI, null, new ArmorWidget());
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.armorModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "armorModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
