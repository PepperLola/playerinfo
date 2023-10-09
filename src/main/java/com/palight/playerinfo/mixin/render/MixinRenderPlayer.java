package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.PlayerHiderMod;
import com.palight.playerinfo.training.AimTrainingController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    private PlayerHiderMod playerHiderMod;

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(AbstractClientPlayer player, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_, CallbackInfo ci) {
        if (playerHiderMod == null) {
            playerHiderMod = (PlayerHiderMod) PlayerInfo.getModules().get("playerHider");
        }

        if (playerHiderMod.isEnabled()) {
            String playerName = EnumChatFormatting.getTextWithoutFormattingCodes(player.getDisplayName().getUnformattedText());
            Pattern namePattern = Pattern.compile(playerHiderMod.showNamePattern);

            if (!namePattern.matcher(playerName).find() && !namePattern.matcher(player.getUniqueID().toString()).find()) {
                ci.cancel();
            }
        }
    }
}
