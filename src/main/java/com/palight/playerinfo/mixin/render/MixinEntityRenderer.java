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
        System.out.println("RENDERING WIDGETS...");
        // render custom gui elements
        for (Module module : PlayerInfo.getModules().values()) {
            GuiIngameWidget widget = module.getWidget();
            System.out.println("RENDERING WIDGET " + module.getId());
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

//    /**
//     * @author palight
//     * @reason Changing camera rotation when in perspective mode.
//     */
//    @Overwrite
//    public void updateCameraAndRender(float p_updateCameraAndRender_1_, long p_updateCameraAndRender_2_) {
//        PerspectiveMod perspectiveMod = (PerspectiveMod) PlayerInfo.getModules().get("perspective");
//        System.out.println("PERSPECTIVE MOD: " + perspectiveMod);
//        boolean flag = Display.isActive();
//        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
//            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
//                this.mc.displayInGameMenu();
//            }
//        } else {
//            this.prevFrameTime = Minecraft.getSystemTime();
//            System.out.println("PREV FRAME TIME: " + prevFrameTime);
//        }
//
//        this.mc.mcProfiler.startSection("mouse");
//        if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
//            Mouse.setGrabbed(false);
//            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
//            Mouse.setGrabbed(true);
//        }
//
//        if (this.mc.inGameHasFocus && flag && perspectiveMod.overrideMouse()) {
//            System.out.println("BEFORE MOUSE XY CHANGE");
//            this.mc.mouseHelper.mouseXYChange();
//            System.out.println("AFTER MOUSE XY CHANGE");
//            float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
//            System.out.println("F " + f);
//            float f1 = f * f * f * 8.0F;
//            System.out.println("F1 " + f1);
//            float f2 = (float)this.mc.mouseHelper.deltaX * f1;
//            System.out.println("F2 " + f2);
//            float f3 = (float)this.mc.mouseHelper.deltaY * f1;
//            System.out.println("F3 " + f3);
//            int i = 1;
//            if (this.mc.gameSettings.invertMouse) {
//                i = -1;
//            }
//            System.out.println("I " + i);
//
//            if (this.mc.gameSettings.smoothCamera) {
//                System.out.println("SMOOTH CAMERA");
//                this.smoothCamYaw += f2;
//                this.smoothCamPitch += f3;
//                System.out.println("ANGLES " + smoothCamYaw + " | " + smoothCamPitch);
//                float f4 = p_updateCameraAndRender_1_ - this.smoothCamPartialTicks;
//                System.out.println("F4 " + f4);
//                this.smoothCamPartialTicks = p_updateCameraAndRender_1_;
//                System.out.println("PARTIAL TICKS " + smoothCamPartialTicks);
//                f2 = this.smoothCamFilterX * f4;
//                f3 = this.smoothCamFilterY * f4;
//                System.out.println("F2 " + f2 + " F3 " + f3 + "...\nSETTING ANGLES...");
//                this.mc.thePlayer.setAngles(f2, f3 * (float)i);
//                System.out.println("SET ANGLES");
//            } else {
//                this.smoothCamYaw = 0.0F;
//                this.smoothCamPitch = 0.0F;
//                System.out.println("YAW " + smoothCamYaw + " | PITCH " + smoothCamPitch + "\nSETTING ANGLES...");
//                this.mc.thePlayer.setAngles(f2, f3 * (float)i);
//                System.out.println("SET ANGLES");
//            }
//        }
//
//        this.mc.mcProfiler.endSection();
//        if (!this.mc.skipRenderWorld) {
//            anaglyphEnable = this.mc.gameSettings.anaglyph;
//            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
//            int i1 = scaledresolution.getScaledWidth();
//            int j1 = scaledresolution.getScaledHeight();
//            final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
//            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
//            int i2 = this.mc.gameSettings.limitFramerate;
//            if (this.mc.theWorld != null) {
//                this.mc.mcProfiler.startSection("level");
//                int j = Math.min(Minecraft.getDebugFPS(), i2);
//                j = Math.max(j, 60);
//                long k = System.nanoTime() - p_updateCameraAndRender_2_;
//                long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
//                System.out.println("ENTITY RENDERER " + mc.entityRenderer);
//                mc.entityRenderer.renderWorld(p_updateCameraAndRender_1_, System.nanoTime() + l);
//                System.out.println("RENDERED WORLD!");
//                if (OpenGlHelper.shadersSupported) {
//                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
//                    System.out.println("RENDERED ENTITY OUTLINE FRAME BUFFER");
//                    if (this.theShaderGroup != null && this.useShader) {
//                        System.out.println("USING SHADER");
//                        GlStateManager.matrixMode(5890);
//                        GlStateManager.pushMatrix();
//                        GlStateManager.loadIdentity();
//                        System.out.println("LOADED IDENTITY");
//                        this.theShaderGroup.loadShaderGroup(p_updateCameraAndRender_1_);
//                        System.out.println("LOADED SHADER GROUP");
//                        GlStateManager.popMatrix();
//                    }
//
//                    this.mc.getFramebuffer().bindFramebuffer(true);
//                    System.out.println("BOUND FRAMEBUFFER");
//                }
//
//                this.renderEndNanoTime = System.nanoTime();
//                this.mc.mcProfiler.endStartSection("gui");
//                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
//                    System.out.println("WILL RENDER OVERLAY");
//                    GlStateManager.alphaFunc(516, 0.1F);
//                    System.out.println("ALPHA FUNC");
//                    this.mc.ingameGUI.renderGameOverlay(p_updateCameraAndRender_1_);
//                    System.out.println("RENDERED GAME OVERLAY");
//                }
//
//                this.mc.mcProfiler.endSection();
//            } else {
//                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
//                GlStateManager.matrixMode(5889);
//                GlStateManager.loadIdentity();
//                GlStateManager.matrixMode(5888);
//                GlStateManager.loadIdentity();
//                mc.entityRenderer.setupOverlayRendering();
//                this.renderEndNanoTime = System.nanoTime();
//            }
//
//
//            if (this.mc.currentScreen != null) {
//                System.out.println("BEFORE CLEAR");
//                GlStateManager.clear(256);
//                System.out.println("AFTER CLEAR");
//
//                try {
//                    ForgeHooksClient.drawScreen(this.mc.currentScreen, k1, l1, p_updateCameraAndRender_1_);
//                    System.out.println("DREW SCREEN");
//                } catch (Throwable var16) {
//                    CrashReport crashreport = CrashReport.makeCrashReport(var16, "Rendering screen");
//                    throw new ReportedException(crashreport);
//                }
//            }
//        }
//    }
}
