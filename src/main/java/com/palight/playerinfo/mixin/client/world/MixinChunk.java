package com.palight.playerinfo.mixin.client.world;

import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)

public class MixinChunk {
    @Inject(method = "getLightFor", at = @At("HEAD"), cancellable = true)
    public void getLightFor(EnumSkyBlock p_getLightFor_1_, BlockPos p_getLightFor_2_, CallbackInfoReturnable <Integer> ci){
        if(ModConfiguration.fullBrightModEnabled){
            ci.setReturnValue(15);
        }
    }
    @Inject(method = "getLightSubtracted", at = @At("HEAD"), cancellable = true)
    public void getLightFor(BlockPos p_getLightSubtracted_1_, int p_getLightSubtracted_2_, CallbackInfoReturnable <Integer> ci){
        if(ModConfiguration.fullBrightModEnabled){
            ci.setReturnValue(15);
        }
    }
}

