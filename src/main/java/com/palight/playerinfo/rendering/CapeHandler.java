package com.palight.playerinfo.rendering;

import com.google.gson.reflect.TypeToken;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {
    public static ConcurrentHashMap<UUID, Cape> CAPES = new ConcurrentHashMap<>();

    public static void fetchCape(UUID uuid) {
        HttpUtil.httpGet("https://api.jerlshoba.com/cosmetics/cape/" + uuid.toString().replaceAll("-", ""),
            response -> {
                String entity = EntityUtils.toString(response.getEntity());
                Map<String, Object> data = PlayerInfo.gson.fromJson(entity, new TypeToken<Map<String, Object>>(){}.getType());

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    String capeName = String.valueOf(data.get("cape"));
                    Cape cape = Cape.getCape(capeName);
                    System.out.println("CAPE " + cape);
                    if (cape != null) {
                        CAPES.put(uuid, cape);
                    } else {
                        CAPES.put(uuid, Cape.NONE);
                    }
                } else {
                    CAPES.put(uuid, Cape.NONE);
                }
            });
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = ((EntityPlayer) event.entity);

        if (!CAPES.containsKey(player.getUniqueID())) {
            fetchCape(player.getUniqueID());
        }
    }
}
