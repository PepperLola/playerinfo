package com.palight.playerinfo.rendering;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.util.EntityUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {
    public static ConcurrentHashMap<UUID, PlayerData> PLAYER_DATA = new ConcurrentHashMap<>();

    public static void fetchCape(UUID uuid) {
        HttpUtil.httpGet("https://api.jerlshoba.com/user/" + uuid.toString().replaceAll("-", ""),
            response -> {
                String entity = EntityUtils.toString(response.getEntity());

                System.out.println(entity);

                JsonElement element = new JsonParser().parse(entity);
                if (element == null || element.isJsonNull()) return;
                JsonObject obj = element.getAsJsonObject();
                if (obj == null || obj.isJsonNull()) return;

                JsonElement newElement = obj.get("data");
                if (newElement == null || newElement.isJsonNull()) return;
                obj = newElement.getAsJsonObject();
                if (obj == null || obj.isJsonNull()) return;

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    String capeName = obj.get("cape").getAsString();
                    Cape cape = Cape.getCape(capeName);

                    boolean online = obj.get("online").getAsBoolean();
                    String minecraft_uuid = obj.get("minecraft_uuid").getAsString();

                    long lastOnline = obj.get("last_online").getAsLong();

                    if (cape != null) {
                        PLAYER_DATA.put(uuid, new PlayerData(cape, online, minecraft_uuid, lastOnline));
                    } else {
                        PLAYER_DATA.put(uuid, new PlayerData(Cape.NONE, online, minecraft_uuid, lastOnline));
                    }
                } else {
                    PLAYER_DATA.put(uuid, new PlayerData(Cape.NONE, false, null, 0));
                }
            });
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = ((EntityPlayer) event.entity);

        if (!PLAYER_DATA.containsKey(player.getUniqueID())) {
            fetchCape(player.getUniqueID());
        }
    }

    public static class PlayerData {
        private Cape cape;
        private boolean online;
        private String uuid;
        private long lastOnline;

        public PlayerData(Cape cape, boolean online, String uuid, long lastOnline) {
            this.cape = cape;
            this.online = online;
            this.uuid = uuid;
            this.lastOnline = lastOnline;
        }

        public Cape getCape() {
            return cape;
        }

        public void setCape(Cape cape) {
            this.cape = cape;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public long getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(long lastOnline) {
            this.lastOnline = lastOnline;
        }
    }
}
