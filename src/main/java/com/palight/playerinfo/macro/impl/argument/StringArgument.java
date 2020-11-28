package com.palight.playerinfo.macro.impl.argument;

import com.palight.playerinfo.macro.CommandArgument;

public class StringArgument extends CommandArgument<String> {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    public String setValue(String value) {
        if (value.contains("\"")) {
            this.value = value.replaceAll("\"", "");
        } else if (value.contains("'")) {
            this.value = value.replaceAll("'", "");
        }

        return null;
    }
}
