package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat extends Gui {
    @Final @Shadow private Minecraft mc;
    @Final @Shadow private List<ChatLine> drawnChatLines;
    @Final @Shadow private List<ChatLine> chatLines;
    @Shadow private int scrollPos;
    @Shadow private boolean isScrolled;

    private DisplayTweaksMod displayTweaksMod;
    /**
     * @reason Transparent Chat
     * @author palight
     */
    @Overwrite
    public void drawChat(int updateCounter) {
        if (displayTweaksMod == null) {
            displayTweaksMod = ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks"));
        }
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int lineCount = mc.ingameGUI.getChatGUI().getLineCount();
            boolean isChatOpen = false;
            int i = 0;
            int drawnLineCount = this.drawnChatLines.size();
            float opacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (drawnLineCount > 0) {
                if (mc.ingameGUI.getChatGUI().getChatOpen()) {
                    isChatOpen = true;
                }

                float chatScale = mc.ingameGUI.getChatGUI().getChatScale();
                int chatWidth = MathHelper.ceiling_float_int((float)mc.ingameGUI.getChatGUI().getChatWidth() / chatScale);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(chatScale, chatScale, 1.0F);

                int lineIndex;
                int lvt_11_1_;
                int color;
                for(lineIndex = 0; lineIndex + this.scrollPos < this.drawnChatLines.size() && lineIndex < lineCount; ++lineIndex) {
                    ChatLine chatLine = this.drawnChatLines.get(lineIndex + this.scrollPos);
                    if (chatLine != null) {
                        lvt_11_1_ = updateCounter - chatLine.getUpdatedCounter();
                        if (lvt_11_1_ < 200 || isChatOpen) {
                            double lvt_12_1_ = (double)lvt_11_1_ / 200.0D;
                            lvt_12_1_ = 1.0D - lvt_12_1_;
                            lvt_12_1_ *= 10.0D;
                            lvt_12_1_ = MathHelper.clamp_double(lvt_12_1_, 0.0D, 1.0D);
                            lvt_12_1_ *= lvt_12_1_;
                            color = (int)(255.0D * lvt_12_1_);
                            if (isChatOpen) {
                                color = 255;
                            }

                            color = (int)((float)color * opacity);
                            ++i;
                            if (color > 3) {
                                int x = 0;
                                int y = -lineIndex * 9;
                                if (!displayTweaksMod.transparentChat)
                                    drawRect(x, y - 9, x + chatWidth + 4, y, color / 2 << 24);
                                String chatText = chatLine.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(chatText, (float)x, (float)(y - 8), 16777215 + (color << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (isChatOpen) {
                    lineIndex = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int topLineY = drawnLineCount * lineIndex + drawnLineCount;
                    lvt_11_1_ = i * lineIndex + i;
                    int lineY = this.scrollPos * lvt_11_1_ / drawnLineCount;
                    int lvt_13_1_ = lvt_11_1_ * lvt_11_1_ / topLineY;
                    if (topLineY != lvt_11_1_) {
                        color = lineY > 0 ? 170 : 96;
                        int lvt_15_2_ = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -lineY, 2, -lineY - lvt_13_1_, lvt_15_2_ + (color << 24));
                        drawRect(2, -lineY, 1, -lineY - lvt_13_1_, 13421772 + (color << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * @reason Infinite Chat History
     * @author palight
     */
    @Overwrite
    private void setChatLine(IChatComponent chatComponent, int p_setChatLine_2_, int p_setChatLine_3_, boolean p_setChatLine_4_) {
        if (p_setChatLine_2_ != 0) {
            mc.ingameGUI.getChatGUI().deleteChatLine(p_setChatLine_2_);
        }

        int lvt_5_1_ = MathHelper.floor_float((float)mc.ingameGUI.getChatGUI().getChatWidth() / mc.ingameGUI.getChatGUI().getChatScale());
        List<IChatComponent> lvt_6_1_ = GuiUtilRenderComponents.splitText(chatComponent, lvt_5_1_, this.mc.fontRendererObj, false, false);
        boolean lvt_7_1_ = mc.ingameGUI.getChatGUI().getChatOpen();

        IChatComponent lvt_9_1_;
        for(Iterator lvt_8_1_ = lvt_6_1_.iterator(); lvt_8_1_.hasNext(); this.drawnChatLines.add(0, new ChatLine(p_setChatLine_3_, lvt_9_1_, p_setChatLine_2_))) {
            lvt_9_1_ = (IChatComponent)lvt_8_1_.next();
            if (lvt_7_1_ && this.scrollPos > 0) {
                this.isScrolled = true;
                mc.ingameGUI.getChatGUI().scroll(1);
            }
        }

        if (!p_setChatLine_4_) {
            this.chatLines.add(0, new ChatLine(p_setChatLine_3_, chatComponent, p_setChatLine_2_));
        }
    }
}
