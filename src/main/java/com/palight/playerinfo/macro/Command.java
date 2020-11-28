package com.palight.playerinfo.macro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {
    private List<CommandArgument<?>> arguments;
    protected Map<String, String> replacements = new HashMap<>();

    public void setArguments(List<CommandArgument<?>> arguments) {
        this.arguments = arguments;
    }

    public abstract void run();

    public List<CommandArgument<?>> getArguments() {
        return arguments;
    }

    public Map<String, String> getReplacements() {
        return replacements;
    }

    public void setReplacements(Map<String, String> replacements) {
        this.replacements = replacements;
    }
}
