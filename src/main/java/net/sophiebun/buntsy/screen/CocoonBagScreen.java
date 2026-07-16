package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.server.ModCocoonBagServerPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class CocoonBagScreen extends AbstractContainerScreen<CocoonBagMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/giant_cocoon_gui.png");

    private final Inventory playerInventory;

    public CocoonBagScreen(CocoonBagMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        playerInventory = pPlayerInventory;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return false;
    }

    @Override
    protected void containerTick() {
        ItemStack cocoonBagIn = playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.COCOON_BAG.get()) ?
                playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND) : playerInventory.player.getItemBySlot(EquipmentSlot.OFFHAND);

        if (cocoonBagIn.hasTag()){
            CompoundTag tag = cocoonBagIn.getTag();
            if (tag.contains("in_update") && tag.getBoolean("in_update")) {
                menu.updateNbt(cocoonBagIn.getTag().getCompound("uro_contents"));
                tag.remove("in_update");
                menu.getCocoonBag().setTag(tag);
            }
            if (tag.contains("out_update") && tag.getBoolean("out_update")) {
                ModPacketHandler.INSTANCE.sendToServer( new ModCocoonBagServerPacket(CocoonBag.getUroId(menu.getCocoonBag()),
                        menu.getCocoonBag().getTag().getCompound("uro_contents"), false, false));
                tag.remove("out_update");
                menu.getCocoonBag().setTag(tag);
            }
        }
    }

    @Override
    public void removed() {
        CompoundTag tag = menu.getCocoonBag().getTag();
        if (tag.contains("uro_contents")){
            ModPacketHandler.INSTANCE.sendToServer( new ModCocoonBagServerPacket(CocoonBag.getUroId(menu.getCocoonBag()),
                    menu.getCocoonBag().getTag().getCompound("uro_contents"), false, true));
            tag.remove("uro_contents");
            ItemStack cocoonBagIn = playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.COCOON_BAG.get()) ?
                    playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND) : playerInventory.player.getItemBySlot(EquipmentSlot.OFFHAND);
            cocoonBagIn.setTag(tag);
        }
        super.removed();
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
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
