package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.RenderTitleEvent;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.WidgetEditorGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public class MixinGuiIngameForge extends GuiIngame {
    @Shadow @Final private static int WHITE;
    @Shadow public static boolean renderHelmet;
    @Shadow public static boolean renderPortal;
    @Shadow public static boolean renderHotbar;
    @Shadow public static boolean renderCrosshairs;
    @Shadow public static boolean renderBossHealth;
    @Shadow public static boolean renderHealth;
    @Shadow public static boolean renderArmor;
    @Shadow public static boolean renderFood;
    @Shadow public static boolean renderHealthMount;
    @Shadow public static boolean renderAir;
    @Shadow public static boolean renderExperiance;
    @Shadow public static boolean renderJumpBar;
    @Shadow public static boolean renderObjective;
    @Shadow public static int left_height;
    @Shadow public static int right_height;
    @Shadow private ScaledResolution res;
    @Shadow private FontRenderer fontrenderer;
    @Shadow private RenderGameOverlayEvent eventParent;

    private boolean sentTitle = false;

    public MixinGuiIngameForge(Minecraft mc) {
        super(mc);
    }

    @Inject(method = "renderHUDText", at = @At("HEAD"))
    private void renderHUDText(int width, int height, CallbackInfo ci) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) return;
        // render custom gui elements
        if (!Minecraft.getMinecraft().gameSettings.hideGUI &&
                (Minecraft.getMinecraft().currentScreen instanceof GuiChat ||
                        Minecraft.getMinecraft().currentScreen instanceof WidgetEditorGui ||
                        Minecraft.getMinecraft().currentScreen == null)) {
            for (Module module : PlayerInfo.getModules().values()) {
                GuiIngameWidget widget = module.getWidget();
                if (widget == null || !widget.shouldRender(module) || widget instanceof ScoreboardWidget) continue;
                widget.render(Minecraft.getMinecraft());
            }
        }
    }

    /**
     * @author palight
     */
    @Overwrite
    public void renderHealth(int width, int height) {
        ((IMixinGuiIngameForge) this.mc.ingameGUI).callBind(icons);
        if (!((IMixinGuiIngameForge) this.mc.ingameGUI).callPre(RenderGameOverlayEvent.ElementType.HEALTH)) {
            this.mc.mcProfiler.startSection("health");
            GlStateManager.enableBlend();
            EntityPlayer player = (EntityPlayer) this.mc.getRenderViewEntity();
            int health = MathHelper.ceiling_float_int(player.getHealth());
            boolean highlight = this.healthUpdateCounter > (long) this.updateCounter && (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;
            if (health < this.playerHealth && player.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (health > this.playerHealth && player.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = health;
                this.lastPlayerHealth = health;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.playerHealth = health;
            int healthLast = this.lastPlayerHealth;
            IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            float healthMax = (float) attrMaxHealth.getAttributeValue();
            float absorb = player.getAbsorptionAmount();
            int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);
            this.rand.setSeed(this.updateCounter * 312871L);
            int left = width / 2 - 91;
            int top = height - left_height;
            left_height += healthRows * rowHeight;
            if (rowHeight != 10) {
                left_height += 10 - rowHeight;
            }

            int regen = -1;
            if (player.isPotionActive(Potion.regeneration)) {
                regen = this.updateCounter % 25;
            }

            int TOP = 9 * (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled() || ModConfiguration.hardcoreHeartsEnabled && DisplayTweaksMod.hardcoreHearts ? 5 : 0);
            int BACKGROUND = highlight ? 25 : 16;
            int MARGIN = 16;
            if (player.isPotionActive(Potion.poison)) {
                MARGIN += 36;
            } else if (player.isPotionActive(Potion.wither)) {
                MARGIN += 72;
            }

            float absorbRemaining = absorb;

            for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
                int row = MathHelper.ceiling_float_int((float) (i + 1) / 10.0F) - 1;
                int x = left + i % 10 * 8;
                int y = top - row * rowHeight;
                if (health <= 4) {
                    y += this.rand.nextInt(2);
                }

                if (i == regen) {
                    y -= 2;
                }

                this.drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);
                if (highlight) {
                    if (i * 2 + 1 < healthLast) {
                        this.drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9);
                    } else if (i * 2 + 1 == healthLast) {
                        this.drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9);
                    }
                }

                if (!(absorbRemaining > 0.0F)) {
                    if (i * 2 + 1 < health) {
                        this.drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9);
                    } else if (i * 2 + 1 == health) {
                        this.drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9);
                    }
                } else {
                    if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
                        this.drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9);
                    } else {
                        this.drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9);
                    }

                    absorbRemaining -= 2.0F;
                }
            }

            GlStateManager.disableBlend();
            this.mc.mcProfiler.endSection();
            ((IMixinGuiIngameForge) this.mc.ingameGUI).callPost(RenderGameOverlayEvent.ElementType.HEALTH);
        }
    }

    /**
     * @author palight
     */
    @Overwrite
    protected void renderTitle(int width, int height, float partialTicks) {
        if (this.titlesTimer > 0) {
            System.out.println("RENDERING TITLE " + ((IMixinGuiIngame) this).getDisplayedTitle());
            if (!sentTitle) {
                String title = ((IMixinGuiIngame) this).getDisplayedTitle();
                String subtitle = ((IMixinGuiIngame) this).getDisplayedSubTitle();
                if (!title.equals("") && subtitle.equals("")) {
                    System.out.println("TITLE IN MIXIN: " + title + " | " + EnumChatFormatting.getTextWithoutFormattingCodes(title));
                    MinecraftForge.EVENT_BUS.post(new RenderTitleEvent(title, subtitle));
                    sentTitle = true;
                }
            }
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float age = (float)this.titlesTimer - partialTicks;
            int opacity = 255;
            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                float f3 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - age;
                opacity = (int)(f3 * 255.0F / (float)this.titleFadeIn);
            }

            if (this.titlesTimer <= this.titleFadeOut) {
                opacity = (int)(age * 255.0F / (float)this.titleFadeOut);
            }

            opacity = MathHelper.clamp_int(opacity, 0, 255);
            if (opacity > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(width / 2), (float)(height / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int l = opacity << 24 & -16777216;
                this.getFontRenderer().drawString(this.displayedTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.getFontRenderer().drawString(this.displayedSubTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        } else {
            sentTitle = false;
        }
    }
}
