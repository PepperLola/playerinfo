package com.palight.playerinfo.listeners;

import com.palight.playerinfo.events.HypixelEvent;
import com.palight.playerinfo.macro.Macro;
import com.palight.playerinfo.macro.MacroConfig;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MacroEventHandler {
    @SubscribeEvent
    public void onEvent(Event e) throws IllegalAccessException {
        if (e.getClass().getSimpleName().toLowerCase().contains("update")) return;

        String eventName = e.getClass().getSimpleName();

        if (!MacroConfig.macros.containsKey(eventName)) return;

        Macro macro = MacroConfig.macros.get(e.getClass().getSimpleName());

        Map<String, String> replacements = new HashMap<>();

        for (Field declaredField : e.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            replacements.put(declaredField.getName(), declaredField.get(e).toString());
        }

        if (e instanceof HypixelEvent.FriendEvent) {
            HypixelEvent.FriendEvent friendEvent = (HypixelEvent.FriendEvent) e;
            replacements.put("username", friendEvent.getUsername());
            replacements.put("type", friendEvent.getType().toString().toLowerCase());
        } else if (e instanceof ClientChatReceivedEvent) {
            ClientChatReceivedEvent clientChatReceivedEvent = (ClientChatReceivedEvent) e;
            replacements.put("message", clientChatReceivedEvent.message.getUnformattedText());
        }

        macro.run(replacements);
    }
}
