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
import com.palight.playerinfo.util.HypixelUtil;
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

    private static DiscordRichPresenceMod module;
    private static final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    private boolean isInGame = false;
    private Pattern whereAmIPattern = Pattern.compile("You are currently connected to server ([\\w\\d]+)");
    public static String currentDuelsType;

    public StatsMod() {
        super("stats", "Stats Overlay", "Shows the Hypixel stats of people in your game", ModuleType.GUI, null, new StatsOverlayWidget());
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {

        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = ((EntityPlayer) event.entity);
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (player.getUniqueID().equals(clientPlayer.getUniqueID())) {
            isInGame = false;
            getPlayerStats().clear();
            clientPlayer.sendChatMessage("/whereami");
        } else {
            if (isInGame) {
                Thread thread = new Thread() {
                    public void run() {
                        if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                            String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                            System.out.println("SCOREBOARD TITLE: " + scoreboardTitle);
                            //getting scoreboard title

                            if (playerStats.containsKey(player.getUniqueID())) return;
                            System.out.println("ADDING PLAYER: " + player.getDisplayNameString());
                            playerStats.put(player.getUniqueID(), new PlayerStats(player.getDisplayNameString(), player.getUniqueID(), GameType.getGameType(scoreboardTitle)));
                            //adds people to the list to be displayed
                        }
                        this.stop();
                    }
                };

                thread.start();
            }
        }

    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = ColorUtil.stripColor(event.message.getUnformattedText());
        Matcher matcher = whereAmIPattern.matcher(message);
        if (!matcher.matches()) return;
        String serverName = matcher.group(1);
        System.out.println("[playerinfo] SERVER NAME: " + serverName + " | EQUALS: " + matcher.group(1).equals(serverName));
        System.out.println("[playerinfo] CONTAINS MINI: " + serverName.contains("mini") + " | CONTAINS LOBBY: " + serverName.contains("lobby"));
        int players = Minecraft.getMinecraft().theWorld.playerEntities.size();
        int PLAYER_THRESHOLD = 24;
        System.out.println("PLAYERS IN LOBBY: " + players + " | LESS THAN THRESHOLD: " + (players <= PLAYER_THRESHOLD));
        // server name must contain mini (for minigame server), not contain lobby, and have less than a specific amount of players
        isInGame = serverName.contains("mini") && players <= PLAYER_THRESHOLD && !serverName.contains("lobby");
        System.out.println("SERVER NAME: " + serverName + " | IS IN GAME: " + isInGame);
        if (isInGame) {
            Thread thread = new Thread() {
                public void run() {
                    // get scoreboard title
                    if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                        String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                        // add players to cache
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


    public static Map<UUID, PlayerStats> getPlayerStats() {
        return StatsMod.playerStats;
    }

    public enum GameType {
        BEDWARS("BED WARS", "bw"),
        DUELS("DUELS", "duels");

        String scoreboardName;
        String requestName;

        GameType(String scoreboardName, String requestName) {
            this.scoreboardName = scoreboardName;
            this.requestName = requestName;
        }

        public static GameType getGameType(String scoreboardTitle) {
            for (GameType gameType : GameType.values()) {
                if (scoreboardTitle.equals(gameType.getScoreboardName()) || scoreboardTitle.equals(gameType.getRequestName()))
                    return gameType;
            }

            return null;
        }

        public String getScoreboardName() {
            return this.scoreboardName;
        }

        public String getRequestName() { return this.requestName; }
    }

    //initializing a bunch of stuff
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
        public int ws;
        public String title;
        public int prestige;
        public HypixelUtil.Rank rank;
        public HypixelUtil.PlusColor plusColor;

        public boolean nicked = false;

        public PlayerStats(String name, UUID uuid, GameType gameType) {
            this.name = name;
            this.uuid = uuid;
            this.gameType = gameType;
            this.rank = HypixelUtil.Rank.NONE;
            getStats();
        }

        public void getStats() {
            if (module == null) {
                module = (DiscordRichPresenceMod) PlayerInfo.getModules().get("discordRPC");
            }

            if (module.hypixelApiKey == null || module.hypixelApiKey.isEmpty()) {
                System.out.println("[playerinfo] HYPIXEL API KEY IS UNDEFINED!");
                return;
            } else {
                System.out.println("[playerinfo] HYPIXEL API KEY IS DEFINED!");
            }
            String requestGameType = this.gameType.getRequestName();
            // fetch player stats from playerinfo api
            String url = "https://api.jerlshoba.com/hypixel/stats/" + uuid.toString() + "/" + requestGameType + "?key=" + module.hypixelApiKey + "&get_status=true";
            HttpUtil.httpGet(url, response -> {
                String entity = EntityUtils.toString(response.getEntity());

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("[playerinfo] HYPIXEL RESPONSE STATUS CODE: " + statusCode);

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(entity);
                JsonObject obj = element.getAsJsonObject();

                // parse data if the request succeeds
                if (statusCode >= 200 && statusCode < 300) {
                    System.out.println("[playerinfo] HYPIXEL REQUEST IS OK");

                    boolean online = obj.get("online").getAsBoolean();

                    if (!online) {
                        StatsMod.getPlayerStats().remove(this.uuid);
                    }

                    // sometimes some of these are null for some reason
                    // so we have to handle that by checking if they're null
                    String rank = "NONE";
                    if (obj.get("rank") != null) {
                        rank = obj.get("rank").getAsString();
                    }

                    String plusColor = "RED";
                    if (obj.get("plusColor") != null) {
                        plusColor = obj.get("plusColor").getAsString();
                    }

                    if(obj.get("level") != null) {
                        level = obj.get("level").getAsInt();
                    }

                    if (obj.get("kdr") != null)
                        kdr = obj.get("kdr").getAsDouble();

                    if (obj.get("wlr") != null)
                        wlr = obj.get("wlr").getAsDouble();

                    if (obj.get("ws") != null)
                        ws = obj.get("winstreak").getAsInt();

                    this.rank = HypixelUtil.Rank.getRankFromAPIName(rank);
                    this.plusColor = HypixelUtil.PlusColor.getPlusColorFromName(plusColor);

                    System.out.println("[playerinfo] HYPIXEL GAME TYPE: " + this.gameType.toString());

                    // set game-specific stats
                    switch (this.gameType){
                        case BEDWARS:
                            if (obj.get("fkdr") != null)
                                fkdr = obj.get("fkdr").getAsDouble();
                            if (obj.get("bblr") != null)
                                bblr = obj.get("bblr").getAsDouble();
                            if (obj.get("gameLevel") != null)
                                gameLevel = obj.get("gameLevel").getAsInt();
                            break;
                        case DUELS:
                            title = obj.get("title").getAsString();
                            prestige = obj.get("prestige").getAsInt();
                            String duelsType = obj.get("gameType").getAsString();
                            if ((currentDuelsType == null || currentDuelsType.isEmpty()) && (duelsType != null && !duelsType.isEmpty())) {
                                currentDuelsType = duelsType;
                            }
                            break;
                    }

                } else {
                    System.out.println("[playerinfo] HYPIXEL STATUS CODE IS NOT OK");
                    // if the request fails and the player is nicked, they're a nick
                    // otherwise it's watchdog
                    if (statusCode == 404) {
                        boolean success = obj.get("success").getAsBoolean();
                        boolean isNicked = obj.get("nicked").getAsBoolean();
                        if (isNicked) {
                            nicked = true;
                        } else {
                            // remove watchdog
                            StatsMod.getPlayerStats().remove(this.uuid);
                        }
                    }
                }
            });

        }

        public GameType getGameType() {
            return gameType;
        }
    }


}