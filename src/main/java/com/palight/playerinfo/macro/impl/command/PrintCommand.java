package com.palight.playerinfo.macro.impl.command;

import com.palight.playerinfo.macro.Command;
import com.palight.playerinfo.macro.impl.argument.StringArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PrintCommand extends Command {
    private static final String name = "print";
    private final StringArgument string = new StringArgument("string");

    public PrintCommand() {
        super();
        // define arguments
        this.setArguments(new ArrayList<>(Arrays.asList(
                string
        )));
    }

    @Override
    public void run(Map<String, String> replacements) {
        String stringValue = string.getValue();
        if (replacements != null) {
            for (String key : replacements.keySet()) {
                stringValue = stringValue.replaceAll(String.format("<%s>", key), replacements.get(key));
            }
        }
        System.out.println(stringValue);
    }
}
