package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.EntityRenderTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.ColorUtil;

public class EntityRenderTweaksMod extends Module {

    @ConfigOption
    public int hitTintColor = ColorUtil.getColorInt(255, 0, 0, 75);

    @ConfigOption
    public boolean doArmorTint = true;

    public EntityRenderTweaksMod() {
        super("entityRenderTweaks", ModuleType.MISC, new EntityRenderTweaksGui(), null);
    }
}
