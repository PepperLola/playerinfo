package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.impl.CPSWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;

public class CPSMod extends Module {

    private static ArrayDeque<Long> leftClicks = new ArrayDeque<>();
    private static ArrayDeque<Long> rightClicks = new ArrayDeque<>();

    public CPSMod() {
        super("cps", "CPS Mod", "Display your CPS", ModuleType.MISC, null, new CPSWidget(4, 64));
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.cpsModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "cpsModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    public static int getLeftClicks() {
        return leftClicks.size();
    }

    public static int getRightClicks() {
        return rightClicks.size();
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (!this.isEnabled()) return;
        if (event.button == 0 && event.buttonstate) {
            leftClicks.addLast(System.currentTimeMillis());
        }
        if (event.button == 1 && event.buttonstate) {
            rightClicks.addLast(System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!this.isEnabled()) return;
        // remove left clicks
        removeClicks(leftClicks);

        // remove right clicks
        removeClicks(rightClicks);
    }

    private void removeClicks(ArrayDeque<Long> leftClicks) {
        while (true) {
            if (leftClicks.size() == 0) {
                break;
            }
            long clickTime = leftClicks.getFirst();
            long currentTime = System.currentTimeMillis();
            if (currentTime - clickTime >= 1000) {
                leftClicks.removeFirst();
            } else {
                break;
            }
        }
    }
}
