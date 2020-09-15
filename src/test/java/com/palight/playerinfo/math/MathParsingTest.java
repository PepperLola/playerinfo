package com.palight.playerinfo.math;

import com.palight.playerinfo.math.parsing.ImaginaryNumberException;
import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import com.palight.playerinfo.util.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathParsingTest {
    @Test
    public void additionTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("10+5"), 10+5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("100+5"), 100+5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("279+120"), 279+120);
    }

    @Test
    public void subtractionTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("10-5"), 10-5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("100-5"), 100-5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("279-120"), 279-120);
    }

    @Test
    public void multiplicationTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("10*5"), 10*5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("100*5"), 100*5);
        Assertions.assertEquals(NumberUtil.evaluateExpression("279*120"), 279*120);
    }

    @Test
    public void divisionTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("10/5"), 10/5.0);
        Assertions.assertEquals(NumberUtil.evaluateExpression("100/5"), 100/5.0);
        Assertions.assertEquals(NumberUtil.evaluateExpression("279/120"), 279/120.0);
    }

    @Test
    public void powerTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("10^5"), Math.pow(10, 5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("100^5"), Math.pow(100, 5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("279^120"), Math.pow(279, 120));
    }

    @Test
    public void functionTest() throws InvalidExpressionException, ImaginaryNumberException {
        Assertions.assertEquals(NumberUtil.evaluateExpression("log(5,2)"), Math.log(5) / Math.log(2));
        Assertions.assertEquals(NumberUtil.evaluateExpression("sin(5)"), Math.sin(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("cos(5)"), Math.cos(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("tan(5)"), Math.tan(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("cot(5)"), 1 / Math.tan(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("sec(5)"), 1 / Math.cos(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("csc(5)"), 1 / Math.sin(5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("asin(0.5)"), Math.asin(0.5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("acos(0.5)"), Math.acos(0.5));
        Assertions.assertEquals(NumberUtil.evaluateExpression("atan(0.5)"), Math.atan(0.5));
    }
}
