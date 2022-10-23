package com.palight.playerinfo.math.parsing;

import com.palight.playerinfo.math.MathOperator;

import java.util.Queue;

public abstract class ExpressionNode {

    public static String operators = "(?<=[0-9.])[+-/\\^*]";
//    public static String numbers = "((?<![0-9.])[-][0-9.]+)|[0-9.]+";
//    public static String functions = "((log|sin|cos|tan|cot|sec|csc|asin|acos|atan|sqrt)\\(.+\\))";

    public static double evaluateExpression(String expression) throws InvalidExpressionException, ImaginaryNumberException {
        return parseExpression(expression).evaluate();
    }

    private static boolean isValue(String exp) {
        try {
            Double.parseDouble(exp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int findNextOperator(String s, int startIndex) {
        for (int i = startIndex; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    return i;
            }
        }

        return -1;
    }

    private static int findClosingParenthesis(String exp, int startIndex) {
        int counter = 0;
        for (int i = startIndex; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == ')' && counter == 0) return i;
            else if (c == '(') counter ++;
            else if (c == ')') counter --;
        }

        return -1;
    }

//    private static ExpressionNode parseEquation(String exp) throws InvalidExpressionException, ImaginaryNumberException {
//        System.out.println("PARSING EXPRESSION " + exp);
//        exp = exp.replace(" ", "");
//
//        exp = parseFunctions(exp);
//
//        String[] numberArray = ArrayUtils.removeElement(exp.split(operators), "");
//        String[] operatorArray = ArrayUtils.removeElement(exp.replaceAll(numbers, "").split(""), "");
//
//        Queue<String> nums = new LinkedList<>(Arrays.asList(numberArray));
//        Queue<String> ops = new LinkedList<>(Arrays.asList(operatorArray));
//
//        ExpressionNode node = createExpressionTree(null, nums, ops);
//
//        System.out.println(node);
//        return node;
//    }

    private static ExpressionNode parseExpression(String exp) {
        if (isValue(exp)) return new ValueNode(exp);

        char first = exp.charAt(0);
        if (first >= '0' && first <= '9') {
            int operatorIdx = findNextOperator(exp, 0);
            ValueNode value = new ValueNode(exp.substring(0, operatorIdx));
            MathOperator operator = MathOperator.fromString("" + exp.charAt(operatorIdx));
            ExpressionNode right = ExpressionNode.parseExpression(exp.substring(operatorIdx + 1));

            return new OperatorNode(value, right, operator);
        }
        if (first == '(') {
            int nextCloseIdx = findClosingParenthesis(exp, 1);
            ExpressionNode parenExpression = ExpressionNode.parseExpression(exp.substring(1, nextCloseIdx));
            if (exp.length() <= nextCloseIdx + 1) return parenExpression;

            int nextOperatorIdx = findNextOperator(exp, nextCloseIdx);
            MathOperator operator = MathOperator.fromString("" + exp.charAt(nextOperatorIdx));
            ExpressionNode right = ExpressionNode.parseExpression(exp.substring(nextOperatorIdx + 1));

            return new OperatorNode(parenExpression, right, operator);
        }
        // otherwise is function
        int firstParenIdx = exp.indexOf('(');
        String functionName = exp.substring(0, firstParenIdx);
        int closingParenIdx = findClosingParenthesis(exp, firstParenIdx + 1);
        ExpressionNode functionContents = ExpressionNode.parseExpression(exp.substring(firstParenIdx + 1, closingParenIdx));
        FunctionNode functionNode = new FunctionNode(functionName, functionContents);

        if (exp.length() <= closingParenIdx + 1) return functionNode;

        int nextOperatorIdx = findNextOperator(exp, closingParenIdx);
        MathOperator operator = MathOperator.fromString("" + exp.charAt(nextOperatorIdx));
        ExpressionNode right = ExpressionNode.parseExpression(exp.substring(nextOperatorIdx + 1));

        return new OperatorNode(functionNode, right, operator);
    }

//    private static String parseFunctions(String exp) throws ImaginaryNumberException, InvalidExpressionException {
//        List<String> matches = new ArrayList<>();
//        Matcher m = Pattern.compile(functions).matcher(exp);
//        while (m.find()) {
//            matches.add(m.group());
//        }
//        for (String match : matches) {
//            String function = match.replaceAll("[^a-zA-Z]", "");
//            String argString = match.replaceAll("[^0-9,.\\-]+", "");
//            String[] args = argString.split(",");
//            String result = String.valueOf(parseFunction(function, args));
//            exp = exp.replace(match, result);
//        }
//        return exp;
//    }

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

        System.out.println("OPERATOR: " + operator + " | NEXT OPERATOR: " + nextOperator);

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

    protected abstract double evaluate() throws ImaginaryNumberException;

    public static void main(String[] args) throws InvalidExpressionException, ImaginaryNumberException {
        System.out.println(evaluateExpression("sqrt(2+2)"));
    }
}
