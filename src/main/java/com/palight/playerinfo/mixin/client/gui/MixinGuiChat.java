package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.gui.widgets.impl.GuiEmojiPicker;
import com.palight.playerinfo.modules.impl.misc.TextReplacementMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {

    @Shadow protected GuiTextField inputField;

    private GuiEmojiPicker emojiPicker;

    private GuiButton emojiButton;
    private boolean pickerEnabled = false;
    private int selectedIndex = 0;
    private String[] words;
    private String lastWord;

    private List<String> filteredNames = new ArrayList<>();

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        this.inputField.width = this.inputField.width - 512;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        this.emojiButton = new GuiButton(2, res.getScaledWidth() - 64 - 4, res.getScaledHeight() - 12 - 24, 64, 20, "Emoji");
        this.buttonList.addAll(Arrays.asList(
                this.emojiButton
        ));

        this.emojiPicker = new GuiEmojiPicker(1, res.getScaledWidth() - this.emojiButton.width - 100 - 4, res.getScaledHeight() - 12 - 100 - 4, 100, 100, 5, 5);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_, CallbackInfo ci) {
        if (pickerEnabled)
            emojiPicker.drawWidget(Minecraft.getMinecraft(), mouseX, mouseY);

        // draw autocomplete menu
        if (words != null && words.length > 0) {
            String lastWord = words[words.length - 1];
            if (!lastWord.equals("") && !lastWord.equals(":") && lastWord.startsWith(":") && !lastWord.endsWith(":")) {

                ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
                FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
                int height = filteredNames.size() * fr.FONT_HEIGHT;
                int width = 0;
                for (String filteredName : filteredNames) {
                    if (fr.getStringWidth(filteredName) > width)
                        width = fr.getStringWidth(filteredName);
                }
                width += 4;
                int x = 4 + fr.getStringWidth(inputField.getText());
                int y = res.getScaledHeight() - 4 - 12 - height;

                this.drawGradientRect(x, y, x + width, y + height, 0x99000000, 0x99000000);

                int offset = 0;
                for (int i = 0; i < filteredNames.size(); i++) {
                    String filteredName = filteredNames.get(i);
                    int color = 0xff777777;
                    if (i == getSelectedEmojiIndex()) {
                        // draw selected one in white
                        // subtract index from size because selected one at index 0 will be the bottom one, which is the last element in the list
                        color = 0xffffffff;
                    }
                    fr.drawString(filteredName, x + 2, y + offset, color);
                    offset += fr.FONT_HEIGHT;
                }
            }
        }
    }

    private int getSelectedEmojiIndex() {
        return filteredNames.size() - selectedIndex - 1;
    }

    private String getSelectedEmoji() {
        if (filteredNames.size() <= 0) {
            return "";
        }

        return filteredNames.get(getSelectedEmojiIndex());
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(int mouseX, int mouseY, int btn, CallbackInfo ci) {
        String emoji = this.emojiPicker.widgetClicked(mouseX, mouseY);
        if (!emoji.equals("")) {
            inputField.writeText(String.format(":%s:", emoji));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == this.emojiButton.id) {
            pickerEnabled = !pickerEnabled;
        }
        super.actionPerformed(button);
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"), cancellable = true)
    public void handleMouseInput(CallbackInfo ci) {
        int i = Mouse.getEventDWheel();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) Math.floor((Mouse.getX() * 1.0F / Display.getWidth()) * res.getScaledWidth());
        int y = (int) Math.floor(((res.getScaledHeight() - Mouse.getY()) * 1.0F + (Display.getHeight() / 2.0F)) / Display.getHeight() * res.getScaledHeight());
        this.emojiPicker.handleScrolling(x, y, i);
    }

    /**
     * @author palight
     */
    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    protected void keyTyped(char character, int keyCode, CallbackInfo ci) {
        // up = 200
        // down = 208
        // tab = 15
        if (filteredNames.size() > 0) {
            if (Keyboard.getEventKey() == 15) {
                if (lastWord.length() == 0) return;
                this.inputField.setText(this.inputField.getText().substring(0, this.inputField.getText().lastIndexOf(lastWord)) + String.format(":%s:", getSelectedEmoji()));
            } else if (Keyboard.getEventKey() == 200) {
                selectedIndex++;
                if (selectedIndex >= filteredNames.size())
                    selectedIndex %= filteredNames.size();
                ci.cancel();
            } else if (Keyboard.getEventKey() == 208) {
                selectedIndex--;
                if (selectedIndex < 0)
                    selectedIndex += filteredNames.size();
                ci.cancel();
            }
        }
    }

    @Inject(method = "keyTyped", at = @At("RETURN"))
    protected void keyTypedReturn(char character, int keyCode, CallbackInfo ci) {
        words = this.inputField.getText().split(" ");
        if (words.length <= 0) return;
        lastWord = words[words.length - 1];
        if (lastWord.length() == 0) return;
        filteredNames.clear();

        for (String name : TextReplacementMod.nameList) {
            if (name.startsWith(lastWord.replaceAll(":", ""))) {
                filteredNames.add(name);
            }
        }

        if (filteredNames.size() > 0) {
            if (selectedIndex >= filteredNames.size()) {
                selectedIndex %= filteredNames.size();
            } else if (selectedIndex < 0) {
                while (selectedIndex < 0) {
                    selectedIndex += filteredNames.size();
                }
            }
        }
    }
}
