package net.sophiebun.buntsy.screen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sophiebun.buntsy.blocks.inventory.InvDisplaySlot;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.server.ModCocoonBagServerPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class CocoonBagMenu extends AbstractContainerMenu {

    private final ItemStack cocoonBag;
    private ItemStackHandler handler;
    private LazyOptional<IItemHandler> handlerLazyOptional = LazyOptional.of(() -> handler);

    protected CocoonBagMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, inv.player);
    }

    public void updateNbt(CompoundTag tag){
        handler.deserializeNBT(tag);
    }

    public ItemStack getCocoonBag(){
        return cocoonBag;
    }

    public CocoonBagMenu(int pContainerId, Inventory inv, Player pPlayer) {
        super(ModMenuTypes.COCOON_BAG_MENU.get(), pContainerId);
        this.cocoonBag = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.COCOON_BAG.get()) ?
                pPlayer.getItemBySlot(EquipmentSlot.MAINHAND) : pPlayer.getItemBySlot(EquipmentSlot.OFFHAND);

        if (!this.cocoonBag.hasTag() || !this.cocoonBag.getTag().contains("buntsy.uro_id")){
            handler = new ItemStackHandler(27){
                @Override
                protected void onContentsChanged(int slot) {
                    CompoundTag tag = new CompoundTag();
                    tag.put("contents" ,this.serializeNBT());
                    cocoonBag.setTag(tag);
                }
            };

            if (this.cocoonBag.hasTag()) handler.deserializeNBT(this.cocoonBag.getTag().getCompound("contents"));
        }
        else {
            ModPacketHandler.INSTANCE.sendToServer( new ModCocoonBagServerPacket(CocoonBag.getUroId(getCocoonBag()),
                    getCocoonBag().getTag().getCompound("uro_contents"), true, false));
            handler = new ItemStackHandler(27){
                @Override
                protected void onContentsChanged(int slot) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("buntsy.uro_id", cocoonBag.getTag().getInt("buntsy.uro_id"));
                    tag.put("uro_contents" ,this.serializeNBT());
                    tag.putBoolean("out_update", true);
                    cocoonBag.setTag(tag);
                }

                @Override
                public int getSlots() {
                    onContentsChanged(0);
                    return super.getSlots();
                }
            };

            if (this.cocoonBag.getTag().contains("uro_contents")) handler.deserializeNBT(this.cocoonBag.getTag().getCompound("uro_contents"));
        }

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
        addBlockInventory(handlerLazyOptional);

        ///give @s buntsy:uro{buntsy.uro_id: 0} 2
    }

    private void addBlockInventory(LazyOptional<IItemHandler> contentLazyItemHandler) {
        contentLazyItemHandler.ifPresent(iItemHandler -> {
            for (int i = 0; i < 3; ++i) {
                for (int l = 0; l < 9; ++l) {
                    this.addSlot(new SlotItemHandler(iItemHandler, l + i * 9, 8 + l * 18, 18 + i * 18));
                }
            }
        });
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
                if (playerInventory.getItem(l + i * 9 + 9).equals(this.cocoonBag, false)){
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
            if (playerInventory.getItem(i).equals(this.cocoonBag, false)){
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
