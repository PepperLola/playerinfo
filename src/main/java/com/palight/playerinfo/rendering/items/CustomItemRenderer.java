package com.palight.playerinfo.rendering.items;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.mixin.render.IMixinItemRenderer;
import com.palight.playerinfo.modules.impl.misc.OldAnimationsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class CustomItemRenderer {
    private ItemRenderer parent;
    private Minecraft mc;

    private OldAnimationsMod oldAnimationsMod = ((OldAnimationsMod) PlayerInfo.getModules().get("oldAnimations"));

    public CustomItemRenderer(ItemRenderer parent) {
        this.parent = parent;
    }

    public void transformFirstPersonItem(float equipProgress, float swingProgress) {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.bowAnimationEnabled && mc != null && mc.thePlayer != null &&
                mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() != null &&
                Item.getIdFromItem(mc.thePlayer.getItemInUse().getItem()) == 261) {
            GlStateManager.translate(0.0f, 0.05f, 0.04f);
        }

        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.rodAnimationEnabled && mc != null && mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() !=
                null && Item.getIdFromItem(mc.thePlayer.getCurrentEquippedItem().getItem()) == 346) {
            GlStateManager.translate(0.08f, -0.027f, -0.33f);
            GlStateManager.scale(0.93f, 1.0f, 1.0f);
        }

        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.blockHitAnimationEnabled && mc != null && mc.thePlayer != null && mc.thePlayer.isSwingInProgress && mc.thePlayer.getCurrentEquippedItem() !=
                null && !mc.thePlayer.isEating() && !mc.thePlayer.isBlocking()) {
            GlStateManager.scale(0.85f, 0.85f, 0.85f);
            GlStateManager.translate(-0.078f, 0.003f, 0.05f);
        }

        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    public void renderItemInFirstPerson(float partialTicks, float prevEquippedProgress, float equippedProgress, ItemStack itemToRender) {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        float f = 1.0F - (prevEquippedProgress + (equippedProgress - prevEquippedProgress) * partialTicks);
        EntityPlayerSP entityPlayerSP = mc.thePlayer;
        float f1 = entityPlayerSP.getSwingProgress(partialTicks);
        float f2 = entityPlayerSP.prevRotationPitch + (entityPlayerSP.rotationPitch - entityPlayerSP.prevRotationPitch) * partialTicks;
        float f3 = entityPlayerSP.prevRotationYaw + (entityPlayerSP.rotationYaw - entityPlayerSP.prevRotationYaw) * partialTicks;
        ((IMixinItemRenderer) parent).callRotateArroundXAndY(f2, f3);
        ((IMixinItemRenderer) parent).callSetLightMapFromPlayer(entityPlayerSP);
        ((IMixinItemRenderer) parent).callRotateWithPlayerRotations(entityPlayerSP, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (itemToRender != null) {
            if (itemToRender.getItem() == Items.filled_map) {
                ((IMixinItemRenderer) parent).callRenderItemMap(entityPlayerSP, f2, f, f1);
            } else if (entityPlayerSP.getItemInUseCount() > 0) {
                EnumAction enumaction = itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        ((IMixinItemRenderer) parent).callPerformDrinking(entityPlayerSP, partialTicks);
                        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.eatingAnimationEnabled) {
                            transformFirstPersonItem(f, f1);
                        } else {
                            transformFirstPersonItem(f, 0.0F);
                        }
                        break;
                    case BLOCK:
                        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.blockHitAnimationEnabled) {
                            transformFirstPersonItem(f, f1);
//                            GlStateManager.scale(0.83f, 0.88f, 0.85f);
//                            GlStateManager.translate(-0.3f, 0.1f, 0.0f);
                        } else {
                            transformFirstPersonItem(f, 0f);
                        }
                        ((IMixinItemRenderer) parent).callDoBlockTransformations();
                        break;

                    case BOW:
                        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.bowAnimationEnabled) {
                            transformFirstPersonItem(f, f1);
                        } else {
                            transformFirstPersonItem(f, 0.0F);
                        }
                        ((IMixinItemRenderer) parent).callDoBowTransformations(partialTicks, entityPlayerSP);
                }
            } else {
                ((IMixinItemRenderer) parent).callDoItemUsedTransformations(f1);
                transformFirstPersonItem(f, f1);
            }

            parent.renderItem(entityPlayerSP, itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!entityPlayerSP.isInvisible()) {
            ((IMixinItemRenderer) parent).callRenderPlayerArm(entityPlayerSP, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    public void performDrinking(AbstractClientPlayer p_performDrinking_1_, float p_performDrinking_2_, ItemStack itemToRender) {
        if (oldAnimationsMod.isEnabled() && oldAnimationsMod.eatingAnimationEnabled) {
            float f = (float) p_performDrinking_1_.getItemInUseCount() - p_performDrinking_2_ + 1.0F;
            float f1 = 1.0f - f / (float) itemToRender.getMaxItemUseDuration();
            float f2 = 1.0f - f1;
            f2 = f2 * f2 * f2;
            f2 = f2 * f2 * f2;
            f2 = f2 * f2 * f2;
            float f3 = 1.0F - f2;

            GlStateManager.translate(0.0F, MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F) * (float)((double)f1 > 0.2D ? 1 : 0), 0.0F);
            GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
            GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
        } else {
            float f = (float) p_performDrinking_1_.getItemInUseCount() - p_performDrinking_2_ + 1.0F;
            float f1 = f / (float) itemToRender.getMaxItemUseDuration();
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * 3.1415927F) * 0.1F);
            if (f1 >= 0.8F) {
                f2 = 0.0F;
            }

            GlStateManager.translate(0.0F, f2, 0.0F);
            float f3 = 1.0F - (float) Math.pow(f1, 27.0D);
            GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
            GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
