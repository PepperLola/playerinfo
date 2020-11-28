package com.palight.playerinfo.macro.impl.argument;

import com.palight.playerinfo.macro.CommandArgument;

public class IntArgument extends CommandArgument<Integer> {

    public IntArgument(String name) {
        super(name);
    }

    @Override
    public String setValue(String value) {
        this.value = Integer.valueOf(value);

        return null;
    }
}
