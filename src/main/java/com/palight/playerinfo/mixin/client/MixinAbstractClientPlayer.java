package com.palight.playerinfo.mixin.client;

import com.mojang.authlib.GameProfile;
import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer extends EntityPlayer {

    @Shadow NetworkPlayerInfo playerInfo;

    public MixinAbstractClientPlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
        super(p_i45324_1_, p_i45324_2_);
    }

    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
        }

        return this.playerInfo;
    }

    /**
     * @author palight
     * @reason Added custom cape textures.
     */
    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> ci) {
        if (this.getUniqueID().toString().equals("d512bc73-9d3f-43f9-8992-1b9506adc867")) {
            ci.setReturnValue(new ResourceLocation(PlayerInfo.MODID, "textures/capes/glitch_cape.png"));
            ci.cancel();
        } else if (this.getUniqueID().toString().equals("eae049b6-86e4-4213-ab85-084ddb7bf690")) {
            ci.setReturnValue(new ResourceLocation(PlayerInfo.MODID, "textures/capes/walter_cape.png"));
            ci.cancel();
        }
    }

    @Override
    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
}
