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
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DiscordRichPresenceMod extends Module {
    public DiscordRichPresenceMod() {
        super("discordPresence", "Discord RPC", "Enabled Discord Rich Presence", ModuleType.MISC, null, null);
        if (this.enabled) {
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

    public static long applicationId = 568255570407063553L;
    public static String steamId = "";
    public static String serverIp = "";
    public static DiscordState discordState = DiscordState.MAIN_MENU;
    int updateTicksMax = 20 * 60;
    int updateTicks = 0;
    private static IPCClient client;

    public static void updateDiscord() {
        if (ModConfiguration.discordRPCEnabled) {
            new Thread(() -> {
                System.out.println("DISCORD STATE: " + DiscordState.getDisplayString(discordState));
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setState(DiscordState.getDisplayString(discordState))
                        .setLargeImage("minecraft", Minecraft.getMinecraft().getSession().getUsername())
                        .setSmallImage("playerinfo", PlayerInfo.MODID + " v" + PlayerInfo.VERSION);
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
        if (event.gui instanceof GuiMainMenu || event.gui instanceof CustomMainMenuGui || event.gui instanceof GuiScreenServerList) {
            setDiscordState(DiscordState.MAIN_MENU);
        }
    }

    @SubscribeEvent
    public void onConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        setDiscordState(event.isLocal ? DiscordState.SINGLEPLAYER : DiscordState.MULTIPLAYER);
    }

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (updateTicks >= updateTicksMax) {
            updateDiscord();
            updateTicks = 0;
        }

        updateTicks ++;
    }

    @SubscribeEvent
    public void onServerJoin(ServerJoinEvent event) {
        System.out.println(String.format("CONNECTED TO: %s", event.getServer()));
        serverIp = event.getServer();
        updateDiscord();
    }

    @SubscribeEvent
    public void onScoreboardUpdate(ScoreboardTitleChangeEvent event) {
        if (serverIp.contains("hypixel.net")) {
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
                    if (serverIp.contains("hypixel.net") && !ModConfiguration.hypixelApiKey.equals("")) {
                        return "Playing " + MCUtil.getPlayerStatus() + " on Hypixel";
                    }
                    return "Playing on " + serverIp;
            }
            return "";
        }
    }
}


