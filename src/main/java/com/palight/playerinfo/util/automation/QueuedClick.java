package com.palight.playerinfo.util.automation;

import com.palight.playerinfo.mixin.IMixinMinecraft;
import net.minecraft.client.Minecraft;

public class QueuedClick extends QueuedAction {
    private ClickType clickType;

    public QueuedClick(int cooldown, ClickType clickType) {
        super(cooldown);
        this.clickType = clickType;
    }

    @Override
    public void run() {
        switch (this.clickType) {
            case LEFT:
                ((IMixinMinecraft) Minecraft.getMinecraft()).callClickMouse();
                break;
            case RIGHT:
                ((IMixinMinecraft) Minecraft.getMinecraft()).callRightClickMouse();
                break;
            case MIDDLE:
                ((IMixinMinecraft) Minecraft.getMinecraft()).callMiddleClickMouse();
                break;
        }
    }

    public enum ClickType {
        LEFT,
        RIGHT,
        MIDDLE;
    }
}
