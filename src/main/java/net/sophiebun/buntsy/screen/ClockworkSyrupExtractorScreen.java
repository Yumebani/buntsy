package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.sophiebun.buntsy.BuntsyMod;

public class ClockworkSyrupExtractorScreen extends AbstractContainerScreen<ClockworkSyrupExtractorMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/clockwork_syrup_extractor_gui.png");

    public ClockworkSyrupExtractorScreen(ClockworkSyrupExtractorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 173;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 22;
        this.inventoryLabelY = 82;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderFilled(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderFilled(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 13, y + 15, 0, 174, (int)(148 * (menu.getProgress() / ((float) menu.getMaxProgress()))), 4);
    }
}
