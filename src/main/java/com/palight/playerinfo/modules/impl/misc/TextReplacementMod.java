package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextReplacementMod extends Module {

    public static Map<String, String> directReplacements = new HashMap<>();
    public static Map<String, String> emojis = new HashMap<>();
    public static List<String> emojiList = new ArrayList<>();
    public static List<String> nameList = new ArrayList<>();

    static {
        String chars = "☠ ☮ ☯ ♠ Ω ♤ ♣ ♧ ♥ ♡ ♦ ♢ ♔ ♕ ♚ ♛ ⚜ ★ ☆ ✮ ✯ ☄ ☾ ☽ ☼ ☀ ☁ ☂ ☃ ☻ ☺ ☹ ۞ ۩ ε ї з Ƹ Ӝ̵ Ʒ ξ Ж З ж ☎ ☏ ¢ ☚ ☛ ☜ ☝ ☞ ☟ ✍ ✌ ☢ ☣ ♨ ๑ ❀ ✿ ψ ♆ ☪ ♪ ♩ ♫ ♬ ✄ ✂ ✆ ✉ ✦ ✧ ♱ ♰ ∞ ♂ ♀ ☿ ❤ ❥ ❦ ❧ ™ ® © ✖ ✗ ✘ ♒ ■ □ ▢ ▲ △ ▼ ▽ ◆ ◇ ○ ◎ ● ◯ Δ ◕ ◔ ʊ ϟ ღ ツ 回 ₪ ¿ ¡ ½ ⅓ ⅔ ¼ ¾ ⅛ ⅜ ⅝ ⅞ ℅ № ⇨ ❝ ❞ # & ℃ ∃ ∧ ∠ ∨ ∩ ⊂ ⊃ ∪ ⊥ ∀ Ξ Γ ɐ ə ɘ β ɟ ɥ ɯ ɔ и ๏ ɹ ʁ я ʌ ʍ λ ч Σ Π ➀ ➁ ➂ ➃ ➄ ➅ ➆ ➇ ➈ ➉ Ⓐ Ⓑ Ⓒ Ⓓ Ⓔ Ⓕ Ⓖ Ⓗ Ⓘ Ⓙ Ⓚ Ⓛ Ⓜ Ⓝ Ⓞ Ⓟ Ⓠ Ⓡ Ⓢ Ⓣ Ⓤ Ⓥ Ⓦ Ⓧ Ⓨ Ⓩ ⓐ ⓑ ⓒ ⓓ ⓔ ⓕ ⓖ ⓗ ⓘ ⓙ ⓚ ⓛ ⓜ ⓝ ⓞ ⓟ ⓠ ⓡ ⓢ ⓣ ⓤ ⓥ ⓦ ⓧ ⓨ ⓩ { ｡ ^ ‿ ( ) ☭ ℘ ℑ ℜ ℵ ♏ η α ◠ ◡ ╭ ╮ ╯ ╰ ⊙ ¤ ㊣ ▆ ▇ █ ▓ 〓 ≡ ╝ ╚ ╔ ╗ ╬ ═ ╓ ╩ ┠ ┨ ┯ ┷ ┏ ┓ ┗ ┛ ┳ ﹃ ﹄ ┌ ┐ └ ┘ ∟ 「 」 ↑ ↓ → ← ↘ ↙ ┇ ┅ ﹉ ﹊ ﹍ ﹎ * _ - ︵ ∵ ∴ ‖ ︱︳ ︴ ﹏ ﹋ ﹌ ► ◄ ▧ ▨ ◐ ◑ ↔ ↕ ▪ ▫ ▀ ▄ ▌ ▐ ░ ▒ ▬ ◊ ◦ ▣ ▤ ▥ ▦ ▩ ぃ ◘ ◙ ◈ ♭ の あ ￡ ✎ ✟ ஐ ≈ . ✲ ❈ ➹ ~ 【 】 ┱ ┲ ❣ ✚ ✪ ✣ ✤ ✥ ❉ ❃ ❂ ❁ ♈ ✓ ✔ ✕ ㊚ ㊛ : ﾟ ‘ ･ ▁ ▂ ▃ ▅ ⊮ ⊯ ⊰ ⊱ ⊲ ⊳ ⊴ ⊵ ⊶ ⊷ ⊸ ⊹ ⊺ ⊻ ⊼ ⊽ ⊾ ⊿ ⋀ ⋁ ⋂ ⋃ ⋄ ⋅ ⋆ ⋇ ⋈ ⋉ ⋊ ⋋ ⋌ ⋍ ⋎ ⋏ ⋐ ⋑ ⋒ ⋓ ⋔ ⋕ ⋖ ⋗ ⋘ ⋙ ⋚ ⋛ ⋜ ⋝ ⋞ ⋟ ⋠ ⋡ ⋢ ⋣ ⋤ ⋥ ⋦ ⋧ ⋨ ⋩ ⋪ ⋫ ⋬ ⋭ ⋮ ⋯ ⋰ ⋱ ⋲ ⋳ ⋴ ⋵ ⋶ ⋷ ⋸ ⋹ ⋺ ⋻ ⋼ ⋽ ⋾ ⋿ ⌀ ⌁ ⌂ ⌃ ⌄ ⌅ ⌆ ⌇ ⌈ ⌉ ⌊ ⌋";
        String[] charArray = chars.split(" ");
        for (String string : charArray) {
            String name = Character.getName(string.charAt(0)).toLowerCase().replaceAll(" ", "_");
            if (name.equals("bowtie"))
                name = "playerinfo";
            emojis.put(name, string);
            emojiList.add(string);
            nameList.add(name);
        }
        directReplacements.put("<3", "❤");
    }

    public TextReplacementMod() {
        super("textReplacement", "Text Replacement", "Replace specific keywords with emoji/unicode characters.", ModuleType.MISC, null, null);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (ModConfiguration.textReplacementModEnabled) {
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
}
