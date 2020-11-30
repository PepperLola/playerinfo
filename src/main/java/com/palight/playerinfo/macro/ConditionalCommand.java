package com.palight.playerinfo.macro;

import java.util.List;
import java.util.Map;

public class ConditionalCommand extends Command {

    private List<Command> commandsToRun;
    private Condition condition;

    public ConditionalCommand(List<Command> commandsToRun, Condition condition) {
        this.commandsToRun = commandsToRun;
        this.condition = condition;
    }

    @Override
    public void run(Map<String, String> replacements) {
        if (condition.evaluate(replacements)) {
            // do stuff
            commandsToRun.forEach(command -> command.run(replacements));
        }
    }

    public List<Command> getCommandsToRun() {
        return commandsToRun;
    }

    public void setCommandsToRun(List<Command> commandsToRun) {
        this.commandsToRun = commandsToRun;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
