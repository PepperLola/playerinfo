package com.palight.playerinfo.rendering.cosmetics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.util.ApiUtil;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {
    public static ConcurrentHashMap<UUID, PlayerData> PLAYER_DATA = new ConcurrentHashMap<>();

    private int counter = 0;

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
                    String capeName = obj.get("cape").getAsString().toLowerCase();

                    boolean online = obj.get("online").getAsBoolean();
                    String minecraft_uuid = obj.get("minecraft_uuid").getAsString();

                    long lastOnline = obj.get("last_online").getAsLong();

                    if (!Cosmetics.CAPES.containsKey(capeName)) {
                        PLAYER_DATA.put(uuid, new PlayerData(null, online, minecraft_uuid, lastOnline));
                        return;
                    }

                    Cape cape = Cosmetics.getCape(capeName);

                    if (cape.getImage() == null) {
                        cape.setImage(ApiUtil.downloadImage(
                                ApiUtil.API_URL + "/cosmetics/cape/texture/" + capeName.toLowerCase()
                        ));
                        cape.calculateFrames();
                    }

                    PLAYER_DATA.put(uuid, new PlayerData(cape, online, minecraft_uuid, lastOnline));
                } else {
                    PLAYER_DATA.put(uuid, new PlayerData(null, false, null, 0));
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

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        counter ++;
        counter %= 20;

        PLAYER_DATA.forEach((uuid, data) -> {
            if (data == null || data.getCape() == null) return;
            if (counter % (20 / data.getCape().getFPS()) == 0) {
                data.updateFrame();
            }
        });
    }

    public static class PlayerData {
        private Cape cape;
        private boolean online;
        private String uuid;
        private long lastOnline;
        private int frame = 0;
        private TreeMap<Integer, Integer> dataIds = new TreeMap<>();

        public PlayerData(@Nullable Cape cape, boolean online, String uuid, long lastOnline) {
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

        public int getFrame() {
            return frame;
        }

        public void setFrame(int frame) {
            this.frame = frame;
        }

        public void setDataId(int frame, int id) {
            dataIds.put(frame, id);
        }

        public Integer getDataId(int frame) {
            return dataIds.get(frame);
        }

        public void updateFrame() {
            if (cape.isAnimated()) {
                this.frame++;
                this.frame %= cape.getTotalFrames();
            }
        }
    }
}
