package com.palight.playerinfo.macro.impl.command;

import com.palight.playerinfo.macro.Command;
import com.palight.playerinfo.macro.impl.argument.StringArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SetCommand extends Command {
    private static final String name = "set";
    private final StringArgument variableName = new StringArgument("variableName");
    private final StringArgument variableValue = new StringArgument("variableValue");

    public SetCommand() {
        super();
        // define arguments
        this.setArguments(new ArrayList<>(Arrays.asList(
                variableName,
                variableValue
        )));
    }

    @Override
    public void run(Map<String, String> replacements) {
        String variableNameString = variableName.getValue();
        String variableValueString = variableValue.getValue();
        if (replacements != null) {
            for (String key : replacements.keySet()) {
                variableNameString = variableNameString.replaceAll(String.format("<%s>", key), replacements.get(key));
                variableValueString = variableValueString.replaceAll(String.format("<%s>", key), replacements.get(key));
            }
        }
        assert replacements != null;
        replacements.put(variableNameString, variableValueString);
    }
}
