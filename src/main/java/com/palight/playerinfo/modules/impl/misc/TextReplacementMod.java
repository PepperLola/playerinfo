package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class TextReplacementMod extends Module {

    public static Map<String, String> directReplacements = new HashMap<>();
    public static Map<String, String> emojis = new HashMap<>();

    static {
        directReplacements.put("<3", "❤");
        emojis.put("biohazard", "☣");
        emojis.put("radioactive", "☢");
    }

    public TextReplacementMod() {
        super("textReplacement", "Text Replacement", "Replace specific keywords with emoji/unicode characters.", ModuleType.MISC, null, null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.textReplacementModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "textReplacementModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText();
        for (String s : directReplacements.keySet()) {
            message = message.replaceAll(s, directReplacements.get(s));
        }

        for (String emojiName : emojis.keySet()) {
            message = message.replaceAll(String.format(":%s:", emojiName), emojis.get(emojiName));
        }

        event.message = new ChatComponentText(message);
    }
}
