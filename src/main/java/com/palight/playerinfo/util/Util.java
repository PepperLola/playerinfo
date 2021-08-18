package com.palight.playerinfo.util;

public class Util {
    public static String cleanStringForRegex(String string) {
        return string.replaceAll("\\)", "\\\\)").replaceAll("\\(", "\\\\(").replaceAll("\\|", "\\\\|").replaceAll("\\*", "\\\\*");
    }
}
