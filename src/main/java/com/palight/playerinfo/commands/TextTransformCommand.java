package com.palight.playerinfo.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TextTransformCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "texttransform";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/texttransform <transformation> <text>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ttransform", "tt");
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        if (strings.length < 2) return;
        String[] transformationNames = strings[0].split(";");
        String toSend = Arrays.stream(strings).skip(1).collect(Collectors.joining(" "));
        for (String name : transformationNames) {
            Transformation t = Transformation.getFromName(name);
            if (t == null) continue;
            toSend = t.transformText(toSend);
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage(toSend);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return iCommandSender.equals(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        if (strings.length == 0) {
            // return lower case list of Transformation names
            return Arrays.stream(Transformation.values()).map((Transformation t) -> t.name().toLowerCase()).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    interface Transform {
        String run(String s);
    }

    enum Transformation {
        UPPER(String::toUpperCase),
        LOWER(String::toLowerCase),
        VARIED((String text) -> {
            AtomicInteger ix = new AtomicInteger();
            return Arrays.stream(text.split("")).map((String s) -> !s.isEmpty() && !s.equals(" ") ? ix.getAndIncrement() % 2 == 0 ? s.toLowerCase() : s.toUpperCase() : s).collect(Collectors.joining());
        }),
        WIDE((String text) -> {
            return String.join(" ", text.split(""));
        });

        private final Transform transform;

        Transformation(Transform transform) {
            this.transform = transform;
        }

        public static Transformation getFromName(String name) {
            for (Transformation t : Transformation.values()) {
                if (t.name().equalsIgnoreCase(name)) return t;
            }

            return null;
        }

        public String transformText(String text) {
            return transform.run(text);
        }
    }
}
