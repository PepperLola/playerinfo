package com.palight.playerinfo.macro;

import javax.annotation.Nullable;
import java.util.*;

public class Macro {
    private String[] instructions;
    private List<Command> commands = null;

    public Macro(String[] instructions) {
        this.instructions = instructions;
    }

    public void run(@Nullable Map<String, String> replacements) {
        if (commands == null) {
            commands = parse(instructions);
        }

        commands.forEach(command -> {
            command.run(replacements);
        });
    }

    private List<Command> parse(String[] instructions) {
        try {
            List<Command> commands = new ArrayList<>();

            Queue<String> instQueue = new LinkedList<>(Arrays.asList(instructions));

            while (!instQueue.isEmpty()) {
                Command command = Command.parse(instQueue);
                commands.add(command);
            }

            return commands;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
