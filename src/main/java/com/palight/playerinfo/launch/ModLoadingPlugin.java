package com.palight.playerinfo.launch;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class ModLoadingPlugin implements IFMLLoadingPlugin {

    public ModLoadingPlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.playerinfo.json");
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();

//        if (environment.getObfuscationContext() == null) {
//            environment.setObfuscationContext("notch");
//        }

        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
