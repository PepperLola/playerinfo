package com.palight.playerinfo.gui.dynamic.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.gui.dynamic.components.GuiCheckbox;
import com.palight.playerinfo.modules.impl.gui.ArmorMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.math.Vector2;

public class ArmorGui extends DynamicGuiScreen {

    private GuiCheckbox hideDurability;

    private ArmorMod module;

    public ArmorGui() {
        super("screen.armor", new Vector2<>(512, 472));
    }

    @Override
    public void setup() {
        if (module == null) {
            module = (ArmorMod) PlayerInfo.getModules().get("armor");
        }

        this.hideDurability = this.createCheckbox("Hide Durability", 0, 0, 20, 20, module.hideDurability)
            .setOnClick((mousePos, mouseButton) -> {
                module.hideDurability = hideDurability.isEnabled();
                ModConfiguration.syncFromGUI();
            });

        stack.addComponent(this.hideDurability);
    }
}
