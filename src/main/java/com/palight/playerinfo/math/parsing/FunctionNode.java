package com.palight.playerinfo.math.parsing;

public class FunctionNode extends ExpressionNode {

    private String name;
    private ExpressionNode expression;

    public FunctionNode(String name, ExpressionNode exp) {
        this.name = name;
        this.expression = exp;
    }

    @Override
    protected double evaluate() throws ImaginaryNumberException {
        double value = expression.evaluate();
        double result = 0;
        switch (name) {
            case "log":
                // TODO add log base argument
                result =  Math.log(value);
                break;
            case "sin":
                result =  Math.sin(value);
                break;
            case "cos":
                result =  Math.cos(value);
                break;
            case "tan":
                result =  Math.tan(value);
                break;
            case "cot":
                result =  1 / Math.tan(value);
                break;
            case "atan":
                result =  Math.atan(value);
                break;
            case "sec":
                result =  1 / Math.cos(value);
                break;
            case "acos":
                result =  Math.acos(value);
                break;
            case "csc":
                result =  1 / Math.sin(value);
                break;
            case "asin":
                result =  Math.asin(value);
                break;
            case "sqrt":
                result =  Math.sqrt(value);
                break;
        }
        if (Double.isNaN(result)) {
            throw new ImaginaryNumberException();
        }
        return result;
    }
}
