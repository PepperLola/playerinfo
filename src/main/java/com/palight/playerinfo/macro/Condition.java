package com.palight.playerinfo.macro;

import com.palight.playerinfo.macro.impl.condition.MatchesCondition;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public abstract class Condition {

    private List<CommandArgument<?>> arguments;

    public static Condition parse(String conditionString) {
        Condition condition = null;
        conditionString = conditionString.trim();
        String conditionName = conditionString.split("(\\()")[0].trim();

        String argumentsString = StringUtils.substringBetween(conditionString, "(", ")").replaceAll("\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)", "");
        String[] arguments = argumentsString.split(",");

        if (conditionName.equalsIgnoreCase("matches")) {
            condition = new MatchesCondition();
        }

        if (condition != null) {
            for (int i = 0; i < condition.getArguments().size(); i++) {
                String argument = arguments[i];
                CommandArgument<?> conditionArgument = condition.getArguments().get(i);
                String err = conditionArgument.setValue(argument);

                if (err != null) {
                    System.err.println(err);
                }
            }
        }

        return condition;
    }

    public abstract boolean evaluate(Map<String, String> replacements);

    public List<CommandArgument<?>> getArguments() {
        return arguments;
    }

    public void setArguments(List<CommandArgument<?>> arguments) {
        this.arguments = arguments;
    }
}
