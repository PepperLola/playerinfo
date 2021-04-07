package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.misc.MemoryMod;
import net.minecraft.client.Minecraft;

public class MemoryWidget extends GuiIngameWidget {

    private MemoryMod module;

    public MemoryWidget() {
        super(-1, -1, 64, 22);
    }

    @Override
    public void render(Minecraft mc) {

        if (module == null) {
            module = (MemoryMod) PlayerInfo.getModules().get("memory");
        }

        super.render(mc);

        long maxMemory = Runtime.getRuntime().maxMemory(); // bytes
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        String displayString;
        int offset;
        switch (module.format.toLowerCase()) {
            case "raw":
                displayString = "Used: " + usedMemory / 1024L / 1024L + "MB";
                offset = (int) PlayerInfo.instance.fontRendererObj.getHeight(displayString);
                drawTextVerticallyCentered(displayString, this.getPosition().getX() + 2, this.getPosition().getY() + (offset / 2) + 1);

                this.height = offset * 2 + 1;

                displayString = "Allocated: " + totalMemory / 1024L / 1024L + "MB";
                this.width = (int) PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4;

                drawTextVerticallyCentered(displayString, this.getPosition().getX() + 2, (int) (this.getPosition().getY() + offset * 1.5) + 1);
                break;
            default:
            case "percent":
                displayString = "Used: " + usedMemory * 100L / maxMemory + "%";
                int tempWidth = (int) PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4;
                offset = (int) PlayerInfo.instance.fontRendererObj.getHeight(displayString);
                drawTextVerticallyCentered(displayString, this.getPosition().getX() + 2, this.getPosition().getY() + (offset / 2) + 1);

                this.height = offset * 2 + 1;

                displayString = "Allocated: " + totalMemory * 100L / maxMemory + "%";
                this.width = Math.max(tempWidth, (int) PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);

                drawTextVerticallyCentered(displayString, this.getPosition().getX() + 2, (int) (this.getPosition().getY() + offset * 1.5) + 1);
                break;
        }
    }
}
