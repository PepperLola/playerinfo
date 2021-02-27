package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.events.HypixelEvent;
import com.palight.playerinfo.gui.ingame.widgets.impl.HypixelEventWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.misc.HypixelEventsGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelEventsMod extends Module {

    @ConfigOption
    public boolean friendAlerts = false;

    @ConfigOption
    public String alertSound = "none";

    public HypixelEventsMod() {
        super("hypixelEvents", "Hypixel Events", "Useful events for Hypixel.", ModuleType.MISC, new HypixelEventsGui(), new HypixelEventWidget());
    }

    @SubscribeEvent
    public void onReceiveChatEvent(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.contains("Friend > ")) {
            String regexString = "Friend > (\\w*) (\\w*).";
            Matcher matcher = Pattern.compile(regexString).matcher(msg);
            String username;
            if (matcher.find()) {
                username = matcher.group(1);
                String eventTypeString = matcher.group(2).toLowerCase();
                HypixelEvent.FriendEvent.FriendEventType eventType = HypixelEvent.FriendEvent.FriendEventType.getType(eventTypeString);
                MinecraftForge.EVENT_BUS.post(new HypixelEvent.FriendEvent(username, eventType));

                if (!ModConfiguration.alertSound.equals("none")) playAlert(AlertType.FRIEND);
            }
        }
    }

    private void playAlert(AlertType type) {
        SoundHandler sh = Minecraft.getMinecraft().getSoundHandler();
        switch (type) {
            case FRIEND:
                sh.playSound(PositionedSoundRecord.create(new ResourceLocation(ModConfiguration.alertSound), 1.0f));
                break;
            default:
                break;
        }
    }

    private enum AlertType {
        FRIEND,
        GUILD
    }
}
