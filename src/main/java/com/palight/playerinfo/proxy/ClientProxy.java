package com.palight.playerinfo.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
//        Iterator iterator = Minecraft.getMinecraft().getRenderManager().getSkinMap().values().iterator();
//
//        while (iterator.hasNext()) {
//            RenderPlayer render = (RenderPlayer)iterator.next();
//            render.addLayer(new LayerCape(render));
//        }
    }
}
