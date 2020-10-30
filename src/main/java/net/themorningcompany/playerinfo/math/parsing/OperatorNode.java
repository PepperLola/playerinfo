package net.themorningcompany.playerinfo.math.parsing;


import net.themorningcompany.playerinfo.math.MathOperator;

public class OperatorNode extends ExpressionNode {
    private ExpressionNode left;
    private ExpressionNode right;
    private MathOperator operator;

    public OperatorNode(ExpressionNode left, ExpressionNode right, MathOperator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }

    public MathOperator getOperator() {
        return operator;
    }

    public void setOperator(MathOperator operator) {
        this.operator = operator;
    }

    public String toString() {
        return "{ " + left.toString() + ", op=" + operator.name() + ", " + right.toString() + " }";
    }

    @Override
    public double evaluate() {
        double leftValue = left.evaluate();
        double rightValue = right.evaluate();

        switch (operator) {
            case ADD:
                return leftValue + rightValue;
            case SUBTRACT:
                return leftValue - rightValue;
            case MULTIPLY:
                return leftValue * rightValue;
            case DIVIDE:
                return leftValue / rightValue;
            case POWER:
                return Math.pow(leftValue, rightValue);
        }

        return 0;
    }
}
