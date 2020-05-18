package com.palight.playerinfo.gui.screens;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.palight.playerinfo.commands.InfoCommand;
import com.palight.playerinfo.data.PlayerProperties;
import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class InfoGui extends GuiScreen {
    private int xSize = 176;
    private int ySize = 166;

    private GuiTextField text;
    private GuiButton submit;

    private PlayerProperties playerProperties;

    @Override
    public void initGui() {
        this.text = new GuiTextField(0, this.fontRendererObj, (this.width - xSize) / 2 + 1, (this.height - ySize) / 2 - 20, xSize - 49, 18);
        text.setMaxStringLength(23);
        this.text.setFocused(true);

        this.submit = new GuiButton(1, (this.width + xSize) / 2 - 46, (this.height - ySize) / 2 - 21, 46, 20, "Submit");
        this.buttonList.add(this.submit);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == 1) {
            String uuid = InfoCommand.getPlayerUUID(this.text.getText());
            PlayerProperties playerProfile = InfoCommand.mojangApiGetProfile(uuid);
            if (playerProfile == null) return;
            playerProperties = playerProfile;
        }
    }

    @Override
    protected void keyTyped(char p1, int p2) throws IOException {
        super.keyTyped(p1, p2);
        this.text.textboxKeyTyped(p1, p2);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.text.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/infogui.png"));
        drawTexturedModalRect((this.width - xSize) / 2, (this.height - ySize) / 2, 0, 0, xSize, ySize);
        this.text.drawTextBox();
        if (playerProperties != null) {
            int text_x = (this.width - xSize) / 2 + 8;
            int text_y = (this.height - ySize) / 2 + 8;
            fontRendererObj.drawString(playerProperties.getProfileName(), text_x, text_y, 1);
            fontRendererObj.drawString(playerProperties.getProfileId(), text_x, text_y + 8, 0);

            RenderPlayer renderPlayer = new RenderPlayer(Minecraft.getMinecraft().getRenderManager(), playerProperties.getSkinModel().equals("slim"));

//            System.out.println(playerProperties.getSkinUrl());

            try {
                URL skinUrl = new URL("http://textures.minecraft.net/texture/4b767ddc78a14c5f2cf2651cf4638400f5653d299acfac00c306392fe3acf9e2");

                RenderUtil.getEncodedSkin(playerProperties.getProfileId(), skinUrl.toString());
//                DynamicTexture texture = new DynamicTexture(ImageIO.read(skinUrl));
//                GlStateManager.bindTexture(texture.getGlTextureId());
//                renderPlayer.bindTexture(Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation("skin", texture));
//
//                final HashMap skinMap = Maps.newHashMap();
//
//                GameProfile playerGameProfile = new GameProfile(UUID.fromString(playerProperties.getProfileId()), playerProperties.getProfileName());
//                skinMap.putAll(Minecraft.getMinecraft().getSessionService().getTextures(playerGameProfile, false));

//                renderPlayer.bindTexture(skinLocation);
                RenderUtil.renderPlayerModel(renderPlayer.getMainModel(), 1, (this.width) / 2, (this.height) / 2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        this.text.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
