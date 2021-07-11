package com.palight.playerinfo.util.automation;

import net.minecraft.client.settings.KeyBinding;

public class QueuedKeypress extends QueuedAction {
    private int keyId;

    public QueuedKeypress(int cooldown, int keyId) {
        super(cooldown);
        this.keyId = keyId;
    }

    @Override
    public void run() {
        KeyBinding.setKeyBindState(keyId, true);
    }
}
