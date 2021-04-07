package com.palight.playerinfo.mixin.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface IMixinRender {
    @Invoker
    FontRenderer callGetFontRendererFromRenderManager();
}
