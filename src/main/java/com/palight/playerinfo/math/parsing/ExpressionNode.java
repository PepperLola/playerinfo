package com.palight.playerinfo.math.parsing;

import com.palight.playerinfo.math.MathOperator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ExpressionNode {

    public static String operators = "(?<=[0-9.])[^0-9.\\n\\r]";
    public static String numbers = "((?<![0-9.])[-][0-9.]+)|[0-9.]+";
    public static String functions = "((log|sin|cos|tan|cot|sec|csc|asin|acos|atan)\\([0-9.,]+\\))";

    public static double evaluateExpression(String expression) throws InvalidExpressionException, ImaginaryNumberException {
        return parseExpression(expression).evaluate();
    }

    private static ExpressionNode parseExpression(String exp) throws InvalidExpressionException, ImaginaryNumberException {
        exp = exp.replace(" ", "");

        exp = parseFunctions(exp);

        String[] numberArray = ArrayUtils.removeElement(exp.split(operators), "");
        String[] operatorArray = ArrayUtils.removeElement(exp.replaceAll(numbers, "").split(""), "");

        Queue<String> nums = new LinkedList<String>(Arrays.asList(numberArray));
        Queue<String> ops = new LinkedList<String>(Arrays.asList(operatorArray));

        ExpressionNode node = createExpressionTree(null, nums, ops);

        System.out.println(node.toString());
        return node;
    }

    private static String parseFunctions(String exp) throws ImaginaryNumberException {
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile(functions).matcher(exp);
        while (m.find()) {
            matches.add(m.group());
        }
        for (String match : matches) {
            String function = match.replaceAll("[^a-zA-Z]", "");
            String argString = match.replaceAll("[^0-9,.\\-]+", "");
            String[] args = argString.split(",");
            String result = String.valueOf(parseFunction(function, args));
            exp = exp.replace(match, result);
        }
        return exp;
    }

    private static double parseFunction(String function, String[] args) throws ImaginaryNumberException {
        double value = Double.parseDouble(args[0]);
        double result = 0;
        switch (function) {
            case "log":
                double base = Double.parseDouble(args[1]);
                result =  Math.log(value) / Math.log(base);
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
        }
        if (Double.isNaN(result)) {
            throw new ImaginaryNumberException();
        }
        return result;
    }

    private static ExpressionNode createExpressionTree(ExpressionNode leftNode, Queue<String> nums, Queue<String> ops) throws InvalidExpressionException {
        if (ops.size() == 0 && nums.size() == 1) {
            return new ValueNode(nums.remove());
        } else if (ops.size() == 0) {
            throw new InvalidExpressionException();
        }

        MathOperator operator = MathOperator.fromString(ops.remove());

        if (leftNode == null) {
            leftNode = new ValueNode(nums.remove());
        }

        ExpressionNode rightNode = null;

        while (ops.size() > 0 && (operator == MathOperator.OPEN_PAREN || operator == MathOperator.CLOSE_PAREN)) {
            operator = MathOperator.fromString(ops.remove());
        }

        if (ops.size() == 0) {
            if (nums.size() > 0) {
                rightNode = new ValueNode(nums.remove());
                return new OperatorNode(leftNode, rightNode, operator);
            } else if (leftNode != null) {
                return leftNode;
            } else {
                throw new InvalidExpressionException();
            }
        }

        MathOperator nextOperator = MathOperator.fromString(ops.peek());

        if (operator.getPrecedence() > nextOperator.getPrecedence()) {
            if (nums == null || nums.size() <= 0) {
                throw new InvalidExpressionException();
            }
            rightNode = new ValueNode(nums.remove());
            OperatorNode operatorNode = new OperatorNode(leftNode, rightNode, operator);
            return createExpressionTree(operatorNode, nums, ops);
        }

        rightNode = createExpressionTree(null, nums, ops);

        OperatorNode operatorNode = new OperatorNode(leftNode, rightNode, operator);

        return operatorNode;
    }

    protected abstract double evaluate();

    public static void main(String[] args) throws InvalidExpressionException, ImaginaryNumberException {
        System.out.println(evaluateExpression("36^(1/2)"));
    }
}
