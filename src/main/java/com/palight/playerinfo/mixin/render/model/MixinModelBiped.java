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
            this.bipedLeftArm.rotateAngleX = (float) state.getLeftArmRotation().x;
            this.bipedLeftArm.rotateAngleY = (float) state.getLeftArmRotation().y;
            this.bipedLeftArm.rotateAngleZ = (float) state.getLeftArmRotation().z;
            this.bipedRightArm.rotateAngleX = (float) state.getRightArmRotation().x;
            this.bipedRightArm.rotateAngleY = (float) state.getRightArmRotation().y;
            this.bipedRightArm.rotateAngleZ = (float) state.getRightArmRotation().z;
        }
    }
}
