package com.palight.playerinfo.math.parsing;

import com.palight.playerinfo.math.MathOperator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public abstract class ExpressionNode {

    public static String operators = "([^0-9.])+";
    public static String numbers = "[0-9.]+";

    public static double evaluateExpression(String expression) throws InvalidExpressionException {
        return parseExpression(expression).evaluate();
    }

    private static ExpressionNode parseExpression(String exp) throws InvalidExpressionException {
        exp = exp.replace(" ", "");

        String[] numberArray = ArrayUtils.removeElement(exp.split(operators), "");
        String[] operatorArray = ArrayUtils.removeElement(exp.replaceAll(numbers, "").split(""), "");

        Queue<String> nums = new LinkedList<String>(Arrays.asList(numberArray));
        Queue<String> ops = new LinkedList<String>(Arrays.asList(operatorArray));

        ExpressionNode node = createExpressionTree(null, nums, ops);

        System.out.println(node.toString());
        return node;
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
            if (nums.size() <= 0) {
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

    public static void main(String[] args) throws InvalidExpressionException {
        System.out.println(evaluateExpression("36^(1/2)"));
    }
}
