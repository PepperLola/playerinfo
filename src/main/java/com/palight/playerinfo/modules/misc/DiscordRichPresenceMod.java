package com.palight.playerinfo.modules.misc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.ServerJoinEvent;
import com.palight.playerinfo.gui.screens.options.modules.gui.CustomMainMenuGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DiscordRichPresenceMod extends Module {
    public DiscordRichPresenceMod() {
        super("discord-rpc", "Discord RPC", "Enabled Discord Rich Presence", ModuleType.MISC, null, null);
    }

    public static long applicationId = 568255570407063553L;
    public static String steamId = "";

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.discordRPCEnabled);
        if (ModConfiguration.discordRPCEnabled) {

            /* Discord Rich Presence */
            IPCClient client = new IPCClient(applicationId);
            client.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    RichPresence.Builder builder = new RichPresence.Builder();
                    builder.setState(PlayerInfo.NAME + " v" + PlayerInfo.VERSION);
                    client.sendRichPresence(builder.build());
                }
            });
            try {
                client.connect();
            } catch (NoDiscordClientException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISCORD, "discordRPCEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    public void setDiscordState(DiscordState state) {

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (event.gui instanceof GuiMainMenu || event.gui instanceof CustomMainMenuGui) {
            setDiscordState(DiscordState.MAIN_MENU);
        }
    }

    @SubscribeEvent
    public void onConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        setDiscordState(event.isLocal ? DiscordState.SINGLEPLAYER : DiscordState.MULTIPLAYER);
    }

    @SubscribeEvent
    public void onServerJoin(ServerJoinEvent event) {
        System.out.println(String.format("CONNECTED TO: %s", event.getServer()));
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
                    return "Playing on a server";
            }
            return "";
        }
    }
}


