package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.ItemPhysicsMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {
    @Shadow @Final private RenderItem itemRenderer;

    private RenderManager renderManager;

    private double rotation;

    private static Random random = new Random();

    private ItemPhysicsMod itemPhysicsMod;

    @Shadow
    protected ResourceLocation getEntityTexture(EntityItem p_getEntityTexture_1_) {
        return TextureMap.locationBlocksTexture;
    }

    @Shadow
    public boolean shouldBob() {
        return true;
    }

    protected int getModelCount(ItemStack stack) {
        int i = 1;
        if (stack.stackSize > 48) {
            i = 5;
        } else if (stack.stackSize > 32) {
            i = 4;
        } else if (stack.stackSize > 16) {
            i = 3;
        } else if (stack.stackSize > 1) {
            i = 2;
        }

        return i;
    }


        @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityItem item, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        if (this.itemPhysicsMod == null) {
            this.itemPhysicsMod = (ItemPhysicsMod) PlayerInfo.getModules().get("itemPhysics");
        }
        if  (!this.itemPhysicsMod.isEnabled()) return;
        this.render(item, x, y, z, yaw, pitch);
        ci.cancel();
    }

    private void render(EntityItem item, double x, double y, double z, float yaw, float pitch) {
        rotation = (double)(System.nanoTime() - this.itemPhysicsMod.getTick()) / 2500000 * this.itemPhysicsMod.rotateSpeed * 4;
        if (this.renderManager == null) {
            this.renderManager = ((IMixinRender<EntityItem>) this).getRenderManager();
        }
        ItemStack itemstack = item.getEntityItem();
        int i = 187;
        if (itemstack != null && itemstack.getItem() != null) {
            i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        }
        random.setSeed(i);
        boolean flag = false;
        if (((IMixinRender<EntityItem>) this).callBindEntityTexture(item)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(item)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
        int numItems = this.getModelCount(itemstack);
        boolean is3d = ibakedmodel.isGui3d();
        GlStateManager.translate((float)x, (float)y, (float) z);

        item.rotationYaw = (float) NumberUtil.radiansToDegrees(Math.atan2(item.motionZ, item.motionX)) - 90;

        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(item.rotationYaw, 0.0F, 0.0F, 1.0F);

        if (is3d) {
            GlStateManager.translate(0, 0, -0.08);
        } else {
            GlStateManager.translate(0, 0, -0.04);
        }

        if (is3d || this.renderManager.options != null) {
            // HANDLE PHYSICS
            if (!item.onGround) {
                item.rotationPitch += this.rotation;
            } else if (is3d && !Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(item.posZ) && item.worldObj != null) {
                item.rotationPitch = 0;
            }
            GlStateManager.rotate(item.rotationPitch, 1, 0, 0);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for(int itemIndex = 0; itemIndex < numItems; ++itemIndex) {
            GlStateManager.pushMatrix();
            if (is3d) {
                if (itemIndex > 0) {
                    float dx = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float dy = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float dz = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    boolean shouldSpreadItems = ((IMixinRenderEntityItem) this).callShouldSpreadItems();
                    GlStateManager.translate(shouldSpreadItems ? dx : 0.0F, shouldSpreadItems ? dy : 0.0F, dz);
                }

                GlStateManager.scale(0.5F, 0.5F, 0.5F);

                ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            } else {
                ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.05375F);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        ((IMixinRender<EntityItem>) this).callBindEntityTexture(item);
        if (flag) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(item)).restoreLastBlurMipmap();
        }
    }
}
