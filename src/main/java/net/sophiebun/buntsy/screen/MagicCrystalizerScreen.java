package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.sophiebun.buntsy.BuntsyMod;

public class MagicCrystalizerScreen extends AbstractContainerScreen<MagicCrystalizerMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/magic_crystalizer_gui.png");

    public MagicCrystalizerScreen(MagicCrystalizerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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

    private void renderActionIcon(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isEnchanted()) {
            guiGraphics.blit(TEXTURE, x + 43, y + 12, 176, 17, 29, 36);
        }
    }
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isEnchanted()) {
            guiGraphics.blit(TEXTURE, x + 99    , y + 35, 176, 0, menu.getScaledProgress(), 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
