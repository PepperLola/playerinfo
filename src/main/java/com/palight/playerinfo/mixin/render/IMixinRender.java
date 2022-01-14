package com.palight.playerinfo.mixin.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface IMixinRender<T extends Entity> {
    @Invoker
    FontRenderer callGetFontRendererFromRenderManager();
    @Invoker
    boolean callBindEntityTexture(T entity);
    @Accessor
    RenderManager getRenderManager();
}
