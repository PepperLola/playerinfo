package com.palight.playerinfo.util.automation;

public abstract class QueuedAction {
    protected int cooldown;

    public QueuedAction(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean tick() {
        cooldown = Math.max(-1, cooldown - 1);

        boolean shouldRun = cooldown == -1;
        if (shouldRun) this.run();

        return shouldRun;
    }

    public abstract void run();
}
