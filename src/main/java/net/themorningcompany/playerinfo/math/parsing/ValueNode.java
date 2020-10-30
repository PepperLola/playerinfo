package net.themorningcompany.playerinfo.math.parsing;

public class ValueNode extends ExpressionNode {
    private double value;

    public ValueNode(String value) {
        this.value = Double.valueOf(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return "{ val=" + value + " }";
    }

    @Override
    public double evaluate() {
        return value;
    }
}
