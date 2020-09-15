package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.math.parsing.ImaginaryNumberException;
import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

public class GuiCalculator extends GuiCustomWidget {
    private int keypadWidth = 54;
    private int keypadHeight = 48;
    private int rowHeight = 8;
    private int keypadX;
    private int keypadY;

    private String equation = "";

    private CalculatorButton[][] keypad = {
            {CalculatorButton.U_CLS, CalculatorButton.O_POW, null, CalculatorButton.O_DIV},
            {CalculatorButton.N_7, CalculatorButton.N_8, CalculatorButton.N_9, CalculatorButton.O_MULT},
            {CalculatorButton.N_4, CalculatorButton.N_5, CalculatorButton.N_6, CalculatorButton.O_SUB},
            {CalculatorButton.N_1, CalculatorButton.N_2, CalculatorButton.N_3, CalculatorButton.O_ADD},
            {CalculatorButton.S_NEG, CalculatorButton.N_0, CalculatorButton.S_PER, CalculatorButton.O_EQ}
    };

    public GuiCalculator(int id, int xPosition, int yPosition) {
        super(id, xPosition, yPosition, 64, 64);
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);
        this.drawTexturedModalRect(xPosition, yPosition, 96, 0, width, height);
        mc.fontRendererObj.drawString(equation, xPosition + 6, yPosition + 4, 0x00000000, false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        processButtonClick(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY);
    }

    private void processButtonClick(int mouseX, int mouseY) {
        CalculatorButton clickedButton = getClickedButton(mouseX, mouseY);
        if (clickedButton == null) return;

        if (clickedButton == CalculatorButton.O_EQ) {
            try {
                double solution = NumberUtil.evaluateExpression(equation);
                equation = new DecimalFormat("0.######").format(solution);
            } catch (InvalidExpressionException | ImaginaryNumberException e) {
                e.printStackTrace();
                equation = "ERROR";
            }
        } else if (clickedButton == CalculatorButton.U_CLS) {
            equation = "";
        } else {
            equation += clickedButton.getDisplayString();
        }
    }

    private CalculatorButton getClickedButton(int mouseX, int mouseY) {
        keypadX = xPosition + 5;
        keypadY = yPosition + 14;
        if (!NumberUtil.pointIsBetween(mouseX, mouseY, keypadX, keypadY, keypadX + keypadWidth, keypadY + keypadHeight)) return null;

        int row = getRow(mouseX, mouseY);
        int col = getColumn(mouseX, mouseY);

        if (row < 0 || row >= keypad.length) return null;
        if (col < 0 || col >= keypad[0].length) return null;

        return keypad[row][col];
    }

    private int getRow(int mouseX, int mouseY) {
        if (mouseY < keypadY || mouseY > keypadY + keypadHeight) return -1;
        int button = mouseY - keypadY;
        button -= button % 10;
        button /= 10;
        return button;
    }

    private int getColumn(int mouseX, int mouseY) {
        if (mouseX < keypadX || mouseX > keypadX + keypadWidth) return -1;
        int button = (mouseX - keypadX);
        button -= button % 14;
        button /= 14;
        return button;
    }

    /**
     *  N = Number (1, 2, 3, etc.)
     *  S = Special (Negative, Period, etc.)
     *  O = Operator (Add, Subtract, etc.)
     *  U = Utility (C/CE (clear), etc.)
     * **/
    enum CalculatorButton {
        N_1("1", CalculatorButtonType.NUMBER), N_2("2", CalculatorButtonType.NUMBER), N_3("3", CalculatorButtonType.NUMBER),
        N_4("4", CalculatorButtonType.NUMBER), N_5("5", CalculatorButtonType.NUMBER), N_6("6", CalculatorButtonType.NUMBER),
        N_7("7", CalculatorButtonType.NUMBER), N_8("8", CalculatorButtonType.NUMBER), N_9("9", CalculatorButtonType.NUMBER),
        N_0("0", CalculatorButtonType.NUMBER), S_NEG("-", CalculatorButtonType.SPECIAL), S_PER(".", CalculatorButtonType.SPECIAL),
        O_ADD("+", CalculatorButtonType.OPERATOR), O_SUB("-", CalculatorButtonType.OPERATOR), O_MULT("*", CalculatorButtonType.OPERATOR), O_DIV("/", CalculatorButtonType.OPERATOR), O_POW("^", CalculatorButtonType.OPERATOR), O_EQ("=", CalculatorButtonType.OPERATOR),
        U_CLS("CE", CalculatorButtonType.UTILITY);

        private String displayString;
        private CalculatorButtonType type;

        CalculatorButton(String displayString, CalculatorButtonType type) {
            this.displayString = displayString;
            this.type = type;
        }

        public String getDisplayString() {
            return displayString;
        }

        public CalculatorButtonType getType() {
            return type;
        }
    }

    enum CalculatorButtonType {
        NUMBER,
        OPERATOR,
        SPECIAL,
        UTILITY
    }
}
