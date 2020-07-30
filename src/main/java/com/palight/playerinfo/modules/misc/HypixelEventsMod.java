package com.palight.playerinfo.modules.misc;

import com.palight.playerinfo.events.HypixelFriendJoinEvent;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelEventsMod extends Module {
    public HypixelEventsMod() {
        super("hypixelEvents", "Hypixel Events", "Useful events for Hypixel.", ModuleType.MISC, null, null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.hypixelEventsModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "hypixelEventsModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onReceiveChatEvent(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.contains("Friend > ")) {
            String regexString = "Friend > (\\w*) joined.";
            Matcher matcher = Pattern.compile(regexString).matcher(msg);
            String username;
            if (matcher.find()) {
                username = matcher.group(1);
                MinecraftForge.EVENT_BUS.post(new HypixelFriendJoinEvent(username));
            }
        }
    }
}
