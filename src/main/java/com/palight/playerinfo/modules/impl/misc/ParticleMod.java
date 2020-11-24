package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.ParticleGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class ParticleMod extends Module {
    public ParticleMod() {
        super("particle", "Particles", "Allows you to make modifications to particles.", ModuleType.MISC, new ParticleGui(), null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.particleModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "particleModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
