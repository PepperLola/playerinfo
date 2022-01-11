package com.palight.playerinfo.mixin.render.model;

import com.palight.playerinfo.animations.EmoteHandler;
import com.palight.playerinfo.animations.emotes.Emote;
import com.palight.playerinfo.animations.states.impl.PlayerAnimationState;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {
    @Shadow public ModelRenderer bipedLeftArm;

    @Shadow public ModelRenderer bipedRightArm;

    @Shadow public ModelRenderer bipedHead;

    @Shadow public ModelRenderer bipedLeftLeg;

    @Shadow public ModelRenderer bipedRightLeg;

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void setRotationAngles(float a1, float a2, float a3, float a4, float a5, float a6, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer) || !EmoteHandler.playingEmotes.containsKey(entity.getUniqueID())) return;
        long currentTime = System.currentTimeMillis();
        Emote emote = EmoteHandler.playingEmotes.get(entity.getUniqueID());
        long started = EmoteHandler.startPlayingTime.get(entity.getUniqueID());
        long duration = emote.getDurationMillis();

        if (currentTime > started + duration) {
            EmoteHandler.stopPlayingEmote(entity.getUniqueID());
        } else {
            double t = (currentTime - started) / (double) duration;
            PlayerAnimationState state = emote.getAnimationState(t);

            if (state.getHeadRotation() != null) {
                this.bipedHead.rotateAngleX = (float) state.getHeadRotation().x;
                this.bipedHead.rotateAngleY = (float) state.getHeadRotation().y;
                this.bipedHead.rotateAngleZ = (float) state.getHeadRotation().z;
            }

            if (state.getHeadTranslation() != null) {
                this.bipedHead.offsetX = (float) state.getHeadTranslation().x;
                this.bipedHead.offsetY = (float) state.getHeadTranslation().y;
                this.bipedHead.offsetZ = (float) state.getHeadTranslation().z;
            }

            if (state.getLeftArmRotation() != null) {
                this.bipedLeftArm.rotateAngleX = (float) state.getLeftArmRotation().x;
                this.bipedLeftArm.rotateAngleY = (float) state.getLeftArmRotation().y;
                this.bipedLeftArm.rotateAngleZ = (float) state.getLeftArmRotation().z;
            }

            if (state.getLeftArmTranslation() != null) {
                this.bipedLeftArm.offsetX = (float) state.getLeftArmTranslation().x;
                this.bipedLeftArm.offsetY = (float) state.getLeftArmTranslation().y;
                this.bipedLeftArm.offsetZ = (float) state.getLeftArmTranslation().z;
            }

            if (state.getRightArmRotation() != null) {
                this.bipedRightArm.rotateAngleX = (float) state.getRightArmRotation().x;
                this.bipedRightArm.rotateAngleY = (float) state.getRightArmRotation().y;
                this.bipedRightArm.rotateAngleZ = (float) state.getRightArmRotation().z;
            }

            if (state.getRightArmTranslation() != null) {
                this.bipedRightArm.offsetX = (float) state.getRightArmTranslation().x;
                this.bipedRightArm.offsetY = (float) state.getRightArmTranslation().y;
                this.bipedRightArm.offsetZ = (float) state.getRightArmTranslation().z;
            }

            if (state.getLeftLegRotation() != null) {
                this.bipedLeftLeg.rotateAngleX = (float) state.getLeftLegRotation().x;
                this.bipedLeftLeg.rotateAngleY = (float) state.getLeftLegRotation().y;
                this.bipedLeftLeg.rotateAngleZ = (float) state.getLeftLegRotation().z;
            }

            if (state.getLeftLegTranslation() != null) {
                this.bipedLeftLeg.offsetX = (float) state.getLeftLegTranslation().x;
                this.bipedLeftLeg.offsetY = (float) state.getLeftLegTranslation().y;
                this.bipedLeftLeg.offsetZ = (float) state.getLeftLegTranslation().z;
            }

            if (state.getRightLegRotation() != null) {
                this.bipedRightLeg.rotateAngleX = (float) state.getRightLegRotation().x;
                this.bipedRightLeg.rotateAngleY = (float) state.getRightLegRotation().y;
                this.bipedRightLeg.rotateAngleZ = (float) state.getRightLegRotation().z;
            }

            if (state.getRightLegTranslation() != null) {
                this.bipedRightLeg.offsetX = (float) state.getRightLegTranslation().x;
                this.bipedRightLeg.offsetY = (float) state.getRightLegTranslation().y;
                this.bipedRightLeg.offsetZ = (float) state.getRightLegTranslation().z;
            }
        }
    }
}
