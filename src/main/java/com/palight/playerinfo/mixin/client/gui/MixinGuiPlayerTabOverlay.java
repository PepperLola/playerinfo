package com.palight.playerinfo.mixin.client.gui;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.rendering.CapeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay extends Gui {
    @Shadow @Final private Minecraft mc;
    @Shadow @Final private GuiIngame guiIngame;
    @Shadow private IChatComponent footer;
    @Shadow private IChatComponent header;
    @Shadow private long lastTimeOpened;
    @Shadow private boolean isBeingRendered;
    @Shadow @Final private static Ordering<NetworkPlayerInfo> field_175252_a;

    /**
     * @reason Add ability to render ping as text instead of bars
     * @author palight
     */
    @Overwrite
    protected void drawPing(int p_drawPing_1_, int p_drawPing_2_, int p_drawPing_3_, NetworkPlayerInfo info) {
        if (((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).renderPingAsText) {
            String ping = String.valueOf(info.getResponseTime());
            this.mc.fontRendererObj.drawString(ping, p_drawPing_2_ + p_drawPing_1_ - this.mc.fontRendererObj.getStringWidth(ping), p_drawPing_3_, 0xffffffff);
        } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
            byte offset;
            if (info.getResponseTime() < 0) {
                offset = 5;
            } else if (info.getResponseTime() < 150) {
                offset = 0;
            } else if (info.getResponseTime() < 300) {
                offset = 1;
            } else if (info.getResponseTime() < 600) {
                offset = 2;
            } else if (info.getResponseTime() < 1000) {
                offset = 3;
            } else {
                offset = 4;
            }

            this.zLevel += 100.0F;
            this.drawTexturedModalRect(p_drawPing_2_ + p_drawPing_1_ - 11, p_drawPing_3_, 0, 176 + offset * 8, 10, 8);
            this.zLevel -= 100.0F;
        }
    }

    /**
     * @reason Render playerinfo icon
     * @author palight
     */
    @Overwrite
    public void renderPlayerlist(int p_renderPlayerlist_1_, Scoreboard scoreboard, ScoreObjective objective) {
        NetHandlerPlayClient netHandlerPlayClient = this.mc.thePlayer.sendQueue;
        List<NetworkPlayerInfo> networkPlayerInfoList = field_175252_a.sortedCopy(netHandlerPlayClient.getPlayerInfoMap());
        int maxWidth = 0;
        int maxWidth2 = 0;
        Iterator networkPlayerInfoIterator = networkPlayerInfoList.iterator();

        int width;
        while(networkPlayerInfoIterator.hasNext()) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)networkPlayerInfoIterator.next();
            width = this.mc.fontRendererObj.getStringWidth(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkPlayerInfo));
            maxWidth = Math.max(maxWidth, width);
            if (objective != null && objective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                width = this.mc.fontRendererObj.getStringWidth(" " + scoreboard.getValueFromObjective(networkPlayerInfo.getGameProfile().getName(), objective).getScorePoints());
                maxWidth2 = Math.max(maxWidth2, width);
            }
        }

        networkPlayerInfoList = networkPlayerInfoList.subList(0, Math.min(networkPlayerInfoList.size(), 80));
        int length = networkPlayerInfoList.size();
        int tempLength = length;

        for(width = 1; tempLength > 20; tempLength = (length + width - 1) / width) {
            ++width;
        }

        boolean encrypted = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int lvt_12_3_;
        if (objective != null) {
            if (objective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                lvt_12_3_ = 90;
            } else {
                lvt_12_3_ = maxWidth2;
            }
        } else {
            lvt_12_3_ = 0;
        }

        int lvt_13_1_ = Math.min(width * ((encrypted ? 9 : 0) + maxWidth + lvt_12_3_ + 13), p_renderPlayerlist_1_ - 50) / width;
        int lvt_14_1_ = p_renderPlayerlist_1_ / 2 - (lvt_13_1_ * width + (width - 1) * 5) / 2;
        int lvt_15_1_ = 10;
        int lvt_16_1_ = lvt_13_1_ * width + (width - 1) * 5;
        List<String> lvt_17_1_ = null;
        List<String> lvt_18_1_ = null;
        Iterator lvt_19_5_;
        String lvt_20_5_;
        if (this.header != null) {
            lvt_17_1_ = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), p_renderPlayerlist_1_ - 50);

            for(lvt_19_5_ = lvt_17_1_.iterator(); lvt_19_5_.hasNext(); lvt_16_1_ = Math.max(lvt_16_1_, this.mc.fontRendererObj.getStringWidth(lvt_20_5_))) {
                lvt_20_5_ = (String)lvt_19_5_.next();
            }
        }

        if (this.footer != null) {
            lvt_18_1_ = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), p_renderPlayerlist_1_ - 50);

            for(lvt_19_5_ = lvt_18_1_.iterator(); lvt_19_5_.hasNext(); lvt_16_1_ = Math.max(lvt_16_1_, this.mc.fontRendererObj.getStringWidth(lvt_20_5_))) {
                lvt_20_5_ = (String)lvt_19_5_.next();
            }
        }

        int lvt_21_3_;
        if (lvt_17_1_ != null) {
            drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + lvt_17_1_.size() * this.mc.fontRendererObj.FONT_HEIGHT, -2147483648);

            for(lvt_19_5_ = lvt_17_1_.iterator(); lvt_19_5_.hasNext(); lvt_15_1_ += this.mc.fontRendererObj.FONT_HEIGHT) {
                lvt_20_5_ = (String)lvt_19_5_.next();
                lvt_21_3_ = this.mc.fontRendererObj.getStringWidth(lvt_20_5_);
                this.mc.fontRendererObj.drawStringWithShadow(lvt_20_5_, (float)(p_renderPlayerlist_1_ / 2 - lvt_21_3_ / 2), (float)lvt_15_1_, -1);
            }

            ++lvt_15_1_;
        }

        drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + tempLength * 9, -2147483648);

        for(int lvt_19_4_ = 0; lvt_19_4_ < length; ++lvt_19_4_) {
            int lvt_20_4_ = lvt_19_4_ / tempLength;
            lvt_21_3_ = lvt_19_4_ % tempLength;
            int xPosition = lvt_14_1_ + lvt_20_4_ * lvt_13_1_ + lvt_20_4_ * 5;
            int yPosition = lvt_15_1_ + lvt_21_3_ * 9;
            drawRect(xPosition, yPosition, xPosition + lvt_13_1_, yPosition + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            if (lvt_19_4_ < networkPlayerInfoList.size()) {
                NetworkPlayerInfo networkPlayerInfo = networkPlayerInfoList.get(lvt_19_4_);
                String playerName = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkPlayerInfo);
                GameProfile lvt_26_1_ = networkPlayerInfo.getGameProfile();
                if (encrypted) {
                    EntityPlayer lvt_27_1_ = this.mc.theWorld.getPlayerEntityByUUID(lvt_26_1_.getId());
                    boolean lvt_28_1_ = lvt_27_1_ != null && lvt_27_1_.isWearing(EnumPlayerModelParts.CAPE) && (lvt_26_1_.getName().equals("Dinnerbone") || lvt_26_1_.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
                    int lvt_29_1_ = 8 + (lvt_28_1_ ? 8 : 0);
                    int lvt_30_1_ = 8 * (lvt_28_1_ ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(xPosition, yPosition, 8.0F, (float)lvt_29_1_, 8, lvt_30_1_, 8, 8, 64.0F, 64.0F);
                    if (lvt_27_1_ != null && lvt_27_1_.isWearing(EnumPlayerModelParts.HAT)) {
                        int lvt_31_1_ = 8 + (lvt_28_1_ ? 8 : 0);
                        int lvt_32_1_ = 8 * (lvt_28_1_ ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(xPosition, yPosition, 40.0F, (float)lvt_31_1_, 8, lvt_32_1_, 8, 8, 64.0F, 64.0F);
                    }

                    xPosition += 9;
                }

                if (networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    playerName = EnumChatFormatting.ITALIC + playerName;
                    this.mc.fontRendererObj.drawStringWithShadow(playerName, (float)xPosition, (float)yPosition, -1862270977);
                } else {
                    int xOffset = 0;
                    UUID uuid = networkPlayerInfo.getGameProfile().getId();
                    if (uuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                        xOffset = 8;
                    } else if (CapeHandler.PLAYER_DATA.containsKey(uuid)) {
                        if (CapeHandler.PLAYER_DATA.get(uuid).isOnline()) {
                            xOffset = 8;
                        }
                    }
                    if (xOffset > 0) {
                        mc.getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "icons/icon-32x.png"));
                        Gui.drawScaledCustomSizeModalRect(
                                xPosition,
                                yPosition,
                                0,
                                0,
                                32,
                                32,
                                8,
                                8,
                                32,
                                32
                        );
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(playerName, (float)xPosition + xOffset, (float)yPosition, -1);
                }

                if (objective != null && networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    int lvt_27_2_ = xPosition + maxWidth + 1;
                    int lvt_28_2_ = lvt_27_2_ + lvt_12_3_;
                    if (lvt_28_2_ - lvt_27_2_ > 5) {
                        ((IMixinGuiPlayerTabOverlay) this).callDrawScoreboardValues(objective, yPosition, lvt_26_1_.getName(), lvt_27_2_, lvt_28_2_, networkPlayerInfo);
                    }
                }

                this.drawPing(lvt_13_1_, xPosition - (encrypted ? 9 : 0), yPosition, networkPlayerInfo);
            }
        }

        if (lvt_18_1_ != null) {
            lvt_15_1_ += tempLength * 9 + 1;
            drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + lvt_18_1_.size() * this.mc.fontRendererObj.FONT_HEIGHT, -2147483648);

            for(lvt_19_5_ = lvt_18_1_.iterator(); lvt_19_5_.hasNext(); lvt_15_1_ += this.mc.fontRendererObj.FONT_HEIGHT) {
                lvt_20_5_ = (String)lvt_19_5_.next();
                lvt_21_3_ = this.mc.fontRendererObj.getStringWidth(lvt_20_5_);
                this.mc.fontRendererObj.drawStringWithShadow(lvt_20_5_, (float)(p_renderPlayerlist_1_ / 2 - lvt_21_3_ / 2), (float)lvt_15_1_, -1);
            }
        }

    }
}
