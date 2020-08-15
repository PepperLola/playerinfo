package com.palight.playerinfo.math;

import static com.palight.playerinfo.util.random.RandomUtil.randomRange;

public enum MathOperator {
    ADD (MathProblemType.ARITHMETIC, 0, "+"),
    SUBTRACT (MathProblemType.ARITHMETIC, 0, "-"),
    MULTIPLY (MathProblemType.ARITHMETIC, 1, "*"),
    DIVIDE (MathProblemType.ARITHMETIC, 1, "/"),
    POWER (MathProblemType.ARITHMETIC, 3, "^"),
    OPEN_PAREN (MathProblemType.SYMBOLS, Integer.MAX_VALUE, "("),
    CLOSE_PAREN (MathProblemType.SYMBOLS, Integer.MIN_VALUE, ")");

    private MathProblemType problemType;
    private int precedence;
    private String displayString;

    MathOperator(MathProblemType problemType, int precedence, String displayString) {
        this.problemType = problemType;
        this.precedence = precedence;
        this.displayString = displayString;
    }

    public boolean isProblemType(MathProblemType problemType) {
        return this.problemType == problemType;
    }

    public static MathOperator fromString(String operator) {
        for (MathOperator op : MathOperator.values()) {
            if (op.displayString.equals(operator)) {
                return op;
            }
        }

        return null;
    }

    public static String toString(MathOperator operator) {
        return operator.displayString;
    }

    public static MathOperator randomOperator(MathProblemType problemType) {
        MathOperator returnOperator = null;
        boolean isValid = false;
        while (!isValid) {
            returnOperator = MathOperator.values()[randomRange(0, MathOperator.values().length - 1)];
            isValid = returnOperator.isProblemType(problemType);
        }

        return returnOperator;
    }

    public int getPrecedence() {
        return precedence;
    }
}
