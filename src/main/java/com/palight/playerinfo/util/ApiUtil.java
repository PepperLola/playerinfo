package com.palight.playerinfo.util;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.DiscordRichPresenceMod;
import net.minecraft.client.Minecraft;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
    public static final String API_URL = "https://api.jerlshoba.com";
    public static String TOKEN;

    public static void authenticate(String username, String password) {
        String data = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        Map<String, String> HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HttpUtil.httpPost(API_URL + "/user/login", HEADERS, data, response -> {
            assert response != null;
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                if (response.containsHeader("Set-Cookie")) {
                    Header[] headers = response.getHeaders("Set-Cookie");
                    for (Header header : headers) {
                        if (header.getValue().contains("playerinfo-Token")) {
                            String[] cookies = header.getValue().split(";");
                            for (String s : cookies) {
                                if (s.contains("playerinfo-Token=")) {
                                    PlayerInfo.TOKEN = s.split("playerinfo-Token=")[1];

                                    try {
                                        PlayerInfo.instance.setConfigValue("token", PlayerInfo.TOKEN);
                                        PlayerInfo.instance.startSendingOnline();
                                    } catch(IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static void sendOnline() {
        String data = String.format(
                "{\"minecraft_uuid\": \"%s\"}",
                Minecraft.getSessionInfo().get("X-Minecraft-UUID")
        );

        if (DiscordRichPresenceMod.START_TIME != -1 && !DiscordRichPresenceMod.serverIp.isEmpty()) {
            JSONObject obj = new JSONObject()
                    .put("minecraft_uuid", Minecraft.getSessionInfo().get("X-Minecraft-UUID"))
                    .put("start_time", DiscordRichPresenceMod.START_TIME)
                    .put("server_ip", DiscordRichPresenceMod.serverIp)
                    .put("mod_version", PlayerInfo.VERSION);

            data = obj.toString();
        }



        Map<String, String> HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HttpUtil.httpPut(API_URL + "/user/online", HEADERS, data, response -> {
            assert response != null;
            int status = response.getStatusLine().getStatusCode();
            System.out.println("TOKEN ONLINE STATUS CODE: " + status);
            if (status >= 200 && status < 300) {
                System.out.println("SUCCESSFULLY SENT ONLINE!");
            }
        });
    }

    public static void testToken() {
        if (PlayerInfo.TOKEN == null || PlayerInfo.TOKEN.isEmpty()) return;
        Map<String, String> HEADERS = new HashMap<>();
        HEADERS.put("Authorization", "Bearer " + PlayerInfo.TOKEN);
        HttpUtil.httpGet(API_URL + "/user/myUUID", HEADERS, response -> {
            assert response != null;
            int status = response.getStatusLine().getStatusCode();
            if (status == 401 || status == 403) {
                PlayerInfo.TOKEN = null;
                PlayerInfo.instance.setConfigValue("token", "");
            }
        });
    }
}
