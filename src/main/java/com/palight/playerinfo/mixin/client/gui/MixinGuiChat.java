package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.gui.widgets.impl.GuiEmojiPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Arrays;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {

    @Shadow private int sentHistoryCursor;
    @Shadow private GuiTextField inputField;
    @Shadow private String defaultInputFieldText;

    private GuiEmojiPicker emojiPicker;

    private GuiButton emojiButton;
    private boolean pickerEnabled = false;

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

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    public void handleMouseInput(CallbackInfo ci) {
        int i = Mouse.getEventDWheel();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) Math.floor((Mouse.getX() * 1.0F / Display.getWidth()) * res.getScaledWidth());
        int y = (int) Math.floor(((res.getScaledHeight() - Mouse.getY()) * 1.0F + (Display.getHeight() / 2.0F)) / Display.getHeight() * res.getScaledHeight());
        this.emojiPicker.handleScrolling(x, y, i);
    }
}
