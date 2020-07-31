package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class GuiDropdown extends GuiCustomWidget {
    protected static final ResourceLocation textures = new ResourceLocation(PlayerInfo.MODID, "textures/gui/widgets.png");
    public String[] displayStrings;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    public int packedFGColour;
    public boolean expanded = false;
    private int segmentWidth = 16;

    public GuiDropdown(int id, int x, int y, String[] displayStrings) {
        this(id, x, y, 64, 16, displayStrings);
    }

    public GuiDropdown(int id, int x, int y, int width, int height, String[] displayStrings) {
        super(id, x, y, width, height);
        this.width = 64;
        this.height = 16;
        this.enabled = true;
        this.visible = true;
        this.width = width - (width % segmentWidth); // MUST BE A MULTIPLE OF 16 FOR HORIZONTAL TILING TO WORK
        this.displayStrings = displayStrings;
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            //int hoverState = this.getHoverState(this.hovered);
            int selectedItem = getHoveredItem(mouseX, mouseY);
            expanded = NumberUtil.isBetween(selectedItem, 0, displayStrings.length - 1);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            drawVertical();

            this.mouseDragged(mc, mouseX, mouseY);

            for (int i = 0; i < displayStrings.length; i++) {
                if (!expanded && i > 0) break;

                int fgColor = 14737632;

                if (i == selectedItem) {
                    if (this.packedFGColour != 0) {
                        fgColor = this.packedFGColour;
                    } else if (!this.enabled) {
                        fgColor = 10526880;
                    } else {
                        fgColor = 16777120;
                    }
                }

                this.drawCenteredString(fontrenderer, this.displayStrings[i], this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2 + (this.height * i), fgColor);
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public void drawHorizontal(int x, int y, int yOffset) {
        int segments = (width - (width % segmentWidth)) / segmentWidth;

        for (int i = 0; i < segments; i++) {
            if (i == 0) {
                this.drawTexturedModalRect(x + (i * segmentWidth), y, 0, yOffset, segmentWidth, height);
            } else if (i == segments - 1){
                this.drawTexturedModalRect(x + (i * segmentWidth), y, segmentWidth * 2, yOffset, segmentWidth, height);
            } else {
                this.drawTexturedModalRect(x + (i * segmentWidth), y, segmentWidth, yOffset, segmentWidth, height);
            }
        }
    }

    public void drawVertical() {
        if (expanded) {
            if (displayStrings.length > 1) {
                drawHorizontal(xPosition, yPosition, height);
                for (int i = 1; i < displayStrings.length; i++) {
                    if (i != displayStrings.length - 1) {
                        drawHorizontal(xPosition, yPosition + (height * i), height * 2);
                    } else {
                        drawHorizontal(xPosition, yPosition + (height * i), height * 3);
                    }
                }
            } else {
                drawHorizontal(xPosition, yPosition, 0);
            }
        } else {
            drawHorizontal(xPosition, yPosition, 0);
        }
    }

    public void setSelectedItem(String item) {
        if (!ArrayUtils.contains(displayStrings, item)) return;
        displayStrings = NumberUtil.prependElement(displayStrings, item);
    }

    public int getHoveredItem(int mouseX, int mouseY) {
        boolean xCorrect = NumberUtil.isBetween(mouseX, xPosition, xPosition + width);
        boolean yCorrect;

        int selected = -1;

        if (expanded) {
            yCorrect = NumberUtil.isBetween(mouseY, yPosition, yPosition + this.height * (displayStrings.length + 1));

            if (xCorrect && yCorrect) {
                int heightDiff = mouseY - yPosition;
                selected = ((heightDiff) - (heightDiff % 16)) / 16;
            }
        } else {
            yCorrect = NumberUtil.isBetween(mouseY, yPosition, yPosition + height);
            if (xCorrect && yCorrect) {
                selected = 0;
            }
        }

        return selected;
    }

    public String getSelectedItem() {
        return displayStrings[0];
    }

    public String getClickedItem(int index) {
        return displayStrings[index];
    }

    protected void mouseDragged(Minecraft p_mouseDragged_1_, int p_mouseDragged_2_, int p_mouseDragged_3_) {
    }

    public void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY, int btn) {
        if (!(enabled && visible)) return false;

        int selectedItem = getHoveredItem(mouseX, mouseY);
        if (selectedItem == -1) return false;
        if (!NumberUtil.isBetween(selectedItem, 0, displayStrings.length - 1)) return false;
        if (getClickedItem(selectedItem).equals(getSelectedItem())) return false;

        displayStrings = NumberUtil.prependElement(displayStrings, getClickedItem(selectedItem));
        return true;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int p_drawButtonForegroundLayer_1_, int p_drawButtonForegroundLayer_2_) {
    }

    public void playPressSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
