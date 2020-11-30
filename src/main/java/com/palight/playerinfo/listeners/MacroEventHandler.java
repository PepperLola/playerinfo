package com.palight.playerinfo.listeners;

import com.palight.playerinfo.macro.Macro;
import com.palight.playerinfo.macro.MacroConfig;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class MacroEventHandler {
    @SubscribeEvent
    public void onEvent(Event e) {
//        System.out.println(e.getClass().getSimpleName());
//        Macro macro = MacroConfig.macros.get(e.getClass().getSimpleName());
//        if (macro != null) {
//            macro.run();
//        }
    }

    @SubscribeEvent
    public void onChatEvent(ClientChatReceivedEvent event) {
        Macro macro = MacroConfig.macros.get("messageReceive");
        if (macro != null) {
            Map<String, String> replacements = new HashMap<>();
            replacements.put("message", event.message.getUnformattedText());
            macro.run(replacements);
        }
    }
}
