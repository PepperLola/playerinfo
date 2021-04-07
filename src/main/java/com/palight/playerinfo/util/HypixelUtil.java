package com.palight.playerinfo.util;

public class HypixelUtil {
    public enum Rank {
        NONE("NONE", "NONE", 11184810),
        VIP("VIP", "VIP", 5635925),
        VIP_PLUS("VIP+", "VIP_PLUS", 5635925),
        MVP("MVP", "MVP", 5636095),
        MVP_PLUS("MVP+", "MVP_PLUS", 5636095),
        SUPERSTAR("MVP++", "SUPERSTAR", 16755200);

        private final String displayName;
        private final String apiName;
        private final int color;

        Rank(String displayName, String apiName, int color) {
            this.displayName = displayName;
            this.apiName = apiName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getApiName() {
            return apiName;
        }

        public int getColor() {
            return color;
        }

        public static Rank getRankFromAPIName(String apiName) {
            for (Rank rank : Rank.values()) {
                if (rank.getApiName().equalsIgnoreCase(apiName)) {
                    return rank;
                }
            }

            return Rank.NONE;
        }
    }

    public enum PlusColor {
        RED(16733525),
        GOLD(16755200),
        GREEN(5635925),
        YELLOW(16777045),
        LIGHT_PURPLE(16733695),
        WHITE(16777215),
        BLUE(5592575),
        DARK_GREEN(43520),
        DARK_RED(11141120),
        DARK_AQUA(43690),
        DARK_PURPLE(11141290),
        DARK_GRAY(5592405),
        BLACK(0),
        DARK_BLUE(170);

        private int color;

        PlusColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public static PlusColor getPlusColorFromName(String name) {
            for (PlusColor plusColor : PlusColor.values()) {
                if (plusColor.toString().equals(name))
                    return plusColor;
            }

            return PlusColor.RED;
        }
    }

    public enum TitleColor {
        ROOKIE(11184810),
        IRON(16777215),
        GOLD(16755200),
        DIAMOND(5636095),
        MASTER(43520),
        LEGEND(11141120),
        GRANDMASTER(16777045),
        GODLIKE(11141290);

        private final int color;

        TitleColor(int color) {this.color = color;}

        public int getTitleColor () {return color;}

        public static TitleColor getTitleColorFromName(String name) {
            for (TitleColor titleColor : TitleColor.values()) {
                if (titleColor.toString().equalsIgnoreCase(name)){
                    return titleColor;
                }
            }
            return TitleColor.ROOKIE;
        }
    }
}
