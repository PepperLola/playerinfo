package com.palight.playerinfo.gui.ingame;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.ScoreboardWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.gui.CoordsMod;
import com.palight.playerinfo.modules.gui.ScoreboardMod;
import com.palight.playerinfo.modules.movement.ToggleSprintMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GuiIngameCustom extends GuiIngame {
    private static final int WHITE = 16777215;
    public static boolean renderHelmet = true;
    public static boolean renderPortal = true;
    public static boolean renderHotbar = true;
    public static boolean renderCrosshairs = true;
    public static boolean renderBossHealth = true;
    public static boolean renderHealth = true;
    public static boolean renderArmor = true;
    public static boolean renderFood = true;
    public static boolean renderHealthMount = true;
    public static boolean renderAir = true;
    public static boolean renderExperiance = true;
    public static boolean renderJumpBar = true;
    public static boolean renderObjective = true;
    public static int left_height = 39;
    public static int right_height = 39;
    private ScaledResolution res = null;
    private FontRenderer fontrenderer = null;
    private RenderGameOverlayEvent eventParent;
    private GuiOverlayDebugForge debugOverlay;

    public GuiIngameCustom(Minecraft mc) {
        super(mc);
        this.debugOverlay = new GuiOverlayDebugForge(mc);
    }

    public void renderGameOverlay(float partialTicks) {
        this.res = new ScaledResolution(this.mc);
        this.eventParent = new RenderGameOverlayEvent(partialTicks, this.res);
        int width = this.res.getScaledWidth();
        int height = this.res.getScaledHeight();
        renderHealthMount = this.mc.thePlayer.ridingEntity instanceof EntityLivingBase;
        renderFood = this.mc.thePlayer.ridingEntity == null;
        renderJumpBar = this.mc.thePlayer.isRidingHorse();
        right_height = 39;
        left_height = 39;
        if (!this.pre(ElementType.ALL)) {
            this.fontrenderer = this.mc.fontRendererObj;
            this.mc.entityRenderer.setupOverlayRendering();
            GlStateManager.enableBlend();
            if (Minecraft.isFancyGraphicsEnabled()) {
                this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), this.res);
            } else {
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            }

            if (renderHelmet) {
                this.renderHelmet(this.res, partialTicks);
            }

            if (renderPortal && !this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                this.renderPortal(this.res, partialTicks);
            }

            if (renderHotbar) {
                this.renderTooltip(this.res, partialTicks);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.zLevel = -90.0F;
            this.rand.setSeed((long)(this.updateCounter * 312871));
            if (renderCrosshairs) {
                this.renderCrosshairs(width, height);
            }

            if (renderBossHealth) {
                this.renderBossHealth();
            }

            if (this.mc.playerController.shouldDrawHUD() && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (renderHealth) {
                    this.renderHealth(width, height);
                }

                if (renderArmor) {
                    this.renderArmor(width, height);
                }

                if (renderFood) {
                    this.renderFood(width, height);
                }

                if (renderHealthMount) {
                    this.renderHealthMount(width, height);
                }

                if (renderAir) {
                    this.renderAir(width, height);
                }
            }

            this.renderSleepFade(width, height);
            if (renderJumpBar) {
                this.renderJumpBar(width, height);
            } else if (renderExperiance) {
                this.renderExperience(width, height);
            }

            this.renderToolHightlight(this.res);
            this.renderHUDText(width, height);
            this.renderRecordOverlay(width, height, partialTicks);
            this.renderTitle(width, height, partialTicks);
            Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
            ScoreObjective objective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
            if (scoreplayerteam != null) {
                int slot = scoreplayerteam.getChatFormat().getColorIndex();
                if (slot >= 0) {
                    objective = scoreboard.getObjectiveInDisplaySlot(3 + slot);
                }
            }

            ScoreObjective objective1 = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);
            if (renderObjective && objective1 != null) {
                this.renderScoreboard(objective1, this.res);
            }

            // render custom gui elements
            for (Module module : PlayerInfo.getModules().values()) {
                if (!module.isEnabled()) continue;
                GuiIngameWidget widget = module.getWidget();
                if (widget == null || widget instanceof ScoreboardWidget) continue;
                widget.render(this.mc);
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableAlpha();
            this.renderChat(width, height);
            this.renderPlayerList(width, height);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            this.post(ElementType.ALL);
        }
    }

    protected void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        ((ScoreboardWidget) PlayerInfo.getModules().get("scoreboard").getWidget()).render(objective, resolution);
    }

    public ScaledResolution getResolution() {
        return this.res;
    }

    protected void renderCrosshairs(int width, int height) {
        if (!this.pre(ElementType.CROSSHAIRS)) {
            if (this.showCrosshair()) {
                this.bind(Gui.icons);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
                GlStateManager.enableAlpha();
                this.drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.disableBlend();
            }

            this.post(ElementType.CROSSHAIRS);
        }
    }

    protected void renderBossHealth() {
        if (!this.pre(ElementType.BOSSHEALTH)) {
            this.bind(Gui.icons);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.mc.mcProfiler.startSection("bossHealth");
            GlStateManager.enableBlend();
            super.renderBossHealth();
            GlStateManager.disableBlend();
            this.mc.mcProfiler.endSection();
            this.post(ElementType.BOSSHEALTH);
        }
    }

    private void renderHelmet(ScaledResolution res, float partialTicks) {
        if (!this.pre(ElementType.HELMET)) {
            ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
            if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() != null) {
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
                    this.renderPumpkinOverlay(res);
                } else {
                    itemstack.getItem().renderHelmetOverlay(itemstack, this.mc.thePlayer, res, partialTicks);
                }
            }

            this.post(ElementType.HELMET);
        }
    }

    protected void renderArmor(int width, int height) {
        if (!this.pre(ElementType.ARMOR)) {
            this.mc.mcProfiler.startSection("armor");
            GlStateManager.enableBlend();
            int left = width / 2 - 91;
            int top = height - left_height;
            int level = ForgeHooks.getTotalArmorValue(this.mc.thePlayer);

            for(int i = 1; level > 0 && i < 20; i += 2) {
                if (i < level) {
                    this.drawTexturedModalRect(left, top, 34, 9, 9, 9);
                } else if (i == level) {
                    this.drawTexturedModalRect(left, top, 25, 9, 9, 9);
                } else if (i > level) {
                    this.drawTexturedModalRect(left, top, 16, 9, 9, 9);
                }

                left += 8;
            }

            left_height += 10;
            GlStateManager.disableBlend();
            this.mc.mcProfiler.endSection();
            this.post(ElementType.ARMOR);
        }
    }

    protected void renderPortal(ScaledResolution res, float partialTicks) {
        if (!this.pre(ElementType.PORTAL)) {
            float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
            if (f1 > 0.0F) {
                this.renderPortal(f1, res);
            }

            this.post(ElementType.PORTAL);
        }
    }

    protected void renderTooltip(ScaledResolution res, float partialTicks) {
        if (!this.pre(ElementType.HOTBAR)) {
            if (this.mc.playerController.isSpectator()) {
                this.spectatorGui.renderTooltip(res, partialTicks);
            } else {
                if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(widgetsTexPath);
                    EntityPlayer lvt_3_1_ = (EntityPlayer)this.mc.getRenderViewEntity();
                    int lvt_4_1_ = res.getScaledWidth() / 2;
                    float lvt_5_1_ = this.zLevel;
                    this.zLevel = -90.0F;
                    this.drawTexturedModalRect(lvt_4_1_ - 91, res.getScaledHeight() - 22, 0, 0, 182, 22);
                    this.drawTexturedModalRect(lvt_4_1_ - 91 - 1 + lvt_3_1_.inventory.currentItem * 20, res.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
                    this.zLevel = lvt_5_1_;
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    RenderHelper.enableGUIStandardItemLighting();

                    for(int lvt_6_1_ = 0; lvt_6_1_ < 9; ++lvt_6_1_) {
                        int lvt_7_1_ = res.getScaledWidth() / 2 - 90 + lvt_6_1_ * 20 + 2;
                        int lvt_8_1_ = res.getScaledHeight() - 16 - 3;
                        this.renderHotbarItem(lvt_6_1_, lvt_7_1_, lvt_8_1_, partialTicks, lvt_3_1_);
                    }

                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableRescaleNormal();
                    GlStateManager.disableBlend();
                }
            }

            this.post(ElementType.HOTBAR);
        }
    }

    @Override
    protected void renderHotbarItem(int p_renderHotbarItem_1_, int p_renderHotbarItem_2_, int p_renderHotbarItem_3_, float p_renderHotbarItem_4_, EntityPlayer p_renderHotbarItem_5_) {
        ItemStack lvt_6_1_ = p_renderHotbarItem_5_.inventory.mainInventory[p_renderHotbarItem_1_];
        if (lvt_6_1_ != null) {
            float lvt_7_1_ = (float)lvt_6_1_.animationsToGo - p_renderHotbarItem_4_;
            if (lvt_7_1_ > 0.0F) {
                GlStateManager.pushMatrix();
                float lvt_8_1_ = 1.0F + lvt_7_1_ / 5.0F;
                GlStateManager.translate((float)(p_renderHotbarItem_2_ + 8), (float)(p_renderHotbarItem_3_ + 12), 0.0F);
                GlStateManager.scale(1.0F / lvt_8_1_, (lvt_8_1_ + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float)(-(p_renderHotbarItem_2_ + 8)), (float)(-(p_renderHotbarItem_3_ + 12)), 0.0F);
            }

            this.itemRenderer.renderItemAndEffectIntoGUI(lvt_6_1_, p_renderHotbarItem_2_, p_renderHotbarItem_3_);
            if (lvt_7_1_ > 0.0F) {
                GlStateManager.popMatrix();
            }

            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, lvt_6_1_, p_renderHotbarItem_2_, p_renderHotbarItem_3_);
        }
    }

    protected void renderAir(int width, int height) {
        if (!this.pre(ElementType.AIR)) {
            this.mc.mcProfiler.startSection("air");
            EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
            GlStateManager.enableBlend();
            int left = width / 2 + 91;
            int top = height - right_height;
            if (player.isInsideOfMaterial(Material.water)) {
                int air = player.getAir();
                int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
                int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;

                for(int i = 0; i < full + partial; ++i) {
                    this.drawTexturedModalRect(left - i * 8 - 9, top, i < full ? 16 : 25, 18, 9, 9);
                }

                right_height += 10;
            }

            GlStateManager.disableBlend();
            this.mc.mcProfiler.endSection();
            this.post(ElementType.AIR);
        }
    }

    public void renderHealth(int width, int height) {
        this.bind(icons);
        if (!this.pre(ElementType.HEALTH)) {
            this.mc.mcProfiler.startSection("health");
            GlStateManager.enableBlend();
            EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
            int health = MathHelper.ceiling_float_int(player.getHealth());
            boolean highlight = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
            if (health < this.playerHealth && player.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 20);
            } else if (health > this.playerHealth && player.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = health;
                this.lastPlayerHealth = health;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.playerHealth = health;
            int healthLast = this.lastPlayerHealth;
            IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            float healthMax = (float)attrMaxHealth.getAttributeValue();
            float absorb = player.getAbsorptionAmount();
            int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);
            this.rand.setSeed((long)(this.updateCounter * 312871));
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

            int TOP = 9 * (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
            int BACKGROUND = highlight ? 25 : 16;
            int MARGIN = 16;
            if (player.isPotionActive(Potion.poison)) {
                MARGIN += 36;
            } else if (player.isPotionActive(Potion.wither)) {
                MARGIN += 72;
            }

            float absorbRemaining = absorb;

            for(int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
                int row = MathHelper.ceiling_float_int((float)(i + 1) / 10.0F) - 1;
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

                if (absorbRemaining <= 0.0F) {
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
            this.post(ElementType.HEALTH);
        }
    }

    public void renderFood(int width, int height) {
        if (!this.pre(ElementType.FOOD)) {
            this.mc.mcProfiler.startSection("food");
            EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
            GlStateManager.enableBlend();
            int left = width / 2 + 91;
            int top = height - right_height;
            right_height += 10;
            boolean unused = false;
            FoodStats stats = this.mc.thePlayer.getFoodStats();
            int level = stats.getFoodLevel();
            int levelLast = stats.getPrevFoodLevel();

            for(int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte backgound = 0;
                if (this.mc.thePlayer.isPotionActive(Potion.hunger)) {
                    icon += 36;
                    backgound = 13;
                }

                if (unused) {
                    backgound = 1;
                }

                if (player.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (level * 3 + 1) == 0) {
                    y = top + (this.rand.nextInt(3) - 1);
                }

                this.drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);
                if (unused) {
                    if (idx < levelLast) {
                        this.drawTexturedModalRect(x, y, icon + 54, 27, 9, 9);
                    } else if (idx == levelLast) {
                        this.drawTexturedModalRect(x, y, icon + 63, 27, 9, 9);
                    }
                }

                if (idx < level) {
                    this.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
                } else if (idx == level) {
                    this.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
                }
            }

            GlStateManager.disableBlend();
            this.mc.mcProfiler.endSection();
            this.post(ElementType.FOOD);
        }
    }

    protected void renderSleepFade(int width, int height) {
        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int sleepTime = this.mc.thePlayer.getSleepTimer();
            float opacity = (float)sleepTime / 100.0F;
            if (opacity > 1.0F) {
                opacity = 1.0F - (float)(sleepTime - 100) / 10.0F;
            }

            int color = (int)(220.0F * opacity) << 24 | 1052704;
            drawRect(0, 0, width, height, color);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }

    }

    protected void renderExperience(int width, int height) {
        this.bind(icons);
        if (!this.pre(ElementType.EXPERIENCE)) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
                this.mc.mcProfiler.startSection("expBar");
                int cap = this.mc.thePlayer.xpBarCap();
                int left = width / 2 - 91;
                int color;
                if (cap > 0) {
                    short barWidth = 182;
                    color = (int)(this.mc.thePlayer.experience * (float)(barWidth + 1));
                    int top = height - 32 + 3;
                    this.drawTexturedModalRect(left, top, 0, 64, barWidth, 5);
                    if (color > 0) {
                        this.drawTexturedModalRect(left, top, 0, 69, color, 5);
                    }
                }

                this.mc.mcProfiler.endSection();
                if (this.mc.playerController.gameIsSurvivalOrAdventure() && this.mc.thePlayer.experienceLevel > 0) {
                    this.mc.mcProfiler.startSection("expLevel");
                    boolean flag1 = false;
                    color = flag1 ? 16777215 : 8453920;
                    String text = "" + this.mc.thePlayer.experienceLevel;
                    int x = (width - this.fontrenderer.getStringWidth(text)) / 2;
                    int y = height - 31 - 4;
                    this.fontrenderer.drawString(text, x + 1, y, 0);
                    this.fontrenderer.drawString(text, x - 1, y, 0);
                    this.fontrenderer.drawString(text, x, y + 1, 0);
                    this.fontrenderer.drawString(text, x, y - 1, 0);
                    this.fontrenderer.drawString(text, x, y, color);
                    this.mc.mcProfiler.endSection();
                }
            }

            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.post(ElementType.EXPERIENCE);
        }
    }

    protected void renderJumpBar(int width, int height) {
        this.bind(icons);
        if (!this.pre(ElementType.JUMPBAR)) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            this.mc.mcProfiler.startSection("jumpBar");
            float charge = this.mc.thePlayer.getHorseJumpPower();
            int barWidth = 1;
            int x = width / 2 - 91;
            int filled = (int)(charge * 183.0F);
            int top = height - 32 + 3;
            this.drawTexturedModalRect(x, top, 0, 84, 182, 5);
            if (filled > 0) {
                this.drawTexturedModalRect(x, top, 0, 89, filled, 5);
            }

            GlStateManager.enableBlend();
            this.mc.mcProfiler.endSection();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.post(ElementType.JUMPBAR);
        }
    }

    protected void renderToolHightlight(ScaledResolution res) {
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.mc.mcProfiler.startSection("toolHighlight");
            if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
                String name = this.highlightingItemStack.getDisplayName();
                if (this.highlightingItemStack.hasDisplayName()) {
                    name = EnumChatFormatting.ITALIC + name;
                }

                name = this.highlightingItemStack.getItem().getHighlightTip(this.highlightingItemStack, name);
                int opacity = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);
                if (opacity > 255) {
                    opacity = 255;
                }

                if (opacity > 0) {
                    int y = res.getScaledHeight() - 59;
                    if (!this.mc.playerController.shouldDrawHUD()) {
                        y += 14;
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    FontRenderer font = this.highlightingItemStack.getItem().getFontRenderer(this.highlightingItemStack);
                    int x;
                    if (font != null) {
                        x = (res.getScaledWidth() - font.getStringWidth(name)) / 2;
                        font.drawStringWithShadow(name, (float)x, (float)y, 16777215 | opacity << 24);
                    } else {
                        x = (res.getScaledWidth() - this.fontrenderer.getStringWidth(name)) / 2;
                        this.fontrenderer.drawStringWithShadow(name, (float)x, (float)y, 16777215 | opacity << 24);
                    }

                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }

            this.mc.mcProfiler.endSection();
        } else if (this.mc.thePlayer.isSpectator()) {
            this.spectatorGui.func_175263_a(res);
        }

    }

    protected void renderHUDText(int width, int height) {
        this.mc.mcProfiler.startSection("forgeHudText");
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        ArrayList<String> listL = new ArrayList();
        ArrayList<String> listR = new ArrayList();
        if (this.mc.isDemo()) {
            long time = this.mc.theWorld.getTotalWorldTime();
            if (time >= 120500L) {
                listR.add(I18n.format("demo.demoExpired", new Object[0]));
            } else {
                listR.add(I18n.format("demo.remainingTime", new Object[]{StringUtils.ticksToElapsedTime((int)(120500L - time))}));
            }
        }

        if (this.mc.gameSettings.showDebugInfo && !this.pre(ElementType.DEBUG)) {
            listL.addAll(this.debugOverlay.getLeft());
            listR.addAll(this.debugOverlay.getRight());
            this.post(ElementType.DEBUG);
        }

        Text event = new Text(this.eventParent, listL, listR);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            int top = 2;
            Iterator var7 = listL.iterator();

            String msg;
            while(var7.hasNext()) {
                msg = (String)var7.next();
                if (msg != null) {
                    drawRect(1, top - 1, 2 + this.fontrenderer.getStringWidth(msg) + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                    this.fontrenderer.drawString(msg, 2, top, 14737632);
                    top += this.fontrenderer.FONT_HEIGHT;
                }
            }

            top = 2;
            var7 = listR.iterator();

            while(var7.hasNext()) {
                msg = (String)var7.next();
                if (msg != null) {
                    int w = this.fontrenderer.getStringWidth(msg);
                    int left = width - 2 - w;
                    drawRect(left - 1, top - 1, left + w + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                    this.fontrenderer.drawString(msg, left, top, 14737632);
                    top += this.fontrenderer.FONT_HEIGHT;
                }
            }
        }

        this.mc.mcProfiler.endSection();
        this.post(ElementType.TEXT);
    }

    protected void renderRecordOverlay(int width, int height, float partialTicks) {
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            float hue = (float)this.recordPlayingUpFor - partialTicks;
            int opacity = (int)(hue * 256.0F / 20.0F);
            if (opacity > 255) {
                opacity = 255;
            }

            if (opacity > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(width / 2), (float)(height - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int color = this.recordIsPlaying ? Color.HSBtoRGB(hue / 50.0F, 0.7F, 0.6F) & 16777215 : 16777215;
                this.fontrenderer.drawString(this.recordPlaying, -this.fontrenderer.getStringWidth(this.recordPlaying) / 2, -4, color | opacity << 24);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

    }

    protected void renderTitle(int width, int height, float partialTicks) {
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float age = (float)this.field_175195_w - partialTicks;
            int opacity = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float f3 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - age;
                opacity = (int)(f3 * 255.0F / (float)this.field_175199_z);
            }

            if (this.field_175195_w <= this.field_175193_B) {
                opacity = (int)(age * 255.0F / (float)this.field_175193_B);
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
                this.getFontRenderer().drawString(this.field_175201_x, (float)(-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.getFontRenderer().drawString(this.field_175200_y, (float)(-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

    }

    protected void renderChat(int width, int height) {
        this.mc.mcProfiler.startSection("chat");
        Chat event = new Chat(this.eventParent, 0, height - 64);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)event.posX, (float)event.posY, 0.0F);
            this.persistantChatGUI.drawChat(this.updateCounter);
            GlStateManager.popMatrix();
            this.post(ElementType.CHAT);
            this.mc.mcProfiler.endSection();
        }
    }

    protected void renderPlayerList(int width, int height) {
        ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
        NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null)) {
            this.overlayPlayerList.updatePlayerList(true);
            if (this.pre(ElementType.PLAYER_LIST)) {
                return;
            }

            this.overlayPlayerList.renderPlayerlist(width, this.mc.theWorld.getScoreboard(), scoreobjective);
            this.post(ElementType.PLAYER_LIST);
        } else {
            this.overlayPlayerList.updatePlayerList(false);
        }

    }

    protected void renderHealthMount(int width, int height) {
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        Entity tmp = player.ridingEntity;
        if (tmp instanceof EntityLivingBase) {
            this.bind(icons);
            if (!this.pre(ElementType.HEALTHMOUNT)) {
                boolean unused = false;
                int left_align = width / 2 + 91;
                this.mc.mcProfiler.endStartSection("mountHealth");
                GlStateManager.enableBlend();
                EntityLivingBase mount = (EntityLivingBase)tmp;
                int health = (int)Math.ceil((double)mount.getHealth());
                float healthMax = mount.getMaxHealth();
                int hearts = (int)(healthMax + 0.5F) / 2;
                if (hearts > 30) {
                    hearts = 30;
                }

                int MARGIN = 1;
                int BACKGROUND = 52 + (unused ? 1 : 0);
                int HALF = 1;
                int FULL = 1;

                for(int heart = 0; hearts > 0; heart += 20) {
                    int top = height - right_height;
                    int rowCount = Math.min(hearts, 10);
                    hearts -= rowCount;

                    for(int i = 0; i < rowCount; ++i) {
                        int x = left_align - i * 8 - 9;
                        this.drawTexturedModalRect(x, top, BACKGROUND, 9, 9, 9);
                        if (i * 2 + 1 + heart < health) {
                            this.drawTexturedModalRect(x, top, 88, 9, 9, 9);
                        } else if (i * 2 + 1 + heart == health) {
                            this.drawTexturedModalRect(x, top, 97, 9, 9, 9);
                        }
                    }

                    right_height += 10;
                }

                GlStateManager.disableBlend();
                this.post(ElementType.HEALTHMOUNT);
            }
        }
    }

    private boolean pre(ElementType type) {
        return MinecraftForge.EVENT_BUS.post(new Pre(this.eventParent, type));
    }

    private void post(ElementType type) {
        MinecraftForge.EVENT_BUS.post(new Post(this.eventParent, type));
    }

    private void bind(ResourceLocation res) {
        this.mc.getTextureManager().bindTexture(res);
    }

    private class GuiOverlayDebugForge extends GuiOverlayDebug {
        private GuiOverlayDebugForge(Minecraft mc) {
            super(mc);
        }

        protected void renderDebugInfoLeft() {
        }

        protected void renderDebugInfoRight(ScaledResolution res) {
        }

        private List<String> getLeft() {
            return this.call();
        }

        private List<String> getRight() {
            return this.getDebugInfoRight();
        }
    }
}
