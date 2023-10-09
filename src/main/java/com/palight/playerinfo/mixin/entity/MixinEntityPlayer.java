package com.palight.playerinfo.mixin.entity;

import com.palight.playerinfo.rendering.cosmetics.CapeHolder;
import com.palight.playerinfo.rendering.cosmetics.VerletSimulation;
import com.palight.playerinfo.training.AimTrainingController;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements CapeHolder {
    private VerletSimulation simulation = new VerletSimulation();

    @Override
    public VerletSimulation getSimulation() {
        return this.simulation;
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void moveCapeUpdate(CallbackInfo ci) {
        simulate((AbstractClientPlayer) (Object) this);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void updateTargets(CallbackInfo ci) {
        AimTrainingController.update();
    }
}
