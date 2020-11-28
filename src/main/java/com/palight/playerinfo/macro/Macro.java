package com.palight.playerinfo.macro;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Macro {
    public static void run(String[] instructions, @Nullable Map<String, String> replacements) {
        List<Command> commands = parse(instructions, replacements);

        commands.forEach(Command::run);
    }

    public static List<Command> parse(String[] instructions, @Nullable Map<String, String> replacements) {
        try {
            List<Command> commands = new ArrayList<>();

            for (String inst : instructions) {
                String commandName = inst.split("(\\()")[0];
                String argumentsString = StringUtils.substringBetween(inst, "(", ")").replaceAll("\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)", "");
                String[] arguments = argumentsString.split(",");

                Command command = null;

                for (Class commandClass : SupportedCommands.classes) {
                    Field nameField = commandClass.getDeclaredField("name");
                    nameField.setAccessible(true);
                    if (nameField.get(null).equals(commandName)) {
                        command = (Command) commandClass.newInstance();
                        break;
                    }
                }

                if (command != null) {
                    for (int i = 0; i < command.getArguments().size(); i++) {
                        String argument = arguments[i];
                        CommandArgument commandArgument = command.getArguments().get(i);
                        String err = commandArgument.setValue(argument);

                        if (err != null) {
                            System.err.println(err);
                        }
                    }
                    command.setReplacements(replacements);
                    commands.add(command);
                }
            }

            return commands;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
