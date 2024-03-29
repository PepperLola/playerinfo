package com.palight.playerinfo.modules.impl.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.impl.StatsOverlayWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.StatsGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.misc.DiscordRichPresenceMod;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.HttpUtil;
import com.palight.playerinfo.util.HypixelUtil;
import com.palight.playerinfo.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsMod extends Module {

    private static DiscordRichPresenceMod module;
    private static final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    public List<PlayerStats> toDisplay = new ArrayList<>();
    private boolean isInGame = false;
    private final Pattern whereAmIPattern = Pattern.compile("You are currently connected to server ([\\w\\d]+)");
    public static String currentDuelsType = "";
    private long lastJoinTimestamp = 0;
    private static final long joinCooldown = 2000; // 2 seconds

    @ConfigOption
    public boolean onlyShowOwnStats = false;

    public StatsMod() {
        super("stats", ModuleType.GUI, new StatsGui(), new StatsOverlayWidget());
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {

        if (!this.isEnabled() || !(event.entity instanceof EntityPlayer) || !event.world.isRemote || !DiscordRichPresenceMod.serverIp.contains("hypixel")) return;
        EntityPlayer player = ((EntityPlayer) event.entity);
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (player.getUniqueID().equals(clientPlayer.getUniqueID())) {
            if (System.currentTimeMillis() - lastJoinTimestamp > joinCooldown) {
                lastJoinTimestamp = System.currentTimeMillis();
                isInGame = false;
                getPlayerStats().clear();
                clientPlayer.sendChatMessage("/whereami");
            }
        } else {
            if (isInGame) {
                if (player.getUniqueID().version() == 2 || playerStats.containsKey(player.getUniqueID())) return;
                Multithreading.runAsync(
                    () -> {
                        if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                            //getting scoreboard title
                            String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                            //adds people to the list to be displayed
                            playerStats.put(
                                    player.getUniqueID(),
                                    new PlayerStats(
                                            player.getDisplayNameString(),
                                            player.getUniqueID(),
                                            Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()),
                                            GameType.getGameType(scoreboardTitle)
                                    )
                            );
                        }
                });
            }
        }

    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        handleLeave(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        handleLeave(event.player);
    }

    public void handleLeave(EntityPlayer player) {
        System.out.println(player.getName() + " | " + player.getUniqueID());
        playerStats.remove(player.getUniqueID());
        currentDuelsType = "";
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!this.isEnabled()) return;
        String message = ColorUtil.stripColor(event.message.getUnformattedText());
        Matcher matcher = whereAmIPattern.matcher(message);
        if (!matcher.matches()) return;
        String serverName = matcher.group(1);
//        System.out.println("[playerinfo] SERVER NAME: " + serverName + " | EQUALS: " + matcher.group(1).equals(serverName));
//        System.out.println("[playerinfo] CONTAINS MINI: " + serverName.contains("mini") + " | CONTAINS LOBBY: " + serverName.contains("lobby"));
        int players = Minecraft.getMinecraft().theWorld.playerEntities.size();
        int PLAYER_THRESHOLD = 24;
//        System.out.println("PLAYERS IN LOBBY: " + players + " | LESS THAN THRESHOLD: " + (players <= PLAYER_THRESHOLD));
        // server name must contain mini (for minigame server), not contain lobby, and have less than a specific amount of players
        isInGame = serverName.contains("mini") && players <= PLAYER_THRESHOLD && !serverName.contains("lobby");
//        System.out.println("SERVER NAME: " + serverName + " | IS IN GAME: " + isInGame);
        if (isInGame) {
            Multithreading.runAsync(
                () -> {
                    // get scoreboard title
                    if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                        String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
                        // add players to cache
                        for (EntityPlayer worldPlayer : Minecraft.getMinecraft().theWorld.playerEntities) {
                            if (playerStats.containsKey(worldPlayer.getUniqueID())) continue;
                            playerStats.put(
                                    worldPlayer.getUniqueID(),
                                    new PlayerStats(
                                            worldPlayer.getDisplayNameString(),
                                            worldPlayer.getUniqueID(),
                                            Minecraft.getMinecraft().getNetHandler().getPlayerInfo(worldPlayer.getUniqueID()),
                                            GameType.getGameType(scoreboardTitle)
                                    )
                            );
                        }
                    }
            });
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

    public enum DuelsDivision {
        NONE(0),
        ROOKIE(50),
        IRON(100),
        GOLD(250),
        DIAMOND(500),
        MASTER(1000),
        LEGEND(2000),
        GRANDMASTER(5000),
        GODLIKE(10000);

        int requiredWins;

        DuelsDivision(int requiredWins) {
            this.requiredWins = requiredWins;
        }

        public static DuelsDivision getDuelsDivision(int wins) {
            DuelsDivision[] values = DuelsDivision.values();
            ArrayUtils.reverse(values);

            for (int i = 0; i < values.length; i++) {
                DuelsDivision div = values[i];
                System.out.println(String.format("WINS: %d | REQUIRED: %d", wins, div.getRequiredWins()));
                if (wins > div.requiredWins) return div;
            }

            return DuelsDivision.NONE;
        }

        public static DuelsDivision getNext(DuelsDivision current) {
            DuelsDivision[] values = DuelsDivision.values();
            for (int i = 0; i < values.length - 1; i++) {
                DuelsDivision div = values[i];
                if (div == current) return values[i + 1];
            }

            return DuelsDivision.NONE;
        }

        public int getRequiredWins() {
            return requiredWins;
        }
    }

    //initializing a bunch of stuff
    public static class PlayerStats implements Comparable<PlayerStats> {
        private final GameType gameType;
        public String name;
        public UUID uuid;
        public int level;
        public int gameLevel;
        public double kdr;
        public double fkdr;
        public double wlr;
        public double bblr;
        public int wins;
        public int losses;
        public int ws;
        public String title;
        public int prestige;
        public HypixelUtil.Rank rank;
        public HypixelUtil.PlusColor plusColor;
        public NetworkPlayerInfo networkPlayerInfo;
        public DuelsDivision division;

        public boolean nicked = false;

        public PlayerStats(String name, UUID uuid, NetworkPlayerInfo networkPlayerInfo, GameType gameType) {
            this.name = name;
            this.uuid = uuid;
            this.networkPlayerInfo = networkPlayerInfo;
            this.gameType = gameType;
            this.rank = HypixelUtil.Rank.NONE;
            getStats();
        }

        public void getStats() {
            if (module == null) {
                module = (DiscordRichPresenceMod) PlayerInfo.getModules().get("discordRPC");
            }

            if (!module.isEnabled()) return;

            if (module.hypixelApiKey == null || module.hypixelApiKey.isEmpty()) {
                System.out.println("[playerinfo] HYPIXEL API KEY IS UNDEFINED!");
                return;
            }

            if (this.gameType == null) return;

            String requestGameType = this.gameType.getRequestName();
            // fetch player stats from playerinfo api
            String url = "https://api.jerlshoba.com/hypixel/stats/" + uuid.toString() + "/" + requestGameType + "?key=" + module.hypixelApiKey + "&get_status=true";
            HttpUtil.httpGet(url, response -> {
                String entity = EntityUtils.toString(response.getEntity());

                System.out.println(entity);

                int statusCode = response.getStatusLine().getStatusCode();

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(entity);
                JsonObject obj = element.getAsJsonObject();

                if (obj.isJsonNull()) return;

                // parse data if the request succeeds
                if (statusCode >= 200 && statusCode < 300) {
                    boolean online = obj.get("online").getAsBoolean();

                    if (!online) {
                        StatsMod.getPlayerStats().remove(this.uuid);
                    }

                    // sometimes some of these are null for some reason
                    // so we have to handle that by checking if they're null
                    String rank = "NONE";
                    if (obj.has("rank") && !obj.get("rank").isJsonNull())
                        rank = obj.get("rank").getAsString();

                    String plusColor = "RED";
                    if (obj.has("plusColor") && !obj.get("plusColor").isJsonNull())
                        plusColor = obj.get("plusColor").getAsString();

                    if (obj.has("level") && !obj.get("level").isJsonNull())
                        level = obj.get("level").getAsInt();

                    if (obj.has("kdr") && !obj.get("kdr").isJsonNull()) {
                        System.out.println("KDR: " + obj.get("kdr") + " IS JSON NULL? " + obj.get("kdr").isJsonNull());
                        kdr = obj.get("kdr").getAsDouble();
                    }

                    if (obj.has("wlr") && !obj.get("wlr").isJsonNull())
                        wlr = obj.get("wlr").getAsDouble();

                    if (obj.has("ws") && !obj.get("ws").isJsonNull())
                        ws = obj.get("winstreak").getAsInt();

                    if (obj.has("wins") && !obj.get("wins").isJsonNull())
                        wins = obj.get("wins").getAsInt();

                    if (obj.has("losses") && !obj.get("losses").isJsonNull())
                        losses = obj.get("losses").getAsInt();

                    this.rank = HypixelUtil.Rank.getRankFromAPIName(rank);
                    this.plusColor = HypixelUtil.PlusColor.getPlusColorFromName(plusColor);

                    // set game-specific stats
                    switch (this.gameType){
                        case BEDWARS:
                            if (obj.has("fkdr") || obj.get("fkdr").isJsonNull())
                                fkdr = obj.get("fkdr").getAsDouble();
                            if (obj.has("bblr") && !obj.get("bblr").isJsonNull())
                                bblr = obj.get("bblr").getAsDouble();
                            if (obj.has("gameLevel") && !obj.get("gameLevel").isJsonNull())
                                gameLevel = obj.get("gameLevel").getAsInt();
                            break;
                        case DUELS:
                            if (obj.has("title") && !obj.get("title").isJsonNull())
                                title = obj.get("title").getAsString();

                            this.division = DuelsDivision.getDuelsDivision(this.wins);

                            if (obj.has("prestige") && !obj.get("prestige").isJsonNull())
                                prestige = obj.get("prestige").getAsInt();
                            String duelsType = obj.get("gameType").getAsString();
                            if ((currentDuelsType == null || currentDuelsType.isEmpty()) && (duelsType != null && !duelsType.isEmpty())) {
                                currentDuelsType = duelsType;
                            }
                            break;
                    }

                } else {
                    // if the request fails and the player is nicked, they're a nick
                    // otherwise it's watchdog
                    if (statusCode == 404) {
//                        boolean success = obj.get("success").getAsBoolean();
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

        @Override
        public int compareTo(PlayerStats otherComparable) {
            System.out.println();
            if (otherComparable.nicked || otherComparable.getThreat() > this.getThreat()) {
                return 1;
            } else if (otherComparable.getThreat() < this.getThreat()) {
                return -1;
            }

            return 0;
        }

        public double getThreat() {
            // I made this up and have no idea if it's accurate
            return 10D * kdr + 10D * wlr + ws + + (level / 10D) + (this.nicked ? 1000 : 0);
        }

        public GameType getGameType() {
            return gameType;
        }

        @Override
        public String toString() {
            return "PlayerStats[name=" + this.name + ", nicked=" + this.nicked + ", threat=" + this.getThreat() + "]";
        }
    }


}