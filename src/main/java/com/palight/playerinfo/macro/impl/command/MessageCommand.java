package com.palight.playerinfo.macro.impl.command;

import com.palight.playerinfo.macro.Command;
import com.palight.playerinfo.macro.impl.argument.StringArgument;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageCommand extends Command {

    private static final String name = "message";
    private final StringArgument message = new StringArgument("message");

    public MessageCommand() {
        super();
        // define arguments
        this.setArguments(new ArrayList<>(Arrays.asList(
                message
        )));
    }

    @Override
    public void run() {
        String msg = message.getValue();
        for (String key : replacements.keySet()) {
            msg = msg.replaceAll(key, replacements.get(key));
            System.out.println(key + " | " + msg);
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage(msg);
    }
}
