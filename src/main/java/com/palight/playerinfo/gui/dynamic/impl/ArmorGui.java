package com.palight.playerinfo.gui.dynamic.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.gui.dynamic.components.GuiCheckbox;
import com.palight.playerinfo.gui.dynamic.components.GuiStack;
import com.palight.playerinfo.modules.impl.gui.ArmorMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.math.Vector2;

public class ArmorGui extends DynamicGuiScreen {

    private GuiCheckbox hideDurability;

    private ArmorMod module;

    public ArmorGui() {
        super(new Vector2<>(512, 472));
    }

    @Override
    public void setup() {
        if (module == null) {
            module = (ArmorMod) PlayerInfo.getModules().get("armor");
        }

        GuiStack stack = this.createStack(4, 4, 4);

        stack.addComponent(this.createLabel("Armor", 4, 4));
        stack.addComponent(this.createSpacer(0, 0, 0, 16));

        this.hideDurability = this.createCheckbox("Hide Durability", 4, 0, 20, 20, module.hideDurability)
            .setOnClick((mousePos, mouseButton) -> {
                module.hideDurability = hideDurability.isEnabled();
                ModConfiguration.syncFromGUI();
            });

        stack.addComponent(this.hideDurability);
    }
}
