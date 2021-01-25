package com.palight.playerinfo.macro;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Command {
    private List<CommandArgument<?>> arguments;

    public void setArguments(List<CommandArgument<?>> arguments) {
        this.arguments = arguments;
    }

    public abstract void run(Map<String, String> replacements);

    public static Command parse(Queue<String> instQueue) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Command command = null;
        if (instQueue.isEmpty()) return null;
        String fullInstruction = instQueue.remove().trim();
        String commandName = fullInstruction.split("(\\()")[0].trim();

        if (!commandName.equalsIgnoreCase("if")) {
            String argumentsString = StringUtils.substringBetween(fullInstruction, "(", ")").replaceAll("\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)", "");
            String[] arguments = argumentsString.split(",");


            for (Class<?> commandClass : SupportedCommands.classes) {
                Field nameField = commandClass.getDeclaredField("name");
                nameField.setAccessible(true);
                if (nameField.get(null).equals(commandName)) {
                    command = (Command) commandClass.newInstance();
                    break;
                }
            }

            if (command != null) {
                for (int j = 0; j < command.getArguments().size(); j++) {
                    String argument = arguments[j];
                    CommandArgument<?> commandArgument = command.getArguments().get(j);
                    String err = commandArgument.setValue(argument);

                    if (err != null) {
                        System.err.println(err);
                    }
                }
            }
        } else {
            int openingParenthesisIndex = fullInstruction.indexOf("(");
            int closingParenthesisIndex = fullInstruction.lastIndexOf(")");
            if (closingParenthesisIndex == -1) {
                closingParenthesisIndex = fullInstruction.length();
            }
            String conditionString = fullInstruction.substring(openingParenthesisIndex + 1, closingParenthesisIndex);
            Condition condition = Condition.parse(conditionString);

            List<Command> ifCommands = new ArrayList<>();

            while (!instQueue.isEmpty()) {
                String nextCommand = instQueue.peek();
                if (nextCommand.equalsIgnoreCase("fi")) {
                    instQueue.remove();
                    break;
                }
                Command subCommand = Command.parse(instQueue);
                ifCommands.add(subCommand);
            }

            command = new ConditionalCommand(ifCommands, condition);
        }

        return command;
    }

    public List<CommandArgument<?>> getArguments() {
        return arguments;
    }
}
