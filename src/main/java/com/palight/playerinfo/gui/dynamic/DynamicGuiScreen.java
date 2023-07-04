package com.palight.playerinfo.gui.dynamic;

import com.palight.playerinfo.gui.dynamic.components.*;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.math.Vector2;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DynamicGuiScreen extends GuiScreen {
    protected Vector2<Integer> size = new Vector2<>(512, 472);
    public Vector2<Integer> topLeft = new Vector2<>(0, 0);

    private static int TITLE_WIDTH = 76;
    private static int TITLE_HEIGHT = 25;
    private static int TITLE_SLANT_X = 16;

    protected static int STANDARD_PADDING = 4;

    protected GuiStack stack;

    private List<DynamicGuiComponent> components = new ArrayList<>();
    private GuiLabel titleLabel;

    private String langKey;

    public DynamicGuiScreen(String langKey) {
        this.langKey = langKey;
    }

    public DynamicGuiScreen(String langKey, Vector2<Integer> size) {
        this(langKey);
        this.size = size;
    }

    public abstract void setup();

    protected void renderBackground() {
        this.drawGradientRect(topLeft.x, topLeft.y, topLeft.x + size.x, topLeft.y + size.y, DefaultUIConfig.DEFAULT_BACKGROUND_COLOR, DefaultUIConfig.DEFAULT_BACKGROUND_COLOR);
    }

    protected void renderTitle() {
//        GlStateManager.pushMatrix();
//        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        GlStateManager.shadeModel(7425);
//        Tessellator tessellator = Tessellator.getInstance();
//        WorldRenderer wr = tessellator.getWorldRenderer();
//        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
//
//        float r, g, b;
//        r = g = b = 0.5f;
//        float a = 1.0f;
//
//        wr.pos(topLeft.x + TITLE_WIDTH / 2f + TITLE_SLANT_X, topLeft.y - TITLE_HEIGHT / 2f, this.zLevel).color(r, g, b, a).endVertex();
//        wr.pos(topLeft.x - TITLE_WIDTH / 2f, topLeft.y - TITLE_HEIGHT / 2f, this.zLevel).color(r, g, b, a).endVertex();
//        wr.pos(topLeft.x - TITLE_WIDTH / 2f - TITLE_SLANT_X, topLeft.y + TITLE_HEIGHT / 2f, this.zLevel).color(r, g, b, a).endVertex();
//        wr.pos(topLeft.x + TITLE_WIDTH / 2f, topLeft.y + TITLE_HEIGHT / 2f, this.zLevel).color(r, g, b, a).endVertex();
//        tessellator.draw();
//        GlStateManager.shadeModel(7424);
//        GlStateManager.disableBlend();
//        GlStateManager.enableAlpha();
//        GlStateManager.enableTexture2D();
//        GlStateManager.popMatrix();
    }

    @Override
    public void initGui() {
        super.initGui();
        topLeft.x = (this.width - size.x) / 2;
        topLeft.y = (this.height - size.y) / 2;

        stack = this.createStack(STANDARD_PADDING, 16, STANDARD_PADDING);
        this.titleLabel = this.createLabel(langKey, STANDARD_PADDING, STANDARD_PADDING)
//                .<GuiLabel>setFontRenderer(PlayerInfo.instance.titleRendererObj)
                .setAlignment(UnicodeFontRenderer.Alignment.LEFT)
                .setBaseline(UnicodeFontRenderer.Baseline.MIDDLE);

        addComponents(this.titleLabel, this.stack);
        setup();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    /**
     * On mouse clicked
     * @param mouseX mouse X in screen coords; 0 is left side of screen
     * @param mouseY mouse Y in screen coords; 0 is top side of screen
     * @param btn mouse button pressed
     * @throws IOException
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);

        for (DynamicGuiComponent component : components) {
            if (component.mouseIsOver(mouseX, mouseY)) {
                component.onClick(mouseX - topLeft.x, mouseY - topLeft.y, btn);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderBackground();
        this.renderTitle();

        this.components.forEach(component -> {
            component.setHovered(component.mouseIsOver(mouseX, mouseY));
            component.render(mouseX, mouseY, partialTicks);
        });
    }

    public void addComponent(DynamicGuiComponent component) {
        this.components.add(component);
    }

    public void addComponents(List<DynamicGuiComponent> components) {
        this.components.addAll(components);
    }

    public void addComponents(DynamicGuiComponent... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public GuiStack createStack(int x, int y, int spacing) {
        GuiStack stack = new GuiStack(this, x + topLeft.x, y + topLeft.y).setSpacing(spacing);
        addComponent(stack);
        return stack;
    }

    public GuiSpacer createSpacer(int x, int y, int width, int height) {
        GuiSpacer spacer = new GuiSpacer(this, x + topLeft.x, y + topLeft.y, width, height);
        addComponent(spacer);
        return spacer;
    }

    public GuiLabel createLabel(String text, int x, int y) {
        GuiLabel label = new GuiLabel(this, x + topLeft.x, y + topLeft.y).setText(text);
        addComponent(label);
        return label;
    }

    public GuiButton createButton(String text, int x, int y, int width, int height) {
        GuiButton button = new GuiButton(this, x + topLeft.x, y + topLeft.y, width, height).setText(text);
        addComponent(button);
        return button;
    }

    public GuiCheckbox createCheckbox(String label, int x, int y, int width, int height, boolean checked) {
        GuiCheckbox checkbox = new GuiCheckbox(this, x + topLeft.x, y + topLeft.y, width, height).setLabel(label).setEnabled(checked);
        addComponent(checkbox);
        return checkbox;
    }

    public GuiSlider createSlider(int x, int y, int width, int height, int min, int max, int step, String prefix, String suffix) {
        GuiSlider slider = new GuiSlider(this, x + topLeft.x, y + topLeft.y, width, height, min, max, step, prefix, suffix);
        addComponent(slider);
        return slider;
    }
}
