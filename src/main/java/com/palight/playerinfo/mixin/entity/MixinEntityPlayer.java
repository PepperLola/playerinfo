package com.palight.playerinfo.mixin.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {
    @Shadow
    private String displayname;

    @Inject(method = "damageEntity", at = @At("HEAD"))
    public void damageEntity(DamageSource damageSource, float damage, CallbackInfo ci) {
//        System.out.println("damageEntity " + damageSource.getEntity().getName() + " | " + displayname);
//        if (displayname.equals(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {
//            MinecraftForge.EVENT_BUS.post(new PlayerDamageEvent(damageSource, damage));
//        }
    }
}
