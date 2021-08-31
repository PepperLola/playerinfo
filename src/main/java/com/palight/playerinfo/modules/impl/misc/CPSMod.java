package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.impl.CPSWidget;
import com.palight.playerinfo.modules.Module;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;

public class CPSMod extends Module {

    public static ArrayDeque<Long> leftClicks = new ArrayDeque<>();
    public static ArrayDeque<Long> rightClicks = new ArrayDeque<>();

    public CPSMod() {
        super("cps", ModuleType.MISC, null, new CPSWidget(4, 64));
    }

    public static int getLeftClicks() {
        return leftClicks.size();
    }

    public static int getRightClicks() {
        return rightClicks.size();
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (event.button == 0 && event.buttonstate) {
            leftClicks.addLast(System.currentTimeMillis());
        }
        if (event.button == 1 && event.buttonstate) {
            rightClicks.addLast(System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
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
