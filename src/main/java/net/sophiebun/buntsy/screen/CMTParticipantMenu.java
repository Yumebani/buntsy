package net.sophiebun.buntsy.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkCrafterEntity;
import net.sophiebun.buntsy.blocks.inventory.FilterSlot;
import net.sophiebun.buntsy.blocks.inventory.OutputSlot;
import net.sophiebun.buntsy.blocks.inventory.PatternOutputSlot;
import net.sophiebun.buntsy.blocks.inventory.PatternSlot;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;

import java.util.ArrayList;
import java.util.List;

public class CMTParticipantMenu extends AbstractContainerMenu {

    private final ItemStackHandler filterItemHandler = new ItemStackHandler(12);
    private final Level level;

    public final BlockPos pos;
    public final BlockPos terminal;
    public final CMTParticipantData data;
    public final List<Direction> availableSides;


    protected CMTParticipantMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, extraData.readBlockPos(), extraData.readBlockPos(),
                CMTParticipantData.parseCompound(extraData.readNbt()), getSidesList(extraData));
    }

    private static List<Direction> getSidesList(FriendlyByteBuf extraData){

        List<Direction> availableSides = new ArrayList<>();
        int size = extraData.readInt();
        for (int i = 0; i < size; i++){
            availableSides.add(Direction.values()[extraData.readInt()]);
        }
        return availableSides;
    }

    public CMTParticipantMenu(int pContainerId, Inventory playerInventory, BlockPos pos, BlockPos terminal, CMTParticipantData data, List<Direction> availableSides) {
        super(ModMenuTypes.CMT_PARTICIPANT_MENU.get(), pContainerId);
        this.level = playerInventory.player.level();

        this.pos = pos;
        this.terminal = terminal;
        this.data = data;
        this.availableSides = availableSides;

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        for (int i = 0; i < 12; i++){
            this.addSlot(new FilterSlot(filterItemHandler, i,  86 + (18 * (i % 4)), 55 + (18 * (i / 4))));
        };
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
    private static final int TE_INVENTORY_SLOT_COUNT = 0;  // must be the number of slots you have!
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
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + 8 + l * 18, 126 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + 8 + i * 18, 184));
        }
    }
}
