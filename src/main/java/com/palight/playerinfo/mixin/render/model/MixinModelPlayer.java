package com.palight.playerinfo.mixin.render.model;

import com.palight.playerinfo.animations.EmoteHandler;
import com.palight.playerinfo.animations.emotes.Emote;
import com.palight.playerinfo.animations.states.impl.PlayerAnimationState;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer {
    @Shadow private ModelRenderer bipedDeadmau5Head;

    @Shadow public ModelRenderer bipedLeftArmwear;

    @Shadow public ModelRenderer bipedRightArmwear;

    @Shadow public ModelRenderer bipedLeftLegwear;

    @Shadow public ModelRenderer bipedRightLegwear;

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void setRotationAngles(float a1, float a2, float a3, float a4, float a5, float a6, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer) || !EmoteHandler.playingEmotes.containsKey(entity.getUniqueID())) return;
        long currentTime = System.currentTimeMillis();
        Emote emote = EmoteHandler.playingEmotes.get(entity.getUniqueID());
        long started = EmoteHandler.startPlayingTime.get(entity.getUniqueID());
        long duration = emote.getDurationMillis();

        if (currentTime < started + duration) {
            double t = (currentTime - started) / (double) duration;
            PlayerAnimationState state = emote.getAnimationState(t);

            if (state.getHeadTranslation() != null) {
                this.bipedDeadmau5Head.offsetX = (float) state.getHeadTranslation().x;
                this.bipedDeadmau5Head.offsetY = (float) state.getHeadTranslation().y;
                this.bipedDeadmau5Head.offsetZ = (float) state.getHeadTranslation().z;
            }

            if (state.getLeftArmTranslation() != null) {
                this.bipedLeftArmwear.offsetX = (float) state.getLeftArmTranslation().x;
                this.bipedLeftArmwear.offsetY = (float) state.getLeftArmTranslation().y;
                this.bipedLeftArmwear.offsetZ = (float) state.getLeftArmTranslation().z;
            }

            if (state.getRightArmTranslation() != null) {
                this.bipedRightArmwear.offsetX = (float) state.getRightArmTranslation().x;
                this.bipedRightArmwear.offsetY = (float) state.getRightArmTranslation().y;
                this.bipedRightArmwear.offsetZ = (float) state.getRightArmTranslation().z;
            }

            if (state.getLeftLegTranslation() != null) {
                this.bipedLeftLegwear.offsetX = (float) state.getLeftLegTranslation().x;
                this.bipedLeftLegwear.offsetY = (float) state.getLeftLegTranslation().y;
                this.bipedLeftLegwear.offsetZ = (float) state.getLeftLegTranslation().z;
            }

            if (state.getRightLegTranslation() != null) {
                this.bipedRightLegwear.offsetX = (float) state.getRightLegTranslation().x;
                this.bipedRightLegwear.offsetY = (float) state.getRightLegTranslation().y;
                this.bipedRightLegwear.offsetZ = (float) state.getRightLegTranslation().z;
            }
        }
    }
}
