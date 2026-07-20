package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.server.packets.CocoonBagServerPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

import java.util.Optional;

public class CocoonBagScreen extends AbstractContainerScreen<CocoonBagMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "textures/gui/giant_cocoon_gui.png");

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

        if (cocoonBagIn.get(ModDataComponents.URO_CONTENT).incoming()) {
            menu.updateNbt(cocoonBagIn.get(ModDataComponents.URO_CONTENT).content());
            menu.getCocoonBag().set(ModDataComponents.URO_CONTENT, cocoonBagIn.get(ModDataComponents.URO_CONTENT).setIncoming(false));
        }
        if (cocoonBagIn.get(ModDataComponents.URO_CONTENT).outgoing()) {
            PacketDistributor.sendToServer( new CocoonBagServerPacket(Optional.of(menu.getCocoonBag().get(ModDataComponents.URO_CONTENT).content()),
                    CocoonBag.getUroId(menu.getCocoonBag()), false, false));
            menu.getCocoonBag().set(ModDataComponents.URO_CONTENT, cocoonBagIn.get(ModDataComponents.URO_CONTENT).setOutgoing(false));
        }
    }

    @Override
    public void removed() {
        if (menu.getCocoonBag().has(ModDataComponents.URO_CONTENT)){
            PacketDistributor.sendToServer( new CocoonBagServerPacket(Optional.of(menu.getCocoonBag().get(ModDataComponents.URO_CONTENT).content()),
                    CocoonBag.getUroId(menu.getCocoonBag()), false, true));
            ItemStack cocoonBagIn = playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.COCOON_BAG.get()) ?
                    playerInventory.player.getItemBySlot(EquipmentSlot.MAINHAND) : playerInventory.player.getItemBySlot(EquipmentSlot.OFFHAND);
            cocoonBagIn.remove(ModDataComponents.URO_CONTENT);
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
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
