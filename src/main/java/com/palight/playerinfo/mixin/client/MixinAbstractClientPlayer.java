package com.palight.playerinfo.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
//    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
//    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> ci) {
//        if (CapeHandler.PLAYER_DATA.containsKey(this.getUniqueID()) && CapeHandler.PLAYER_DATA.get(this.getUniqueID()).getCape() != Cape.NONE) {
//            ci.setReturnValue(CapeHandler.PLAYER_DATA.get(this.getUniqueID()).getCape().getImage());
//            ci.cancel();
//        }
//    }

    @Override
    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
}
