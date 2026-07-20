package net.sophiebun.buntsy.screen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.inventory.InvDisplaySlot;
import net.sophiebun.buntsy.codec.UroContent;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.server.packets.CocoonBagServerPacket;

import java.util.Optional;

public class CocoonBagMenu extends AbstractContainerMenu {

    private final ItemStack cocoonBag;
    private ItemStackHandler handler;

    protected CocoonBagMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, inv.player);
    }

    public void updateNbt(CompoundTag tag){
        handler.deserializeNBT(Minecraft.getInstance().level.registryAccess(), tag);
    }

    public ItemStack getCocoonBag(){
        return cocoonBag;
    }

    public CocoonBagMenu(int pContainerId, Inventory inv, Player pPlayer) {
        super(ModMenuTypes.COCOON_BAG_MENU.get(), pContainerId);
        this.cocoonBag = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.COCOON_BAG.get()) ?
                pPlayer.getItemBySlot(EquipmentSlot.MAINHAND) : pPlayer.getItemBySlot(EquipmentSlot.OFFHAND);

        if (!this.cocoonBag.has(ModDataComponents.URO_ID)){
            handler = new ItemStackHandler(27){
                @Override
                protected void onContentsChanged(int slot) {
                    cocoonBag.set(ModDataComponents.URO_CONTENT, new UroContent(this.serializeNBT(Minecraft.getInstance().level.registryAccess()), false, true));
                }
            };

            if (this.cocoonBag.has(ModDataComponents.URO_ID)) handler.deserializeNBT(Minecraft.getInstance().level.registryAccess(), this.cocoonBag.get(ModDataComponents.URO_CONTENT).content());
        }
        else {
            PacketDistributor.sendToServer( new CocoonBagServerPacket(Optional.of(getCocoonBag().get(ModDataComponents.URO_CONTENT).content()),
                    CocoonBag.getUroId(getCocoonBag()), true, false));
            handler = new ItemStackHandler(27){
                @Override
                protected void onContentsChanged(int slot) {
                    cocoonBag.set(ModDataComponents.URO_CONTENT, new UroContent(this.serializeNBT(Minecraft.getInstance().level.registryAccess()), false, true));
                }

                @Override
                public int getSlots() {
                    onContentsChanged(0);
                    return super.getSlots();
                }
            };

            if (this.cocoonBag.has(ModDataComponents.URO_CONTENT)) handler.deserializeNBT(Minecraft.getInstance().level.registryAccess(), this.cocoonBag.get(ModDataComponents.URO_CONTENT).content());
        }

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
        addBlockInventory();
    }

    private void addBlockInventory() {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new SlotItemHandler(handler, l + i * 9, 8 + l * 18, 18 + i * 18));
            }
        }
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots both the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 27;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        handler.getSlots();
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                if (playerInventory.getItem(l + i * 9 + 9).equals(this.cocoonBag)){
                    this.addSlot(new InvDisplaySlot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
                }
                else {
                    this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
                }
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            if (playerInventory.getItem(i).equals(this.cocoonBag)){
                this.addSlot(new InvDisplaySlot(playerInventory, i, 8 + i * 18, 142));
            }
            else {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
            }
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
    }
}
