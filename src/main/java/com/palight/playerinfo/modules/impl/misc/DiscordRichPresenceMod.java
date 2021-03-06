package com.palight.playerinfo.modules.impl.misc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.ScoreboardTitleChangeEvent;
import com.palight.playerinfo.events.ServerJoinEvent;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.CustomMainMenuGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class DiscordRichPresenceMod extends Module {

    @ConfigOption
    public String hypixelApiKey = "";

    public static long applicationId = 568255570407063553L;
    public static String steamId = "";
    public static String serverIp = "";
    public static DiscordState discordState = DiscordState.MAIN_MENU;
    int updateTicksMax = 20 * 60;
    int updateTicks = 0;
    private static IPCClient client;

    public static long START_TIME = -1;

    public DiscordRichPresenceMod() {
        super("discordRPC", ModuleType.MISC, null, null);
    }

    public static void updateDiscord() {
        boolean enabled = PlayerInfo.getModules().get("discordRPC").isEnabled();
        if (client == null) {
            if (enabled) {
                /* Discord Rich Presence */
                client = new IPCClient(applicationId);
                client.setListener(new IPCListener() {
                    @Override
                    public void onReady(IPCClient client) {
                        updateDiscord();
                    }
                });
                try {
                    client.connect();
                } catch (NoDiscordClientException e) {
                    e.printStackTrace();
                }
            }
        }
        if (enabled) {
            new Thread(() -> {
                System.out.println("DISCORD STATE: " + DiscordState.getDisplayString(discordState));
                String currentGame = serverIp.contains("hypixel") ? MCUtil.getPlayerStatus().toLowerCase().replaceAll(" ", "_") : "hypixel";
                System.out.println("CURRENT GAME: " + currentGame);
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setState(DiscordState.getDisplayString(discordState))
                        .setLargeImage(serverIp.contains("hypixel") ? currentGame : "minecraft", Minecraft.getMinecraft().getSession().getUsername())
                        .setSmallImage("playerinfo", PlayerInfo.MODID + " v" + PlayerInfo.VERSION)
                        .setStartTimestamp(OffsetDateTime.ofInstant(Instant.ofEpochMilli(START_TIME), ZoneId.systemDefault()));
                client.sendRichPresence(builder.build());
            }).start();
        }
    }

    public void setDiscordState(DiscordState state) {
        discordState = state;
        updateDiscord();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (event.gui instanceof GuiMainMenu || event.gui instanceof CustomMainMenuGui || event.gui instanceof GuiScreenServerList || event.gui instanceof GuiMultiplayer) {
            setDiscordState(DiscordState.MAIN_MENU);
            serverIp = "";
        }
    }

    /**
     * Update every so often to set Discord state
     * @param event
     */
    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (updateTicks >= updateTicksMax) {
            if (Minecraft.getMinecraft().theWorld != null) {
                setDiscordState(Minecraft.getMinecraft().theWorld.isRemote ? DiscordState.MULTIPLAYER : DiscordState.SINGLEPLAYER);
                updateDiscord();
            }
            updateTicks = 0;
        }

        updateTicks ++;
    }

    /**
     * Handle singleplayer world join
     * @param event
     */
    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer &&
                event.entity.getUniqueID().toString().replaceAll("-", "")
                        .equals(Minecraft.getSessionInfo().get("X-Minecraft-UUID"))) {
            if (event.world != null && !event.world.isRemote) {
                setDiscordState(DiscordState.SINGLEPLAYER);
            }
        }
    }

    /**
     * Handle multiplayer server join
     * @param event
     */
    @SubscribeEvent
    public void onServerJoin(ServerJoinEvent event) {
        System.out.printf("CONNECTED TO: %s%n", event.getServer());
        setDiscordState(DiscordState.MULTIPLAYER);
        serverIp = event.getServer();
        START_TIME = System.currentTimeMillis();
        updateDiscord();
    }

    @SubscribeEvent
    public void onServerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        System.out.println("DISCONNECTED FROM " + serverIp);
        START_TIME = -1;
        serverIp = "";
    }

    /**
     * Updates on scoreboard change to update Hypixel minigame in presence
     * @param event
     */
    @SubscribeEvent
    public void onScoreboardUpdate(ScoreboardTitleChangeEvent event) {
        if (serverIp.contains("hypixel")) {
            setDiscordState(DiscordState.MULTIPLAYER);
            updateDiscord();
        }
    }

    public enum DiscordState {
        MAIN_MENU,
        SINGLEPLAYER,
        MULTIPLAYER;

        public static String getDisplayString(DiscordState state) {
            switch (state) {
                case MAIN_MENU:
                    return "In Main Menu";
                case SINGLEPLAYER:
                    return "In a singleplayer world";
                case MULTIPLAYER:
                    if (serverIp.contains("hypixel") && !((DiscordRichPresenceMod) PlayerInfo.getModules().get("discordRPC")).hypixelApiKey.equals("")) {
                        return "Playing " + MCUtil.getPlayerStatus() + " on Hypixel";
                    }
                    return "Playing on " + serverIp;
            }
            return "";
        }
    }
}


