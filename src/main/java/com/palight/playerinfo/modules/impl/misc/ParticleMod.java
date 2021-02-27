package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.ParticleGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class ParticleMod extends Module {

    @ConfigOption
    public String selectedParticle = "crit";

    public ParticleMod() {
        super("particle", "Particles", "Allows you to make modifications to particles.", ModuleType.MISC, new ParticleGui(), null);
    }
}
