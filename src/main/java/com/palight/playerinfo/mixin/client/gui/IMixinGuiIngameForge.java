package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiIngameForge.class)
public interface IMixinGuiIngameForge {
    @Invoker void callBind(ResourceLocation res);
    @Invoker boolean callPre(RenderGameOverlayEvent.ElementType type);
    @Invoker void callPost(RenderGameOverlayEvent.ElementType type);
}
