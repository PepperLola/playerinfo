package com.palight.playerinfo.listeners;

import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import com.palight.playerinfo.util.NumberUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientChatListener {
    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        String message = event.getOriginalMessage();

        // try calc command
        Matcher calcMatcher = Pattern.compile("(calc\\([0-9+\\-/*^]+\\))").matcher(message);
        boolean matches = calcMatcher.find();
        if (matches) {
            String function = calcMatcher.group();
            String equation = StringUtils.substringBetween(function, "calc(", ")");

            try {
                String result = new DecimalFormat("0.######").format(NumberUtil.evaluateExpression(equation));
                message = message.replace(function, result);
                event.setMessage(message);
            } catch (InvalidExpressionException e) {
                e.printStackTrace();
            }
        }

        // try toggle chaosmode command
        Matcher chaosMatcher = Pattern.compile("(chaos\\([A-Za-z]+\\))").matcher(message);
        matches = chaosMatcher.find();
        if (matches) {
            String function = chaosMatcher.group();
            String modeName = StringUtils.substringBetween(function, "chaos(", ")");

            Minecraft.getInstance().getConnection().sendPacket(new CCustomPayloadPacket(new ResourceLocation("ChaosMode"), (new PacketBuffer(Unpooled.buffer())).writeString("hello")));
        }
    }
}
