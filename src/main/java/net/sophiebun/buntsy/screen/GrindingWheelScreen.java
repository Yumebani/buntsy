package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.sophiebun.buntsy.BuntsyMod;

public class GrindingWheelScreen extends AbstractContainerScreen<GrindingWheelMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/grinding_wheel_gui.png");

    public GrindingWheelScreen(GrindingWheelMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
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
        renderFoodBar(guiGraphics, x, y);
    }

    private void renderActionIcon(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 68, y + 45, 176, 17, 18, 18);
        }
    }
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 95, y + 35, 176, 0, menu.getScaledProgress(), 17);
        }
    }
    private void renderFoodBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 36, y + 71 - menu.getScaledFood(), 176, 92 - menu.getScaledFood(), 14, menu.getScaledFood());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
