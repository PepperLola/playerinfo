package com.palight.playerinfo.macro.impl.condition;

import com.palight.playerinfo.macro.Condition;
import com.palight.playerinfo.macro.impl.argument.StringArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchesCondition extends Condition {

    private StringArgument text = new StringArgument("text");
    private StringArgument pattern = new StringArgument("pattern");

    public MatchesCondition() {
        super();
        this.setArguments(new ArrayList<>(Arrays.asList(
                text,
                pattern
        )));
    }

    @Override
    public boolean evaluate(Map<String, String> replacements) {
        String patternText = pattern.getValue();
        String textString = text.getValue();

        if (replacements != null) {
            for (String key : replacements.keySet()) {
                patternText = patternText.replaceAll(String.format("<%s>", key), replacements.get(key));
                textString = textString.replaceAll(String.format("<%s>", key), replacements.get(key));
            }
        }

        if (patternText.equals(textString)) return true;
        if (textString.contains(patternText)) return true;

        Pattern pattern = Pattern.compile(patternText);
        Matcher matcher = pattern.matcher(textString);

        return matcher.find();
    }
}
