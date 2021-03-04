package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.StatsOverlayWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class StatsMod extends Module {

    private final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    private boolean isInGame = false;
    private Pattern whereAmIPattern = Pattern.compile("You are currently connected to server ([\\w\\d]+)");

    public StatsMod() {
        super("stats", "Stats Overlay", "Shows the Hypixel stats of people in your game", ModuleType.GUI, null, new StatsOverlayWidget());
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = ((EntityPlayer) event.entity);
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (player.getUniqueID().equals(clientPlayer.getUniqueID())) {
            clientPlayer.sendChatMessage("/whereami");
        }

    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (!whereAmIPattern.matcher(message).matches()) return;
        String serverName = whereAmIPattern.matcher(message).group(1);
        isInGame = serverName.contains("mini") && Minecraft.getMinecraft().theWorld.playerEntities.size() <= 16;
        if (isInGame) {
            Thread thread = new Thread() {
                public void run() {
                    if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                        String scoreboardTitle = ColorUtil.stripColor(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().trim().replace("\u00A7[0-9a-zA-Z]", ""));
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

    public Map<UUID, PlayerStats> getPlayerStats(){
        return playerStats;
    }

    public static class PlayerStats {
        public String name;
        public UUID uuid;
        public int level;
        public int karma;
        public int gameLevel;
        public double kdr;
        public double fkdr;
        public double wlr;
        public double bblr;
        public boolean nicked;

        private final GameType gameType;

        public PlayerStats(String name, UUID uuid, GameType gameType) {
            this.name = name;
            this.uuid = uuid;
            this.gameType = gameType;
            getStats();
        }

        public void getStats() {
            level = -1;
            karma = -1;
            gameLevel = -1;
            kdr = -1;
            fkdr = -1;
            wlr = -1;
            bblr = -1;
            nicked = false;
        }

        public GameType getGameType() {
            return gameType;
        }
    }

    public enum GameType {
        BEDWARS("BED WARS"),
        DUELS("DUELS");

        String scoreboardName;
        GameType(String scoreboardName) {
            this.scoreboardName = scoreboardName;
        }

        public String getScoreboardName() {
            return this.scoreboardName;
        }

        public static GameType getGameType(String scoreboardTitle) {
            for (GameType gameType : GameType.values()) {
                if (scoreboardTitle.equals(gameType.getScoreboardName()))
                    return gameType;
            }

            return null;
        }
    }
}