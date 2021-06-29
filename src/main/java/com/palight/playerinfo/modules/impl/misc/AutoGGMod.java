package com.palight.playerinfo.modules.impl.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.HypixelEvent;
import com.palight.playerinfo.events.ServerJoinEvent;
import com.palight.playerinfo.gui.screens.impl.options.modules.misc.AutoGGGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.ApiUtil;
import com.palight.playerinfo.util.HttpUtil;
import com.palight.playerinfo.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AutoGGMod extends Module {

    // comma separated messages
    @ConfigOption
    public String messages = "gg";
    // comma separated delays (in seconds)
    @ConfigOption
    public String delays = "0";

    private Pattern winPattern;

    private final List<Server> servers = new ArrayList<>();
    private Server currentServer;

    public AutoGGMod() {
        super("autogg", "AutoGG", "Sends GG after games on Hypixel.", ModuleType.MISC, new AutoGGGui(), null);

        HttpUtil.httpGet(ApiUtil.API_URL + "/resources/autogg-triggers", response -> {
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity);
            JsonElement element = PlayerInfo.parser.parse(entityString);
            JsonArray array = element.getAsJsonArray();

            for (JsonElement serverElement : array) {
                JsonObject obj = serverElement.getAsJsonObject();
                String name = obj.get("name").getAsString();
                String id = obj.get("id").getAsString();
                String prefix = obj.get("messagePrefix").getAsString();
                String ipPattern = obj.get("ip").getAsString();

                JsonArray fetchedTriggers = obj.getAsJsonArray("triggers");

                Server server = new Server(name, id, ipPattern, prefix);

                System.out.println("CREATED SERVER WITH NAME " + name + " | ID " + id + " | IP PATTERN: " + ipPattern + " | PREFIX " + prefix);

                for (JsonElement triggerElement : fetchedTriggers) {
                    String game = triggerElement.getAsJsonObject().get("game").getAsString();
                    String triggerString = triggerElement.getAsJsonObject().get("value").getAsString();
                    Trigger trigger = new Trigger(game, triggerString);

                    server.addTrigger(trigger);
                }

                servers.add(server);
            }
        });
    }

    @SubscribeEvent
    public void onPlayerJoin(ServerJoinEvent event) {
        System.out.println("JOINING SERVER " + event.getServer());
        winPattern = Pattern.compile("^ *(\\[[\\w+]+\\])? *" + Minecraft.getSessionInfo().get("X-Minecraft-Username") + " *WINNER! *(\\[[\\w+]+\\])? *\\w+$");
        for (Server server : servers) {
            System.out.println("SERVER FIND? " + server.getIp() + " | " + Pattern.compile(server.getIp()).matcher(event.getServer()).find());
            if (Pattern.compile(server.getIp()).matcher(event.getServer()).find()) {
                currentServer = server;
                return;
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!this.isEnabled()) return;
        String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        if (winPattern.matcher(unformatted).find()) {
            // player won!
            System.out.println("YOU WON A GAME!");
            MinecraftForge.EVENT_BUS.post(new HypixelEvent.GameEvent(HypixelEvent.GameEvent.GameEventType.WIN));
        }

        if (currentServer == null) return;
        for (Trigger trigger : currentServer.getTriggers()) {
            if (Pattern.compile(trigger.getPattern()).matcher(unformatted).find()) {
                sayGG();
                return;
            }
        }
    }

    public void sayGG() {
        String[] messages = this.messages.split(",");
        double[] delays = Arrays.stream(this.delays.split(",")).mapToDouble(Double::parseDouble).toArray();

        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            double messageDelay = 0;
            if (delays.length > i) {
                messageDelay = delays[i];
            } else if (delays.length > 0) {
                messageDelay = delays[delays.length - 1];
            }

            long finalDelay = (long) Math.floor(messageDelay * 1000);

            Multithreading.schedule(
                    () -> Minecraft.getMinecraft().thePlayer.sendChatMessage(currentServer.getMessagePrefix() + " " + message),
                    finalDelay,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    public static class Server {
        private final String name;
        private final String id;
        private final String ip;
        private final String messagePrefix;
        private final List<Trigger> triggers;

        public Server(String name, String id, String ip, String messagePrefix, List<Trigger> triggers) {
            this.name = name;
            this.id = id;
            this.ip = ip;
            this.messagePrefix = messagePrefix;
            this.triggers = triggers;
        }

        public Server(String name, String id, String ip, String messagePrefix) {
            this(name, id, ip, messagePrefix, new ArrayList<>());
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getIp() {
            return ip;
        }

        public String getMessagePrefix() {
            return messagePrefix;
        }

        public List<Trigger> getTriggers() {
            return triggers;
        }

        public void addTrigger(Trigger trigger) {
            this.triggers.add(trigger);
        }
    }

    public static class Trigger {
        private final String game;
        private final String pattern;

        public Trigger(String game, String pattern) {
            this.game = game;
            this.pattern = pattern;
        }

        public String getGame() {
            return game;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
