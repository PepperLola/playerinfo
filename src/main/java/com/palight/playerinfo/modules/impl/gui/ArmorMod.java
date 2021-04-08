package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ArmorWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.ArmorGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class ArmorMod extends Module {
    @ConfigOption
    public boolean transparentBackground = false;
    @ConfigOption
    public boolean hideDurability = false;
    public ArmorMod() {
        super("armor", "Armor Status", "Shows which type of armor you're wearing, as well as its durability.", ModuleType.GUI, new ArmorGui(), new ArmorWidget());
    }
}
