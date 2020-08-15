package com.palight.playerinfo.commands;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.palight.playerinfo.dataClasses.PlayerProfile;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collection;

public final class InfoCommand {
    private static Gson gson = new Gson();
    private static ClientPlayerEntity player;
    private InfoCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("getinfo").requires(source -> source.hasPermissionLevel(2))
                .then(Commands.argument("target", EntityArgument.player())
                    .executes(context -> getInfo(
                            context.getSource(),
                            EntityArgument.getPlayers(context, "target")
                    ))
                ).executes(context -> {
                    System.out.println("Called getinfo with no arguments");
                    return 1;
                })
        );
    }

    private static int getInfo(CommandSource source, Collection<ServerPlayerEntity> targets) {
        player = Minecraft.getInstance().player;
        System.out.println("TRIGGERED COMMAND!");
        for (ServerPlayerEntity target : targets) {
            String playerUUID = target.getUniqueID().toString().replace("-", "");

            PlayerProfile playerData = mojangApiGetProfile(playerUUID);
            if (playerData == null) return 0;
            MCUtil.sendPlayerMessage(player, playerData.getName() + ": " + playerData.getId());
        }
        return 1;
    }

    public static PlayerProfile mojangApiGetProfile(String uuid) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);

            System.out.println("Executing request " + httpGet.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else if (status == 429) {
                    MCUtil.sendPlayerMessage(player, "Too many requests! Try again in a minute.", TextFormatting.RED);
                    return null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpGet, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            PlayerProfile playerProfile = gson.fromJson(responseBody, PlayerProfile.class);
            return playerProfile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
