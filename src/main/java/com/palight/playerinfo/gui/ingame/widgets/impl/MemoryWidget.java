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
                drawText(displayString, this.getPosition().getX() + 2, this.getPosition().getY());
                offset = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getHeight(displayString));
                displayString = "Allocated: " + totalMemory / 1024L / 1024L + "MB";
                drawText(displayString, this.getPosition().getX() + 2, this.getPosition().getY() + offset);
                this.width = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(displayString)) + 4;
                break;
            default:
            case "percent":
                displayString = "Used: " + usedMemory * 100L / maxMemory + "%";
                int tempWidth = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(displayString)) + 4;
                drawText(displayString, this.getPosition().getX() + 2, this.getPosition().getY());
                offset = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getHeight(displayString));
                displayString = "Allocated: " + totalMemory * 100L / maxMemory + "%";
                this.width = Math.max(tempWidth, (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(displayString)) + 4);
                drawText(displayString, this.getPosition().getX() + 2, this.getPosition().getY() + offset);
                break;
        }
    }
}
