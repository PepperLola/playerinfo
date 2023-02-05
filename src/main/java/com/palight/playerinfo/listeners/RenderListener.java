package com.palight.playerinfo.listeners;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.impl.DiscordLinkGui;
import com.palight.playerinfo.gui.screens.impl.LoginGui;
import com.palight.playerinfo.util.ApiUtil;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.awt.*;

public class RenderListener {

    public static final int LOGIN_BUTTON_ID = PlayerInfo.MODID.hashCode();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiOptions) {
            GuiScreen gui = event.gui;

            int buttonSpacing = 4;

            int buttonWidth = 64;
            int buttonHeight = 20;

            int buttonX = gui.width - buttonWidth - buttonSpacing;
            int buttonY = gui.height - buttonHeight - buttonSpacing;

            while (true) {
                if (buttonX > gui.width) {
                    break;
                }

                int increment = 0;

                Rectangle btn = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
                boolean intersects = false;

                for (int i = 0; i < event.buttonList.size(); i++) {
                    GuiButton currentButton = event.buttonList.get(i);
                    if (!intersects) {
                        intersects = btn.intersects(new Rectangle(currentButton.xPosition, currentButton.yPosition, currentButton.width, currentButton.height));
                    }
                    if (intersects) {
                        increment = currentButton.xPosition + currentButton.width + buttonSpacing;
                    }
                }

                if (!intersects) {
                    break;
                }

                buttonX = increment;
            }

            GuiButton loginGuiButton = new GuiButton(LOGIN_BUTTON_ID, buttonX, buttonY, buttonWidth, buttonHeight, PlayerInfo.TOKEN.isEmpty() ? "Login" : "Link Discord");
            event.buttonList.add(loginGuiButton);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onButtonPressPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiOptions && event.button.id == LOGIN_BUTTON_ID) {
            if (PlayerInfo.TOKEN.isEmpty()) {
                Minecraft.getMinecraft().displayGuiScreen(new LoginGui());
            } else {
                HttpUtil.httpGet(ApiUtil.API_URL + "/user/generateDiscordToken", ApiUtil.generateHeaders(), (HttpResponse response) -> {
                    String entity = EntityUtils.toString(response.getEntity());
                    JsonParser parser = new JsonParser();
                    JsonElement obj = parser.parse(entity);

                    String token = obj.getAsJsonObject().get("token").getAsString();

                    Minecraft.getMinecraft().displayGuiScreen(new DiscordLinkGui(token));
                });
            }
            event.setCanceled(true);
        }
    }

//    @SubscribeEvent
//    public void playerJoinEvent(EntityJoinWorldEvent event) {
//        if (!(event.entity instanceof EntityPlayer)) return;
//        EntityPlayer player = ((EntityPlayer) event.entity);
//    }
}
