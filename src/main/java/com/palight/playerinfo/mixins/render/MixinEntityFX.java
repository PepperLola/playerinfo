package com.palight.playerinfo.mixins.render;

import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.random.RandomUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
public abstract class MixinEntityFX {
    @Shadow protected TextureAtlasSprite particleIcon;

    @Shadow public abstract void setParticleTextureIndex(int p_setParticleTextureIndex_1_);

    @Shadow protected int particleTextureIndexX;

    @Shadow protected int particleTextureIndexY;

    @Inject(method = "renderParticle", at = @At("HEAD"))
    public void renderParticle(WorldRenderer p_renderParticle_1_, Entity p_renderParticle_2_, float p_renderParticle_3_, float p_renderParticle_4_, float p_renderParticle_5_, float p_renderParticle_6_, float p_renderParticle_7_, float p_renderParticle_8_, CallbackInfo ci) {
        if (this.particleTextureIndexX == 1 && this.particleTextureIndexY == 4) {
            // TODO let the player choose particles instead of being random
            // maybe a dropdown on a module settings screen
            MCUtil.ParticleTypes[] particleTypes = MCUtil.ParticleTypes.values();
            MCUtil.ParticleTypes randomParticleType = particleTypes[RandomUtil.randomRange(0, particleTypes.length - 1)];
            this.particleTextureIndexX = randomParticleType.x;
            this.particleTextureIndexY = randomParticleType.y;
        }
    }
}
