package com.palight.playerinfo.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palight.playerinfo.data.PlayerProperties;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoCommand {

    private static Gson gson = new Gson();
    private static EntityPlayer player;

    private InfoCommand() {}

    public static int getInfo(ICommandSender source, String target) {
//        player = Minecraft.getMinecraft().thePlayer;
//        System.out.println("TRIGGERED COMMAND!");
//        String playerUUID = target.getUniqueID().toString().replace("-", "");
//
//        PlayerProfile playerData = mojangApiGetProfile(playerUUID);
//        if (playerData == null) return 0;
//        MCUtil.sendPlayerMessage(player, playerData.getName() + ": " + playerData.getId());
        return 1;
    }

    public static PlayerProperties mojangApiGetProfile(String uuid) {
        String responseBody = HttpUtil.httpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        if (responseBody == null) return null;
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        Map<String, Object> responseData = gson.fromJson(responseBody, new TypeToken<HashMap<String, Object>>(){}.getType());
        List<Map<String, String>> rawProperties = (ArrayList<Map<String, String>>) responseData.get("properties");

        String decodedBase64 = new String(DatatypeConverter.parseBase64Binary(rawProperties.get(0).get("value")));

        PlayerProperties playerProperties = gson.fromJson(decodedBase64, PlayerProperties.class);

        System.out.println(playerProperties.getProfileName() + ": " + playerProperties.getSkinMetadata());
        return playerProperties;
    }

    public static void executeCommand(ICommandSender source, String target) {
        getInfo(source, target);
    }

    public static String getPlayerUUID(String username) {
        String response = HttpUtil.httpGet("https://api.mojang.com/users/profiles/minecraft/" + username);
        Map<String, String> responseMap = gson.fromJson(response, new TypeToken<HashMap<String, String>>(){}.getType());
        return responseMap.get("id");
    }
}
