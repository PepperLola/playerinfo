package com.palight.playerinfo.macro.impl.argument;

import com.palight.playerinfo.macro.CommandArgument;

public class DoubleArgument extends CommandArgument<Double> {

    public DoubleArgument(String name) {
        super(name);
    }

    @Override
    public String setValue(String arg) {
        this.value = Double.valueOf(arg);

        return null;
    }
}
