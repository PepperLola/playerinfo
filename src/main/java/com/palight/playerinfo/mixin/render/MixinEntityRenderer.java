package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.misc.PerspectiveMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow private Minecraft mc;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private float thirdPersonDistance;
    @Shadow private boolean cloudFog;
    @Shadow private long prevFrameTime;
    @Shadow private float smoothCamYaw;
    @Shadow private float smoothCamPitch;
    @Shadow private float smoothCamPartialTicks;
    @Shadow  private float smoothCamFilterX;
    @Shadow private float smoothCamFilterY;
    @Shadow public static boolean anaglyphEnable;
    @Shadow private ShaderGroup theShaderGroup;
    @Shadow private boolean useShader;
    @Shadow private long renderEndNanoTime;

    @Inject(method = "setupOverlayRendering", at = @At("RETURN"))
    private void setupOverlayRendering(CallbackInfo ci) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) return;
        // render custom gui elements
        for (Module module : PlayerInfo.getModules().values()) {
            GuiIngameWidget widget = module.getWidget();
            if (widget == null || !widget.shouldRender(module) || widget instanceof ScoreboardWidget) continue;
            widget.render(Minecraft.getMinecraft());
        }
    }

    /**
     * @author palight
     * @reason Changing camera rotation when in perspective mode.
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        orientPerspectiveCamera(partialTicks);
    }

    /**
     * @author palight
     * @reason Changing camera rotation when in perspective mode.
     */
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
       updatePerspectiveCamera();
    }

    public void orientPerspectiveCamera(float partialTicks) {
        PerspectiveMod perspectiveMod = (PerspectiveMod) PlayerInfo.getModules().get("perspective");
        Entity entity = mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;

        double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
            ++f;
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!mc.gameSettings.debugCamEnable) {
                final BlockPos blockpos = new BlockPos(entity);
                final IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (block == Blocks.bed) {
                    int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate((float) (j * 90), 0.0F, 1.0F, 0.0F);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        } else if (mc.gameSettings.thirdPersonView > 0) {
            double d4 = thirdPersonDistanceTemp + (thirdPersonDistance - thirdPersonDistanceTemp) * partialTicks;
            if (mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
            } else {
                float f2 = entity.rotationYaw;
                float f3 = entity.rotationPitch;
                if (perspectiveMod.isEnabled()) {
                    f2 = perspectiveMod.getCameraYaw();
                    f3 = perspectiveMod.getCameraPitch();
                }
                if (mc.gameSettings.thirdPersonView == 2) {
                    f3 += 180.0f;
                }
                final double d5 = -MathHelper.sin(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d6 = MathHelper.cos(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d7 = -MathHelper.sin(f3 / 180.0f * 3.1415927f) * d4;
                for (int i = 0; i < 8; ++i) {
                    float f4 = (i & 0x1) * 2 - 1;
                    float f5 = (i >> 1 & 0x1) * 2 - 1;
                    float f6 = (i >> 2 & 0x1) * 2 - 1;
                    f4 *= 0.1f;
                    f5 *= 0.1f;
                    f6 *= 0.1f;
                    final MovingObjectPosition movingobjectposition = mc.theWorld.rayTraceBlocks(new Vec3(d0 + f4, d2 + f5, d3 + f6), new Vec3(d0 - d5 + f4 + f6, d2 - d7 + f5, d3 - d6 + f6));
                    if (movingobjectposition != null) {
                        final double d8 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d2, d3));
                        if (d8 < d4) {
                            d4 = d8;
                        }
                    }
                }
                if (mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }

                if (perspectiveMod.isEnabled()) {
                    GlStateManager.rotate(perspectiveMod.getCameraPitch() - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(perspectiveMod.getCameraYaw() - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
                    GlStateManager.rotate(f2 - perspectiveMod.getCameraYaw(), 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f3 - perspectiveMod.getCameraPitch(), 1.0f, 0.0f, 0.0f);
                } else {
                    GlStateManager.rotate(entity.rotationPitch - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.rotationYaw - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float) (-d4));
                    GlStateManager.rotate(f2 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f3 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                }
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }

        if (!mc.gameSettings.debugCamEnable) {
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
            final float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            final float roll = 0.0f;
            if (entity instanceof EntityAnimal) {
                final EntityAnimal entityanimal = (EntityAnimal) entity;
                yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f;
            }

            if (perspectiveMod.isEnabled()) {
                GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(perspectiveMod.getCameraPitch(), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(perspectiveMod.getCameraYaw() + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
            }
        }

        GlStateManager.translate(0.0f, -f, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
        d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        this.cloudFog = mc.renderGlobal.hasCloudFog(d0, d2, d3, partialTicks);
    }

    /**
     * Update the camera angle in perspective mode
     * @author palight
     */
    public void updatePerspectiveCamera() {
        PerspectiveMod perspectiveMod = (PerspectiveMod) PlayerInfo.getModules().get("perspective");
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.inGameHasFocus && Display.isActive()) {
            if (!perspectiveMod.isPerspectiveToggled()) {
                return;
            }

            // CODE
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float) mc.mouseHelper.deltaX * f2;
            float f4 = (float) mc.mouseHelper.deltaY * f2;

            if (mc.gameSettings.invertMouse) {
                f4 = -f4;
            }

            perspectiveMod.setCameraYaw(perspectiveMod.getCameraYaw() + f3 * 0.15F);
            perspectiveMod.setCameraPitch(perspectiveMod.getCameraPitch() + f4 * 0.15F);

            if (perspectiveMod.getCameraPitch() > 90) perspectiveMod.setCameraPitch(90);
            if (perspectiveMod.getCameraPitch() < -90) perspectiveMod.setCameraPitch(-90);
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }
}
