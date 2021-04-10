package com.palight.playerinfo.util;

import com.palight.playerinfo.math.parsing.ExpressionNode;
import com.palight.playerinfo.math.parsing.ImaginaryNumberException;
import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.TreeMap;

public class NumberUtil {

    private static final TreeMap<Integer, String> treemap = new TreeMap<>();
    static {
        treemap.put(1000, "M");
        treemap.put(900, "CM");
        treemap.put(500, "D");
        treemap.put(400, "CD");
        treemap.put(100, "C");
        treemap.put(90, "XC");
        treemap.put(50, "L");
        treemap.put(40, "XL");
        treemap.put(10, "X");
        treemap.put(9, "IX");
        treemap.put(5, "V");
        treemap.put(4, "IV");
        treemap.put(1, "I");

    }

    public static String integerToRoman(int number) {
        if (number <= 0) return "";
        int l = treemap.floorKey(number);
        if (number == l) {
            return treemap.get(number);
        }
        return treemap.get(l) + integerToRoman(number - l);
    }

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

    public static double evaluateExpression(String exp) throws InvalidExpressionException, ImaginaryNumberException {
        return ExpressionNode.evaluateExpression(exp);
    }

    public static double round(double input, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(input * multiplier) / multiplier;
    }

    public static int clamp(int n, int low, int high) {
        return n < low ? low : Math.min(n, high);
    }
}
