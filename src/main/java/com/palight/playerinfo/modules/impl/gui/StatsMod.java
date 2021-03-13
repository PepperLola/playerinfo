package com.palight.playerinfo.modules.impl.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.impl.StatsOverlayWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.misc.DiscordRichPresenceMod;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsMod extends Module {

    private final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    private boolean isInGame = false;
    private Pattern whereAmIPattern = Pattern.compile("You are currently connected to server ([\\w\\d]+)");

    private static DiscordRichPresenceMod module;

    public StatsMod() {
        super("stats", "Stats Overlay", "Shows the Hypixel stats of people in your game", ModuleType.GUI, null, new StatsOverlayWidget());
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {

        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = ((EntityPlayer) event.entity);
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (player.getUniqueID().equals(clientPlayer.getUniqueID())) {
            for (UUID uuid : getPlayerStats().keySet()) {
                if (!uuid.equals(clientPlayer.getUniqueID())) {
                    getPlayerStats().remove(uuid);
                }
            }
            clientPlayer.sendChatMessage("/whereami");
        } else {
            Thread thread = new Thread() {
                public void run() {
                    if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                        String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                        System.out.println("SCOREBOARD TITLE: " + scoreboardTitle);
                        if (playerStats.containsKey(player.getUniqueID())) return;
                        playerStats.put(player.getUniqueID(), new PlayerStats(player.getDisplayNameString(), player.getUniqueID(), GameType.getGameType(scoreboardTitle)));
                    }
                    this.stop();
                }
            };

            thread.start();
        }

    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = ColorUtil.stripColor(event.message.getUnformattedText());
        Matcher matcher = whereAmIPattern.matcher(message);
        if (!matcher.matches()) return;
        System.out.println("GROUP 0: " + matcher.group(0));
        System.out.println("GROUP 1: " + matcher.group(1));
        String serverName = matcher.group(1);
        isInGame = serverName.contains("mini") && Minecraft.getMinecraft().theWorld.playerEntities.size() <= 16 && !serverName.contains("lobby");
        System.out.println("SERVER NAME: " + serverName + " | IS IN GAME: " + isInGame);
        if (isInGame) {
            Thread thread = new Thread() {
                public void run() {
                    if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                        String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                        System.out.println("SCOREBOARD TITLE: " + scoreboardTitle);
                        for (EntityPlayer worldPlayer : Minecraft.getMinecraft().theWorld.playerEntities) {
                            if (playerStats.containsKey(worldPlayer.getUniqueID())) continue;
                            playerStats.put(worldPlayer.getUniqueID(), new PlayerStats(worldPlayer.getDisplayNameString(), worldPlayer.getUniqueID(), GameType.getGameType(scoreboardTitle)));
                        }
                    }
                    this.stop();
                }
            };

            thread.start();
        }

    }


    public Map<UUID, PlayerStats> getPlayerStats() {
        return playerStats;
    }

    public enum GameType {
        BEDWARS("BED WARS"),
        DUELS("DUELS");

        String scoreboardName;

        GameType(String scoreboardName) {
            this.scoreboardName = scoreboardName;
        }

        public static GameType getGameType(String scoreboardTitle) {
            for (GameType gameType : GameType.values()) {
                if (scoreboardTitle.equals(gameType.getScoreboardName()))
                    return gameType;
            }

            return null;
        }

        public String getScoreboardName() {
            return this.scoreboardName;
        }
    }

    public static class PlayerStats {
        private final GameType gameType;
        public String name;
        public UUID uuid;
        public int level;
        public int gameLevel;
        public double kdr;
        public double fkdr;
        public double wlr;
        public double bblr;
        public boolean nicked = false;

        public PlayerStats(String name, UUID uuid, GameType gameType) {
            this.name = name;
            this.uuid = uuid;
            this.gameType = gameType;
            getStats();
        }

        public void getStats() {
            if (module == null) {
                module = (DiscordRichPresenceMod) PlayerInfo.getModules().get("discordRPC");
            }

            if (module.hypixelApiKey == null || module.hypixelApiKey.isEmpty()) {
                System.out.println("HYPIXEL API KEY IS UNDEFINED!");
                return;
            } else {
                System.out.println("HYPIXEL API KEY IS DEFINED!");
            }
            String url = "https://api.jerlshoba.com/hypixel/stats/" + uuid.toString() + "/bw?key=" + module.hypixelApiKey;
            System.out.println("URL FOR HYPIXEL REQUEST: " + url);
            HttpUtil.httpGet(url, response -> {
                String entity = EntityUtils.toString(response.getEntity());
                System.out.println("HYPIXEL RESPONSE ENTITY: " + entity);

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("HYPIXEL RESPONSE STATUS CODE: " + statusCode);
                if (statusCode >= 200 && statusCode < 300) {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entity);
                    JsonObject obj = element.getAsJsonObject();

                    System.out.println(obj.toString());

                    level = obj.get("level").getAsInt();
                    gameLevel = obj.get("gameLevel").getAsInt();
                    kdr = obj.get("kdr").getAsDouble();
                    wlr = obj.get("wlr").getAsDouble();
                    fkdr = obj.get("fkdr").getAsDouble();
                    bblr = obj.get("bblr").getAsDouble();

                } else {
                    nicked = true;
                }
            });
        }

        public GameType getGameType() {
            return gameType;
        }
    }
}