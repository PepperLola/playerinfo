package com.palight.playerinfo.macro;

import com.palight.playerinfo.macro.impl.command.MessageCommand;
import com.palight.playerinfo.macro.impl.command.SetCommand;

public class SupportedCommands {
    public static final Class[] classes = new Class[] {
            MessageCommand.class,
            SetCommand.class
    };
}
