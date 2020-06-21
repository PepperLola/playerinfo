package com.palight.playerinfo.proxy;

import com.palight.playerinfo.listeners.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import com.palight.playerinfo.rendering.cosmetics.layers.LayerCape;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Iterator;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
        Iterator iterator = Minecraft.getMinecraft().getRenderManager().getSkinMap().values().iterator();

        while (iterator.hasNext()) {
            RenderPlayer render = (RenderPlayer)iterator.next();
            render.addLayer(new LayerCape(render));
        }
    }
}
