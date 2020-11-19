package com.palight.playerinfo.listeners;

import com.palight.playerinfo.options.ModConfiguration;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    /**
     * Client chat listener for Hypixel API key
     * @param event Client chat received event. Posted by Minecraft when the client receives a chat message.
     */
    @SubscribeEvent
    public void onReceiveChatEvent(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.length() == 54) {
            String regexString = "Your API key is [a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";

            Matcher matcher = Pattern.compile(regexString).matcher(msg);
            if (matcher.matches()) {
                String apiKey = msg.replaceAll("Your API key is ", "");
                System.out.println("API KEY: " + apiKey);
                ModConfiguration.writeConfig(ModConfiguration.CATEGORY_HYPIXEL, "hypixelApiKey", apiKey);
                ModConfiguration.syncFromGUI();
            }
        } else if (msg.length() == 56) {
            String regexString = "Your new API key is [a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
            Matcher matcher = Pattern.compile(regexString).matcher(msg);
            if (matcher.matches()) {
                String apiKey = msg.replaceAll("Your new API key is ", "");
                System.out.println("API KEY: " + apiKey);
                ModConfiguration.writeConfig(ModConfiguration.CATEGORY_HYPIXEL, "hypixelApiKey", apiKey);
                ModConfiguration.syncFromGUI();
            }
        }
    }
}
