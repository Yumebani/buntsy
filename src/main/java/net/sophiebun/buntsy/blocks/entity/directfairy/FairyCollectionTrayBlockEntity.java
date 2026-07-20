package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.screen.FairyCollectionTrayMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FairyCollectionTrayBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    public final ItemStackHandler itemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int OUTPUT_SLOT_START = 0;
    private static final int OUTPUT_SLOT_COUNT = 15;

    private final List<Integer> randomRotations;

    public List<Integer> getRandomRotations() {
        return randomRotations;
    }

    public FairyCollectionTrayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(0.7f);

        Random random = new Random();

        randomRotations = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            randomRotations.add(random.nextInt(0, 270));
        }
    }

    @Override
    public boolean isTitular() {
        return true;
    }

    @Override
    public int getFairyWeight() {
        return 4;
    }

    public ItemStackHandler getItemHandler(Direction side) {
         return itemHandler;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.fairy_collection_tray");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyCollectionTrayMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public List<ItemStack> getRenderItems(){

        List<Integer> values = getFilledSlotList();

        List<ItemStack> renderItems = new ArrayList<>();

        for (Integer i : values){
            if (renderItems.size() < 4){
                renderItems.add(this.itemHandler.getStackInSlot(i));
            }
        }

        return renderItems;
    }

    private List<Integer> getFilledSlotList() {

        List<Integer> slots = new ArrayList<Integer>();
        for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; i++){
            if (!this.itemHandler.getStackInSlot(i).isEmpty() && this.itemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    public void depositItem(ItemStack item) {
        Integer outputSlot = getClearOutput(item);

        if (outputSlot != null){
            this.itemHandler.setStackInSlot(outputSlot,
                    new ItemStack(item.getItem(),
                            this.itemHandler.getStackInSlot(outputSlot).getCount() + item.getCount()));
        }
        else {
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, item);
            Containers.dropContents(this.level, this.worldPosition, container);
        }
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; i++){
            if ((itemHandler.getStackInSlot(i).isEmpty() || this.itemHandler.getStackInSlot(i).getItem() == result.getItem())
                    && (this.itemHandler.getStackInSlot(i).getCount() + result.getCount() <= result.getMaxStackSize())){
                return i;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}