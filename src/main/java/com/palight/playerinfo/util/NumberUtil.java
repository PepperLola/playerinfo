package com.palight.playerinfo.util;

import org.apache.commons.lang3.ArrayUtils;

public class NumberUtil {
    public static boolean isBetween(int num, int low, int high) {
        return low <= num && num <= high;
    }

    public static boolean isBetween(double num, double low, double high) {
        return low <= num && num <= high;
    }

    public static boolean pointIsBetween(int x, int y, int lowX, int lowY, int highX, int highY) {
        return isBetween(x, lowX, highX) && isBetween(y, lowY, highY);
    }

    public static int roundDown(int in, int max) {
        return Math.min(in, max);
    }

    public static String[] prependElement(String[] array, String element) {
        array = ArrayUtils.remove(array, ArrayUtils.indexOf(array, element));
        return ArrayUtils.add(array, 0, element);
    }

    public static int[] prependElement(int[] array, int element) {
        array = ArrayUtils.remove(array, ArrayUtils.indexOf(array, element));
        return ArrayUtils.add(array, 0, element);
    }
}
