package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.sophiebun.buntsy.BuntsyMod;

public class MixerScreen extends AbstractContainerScreen<MixerMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/mixer_gui.png");

    public MixerScreen(MixerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 56;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderActionIcon(guiGraphics, x, y);
        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isEnchanted()) {
            guiGraphics.blit(TEXTURE, x + 88, y + 23, 176, 14, menu.getScaledProgress(), 17);
        }
    }
    private void renderActionIcon(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isEnchanted()) {
            guiGraphics.blit(TEXTURE, x + 92, y + 47, 176, 31, 14, 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
