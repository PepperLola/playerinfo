package com.palight.playerinfo.modules.impl.util;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.automation.QueuedAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class AutomationMod extends Module {

    private QueuedAction currentAction = null;
    private final Queue<QueuedAction> queuedActions = new ArrayDeque<>();

    public AutomationMod() {
        super("automation", ModuleType.UTIL, null, null);
    }

    public void addQueuedAction(QueuedAction action) {
        queuedActions.add(action);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (currentAction == null) {
            if (!queuedActions.isEmpty()) {
                currentAction = queuedActions.remove();
            }
        } else {
            boolean didRun = currentAction.tick();
            if (didRun) {
                currentAction = null;
            }
        }
    }
}
