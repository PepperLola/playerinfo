package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ArmorWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class ArmorMod extends Module {
    public ArmorMod() {
        super("armor", "Armor Status", "Shows which type of armor you're wearing, as well as its durability.", ModuleType.GUI, null, new ArmorWidget());
    }
}
