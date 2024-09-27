package net.sophiebun.buntsy.blocks.entity.advancedfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.MagicCrystalizerRecipe;
import net.sophiebun.buntsy.screen.MagicCrystalizerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MagicCrystalizerBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int SAMPLE_INPUT_SLOT = 0;
    private static final int EXTRA_INPUT_SLOT_START = 1;
    private static final int EXTRA_INPUT_SLOT_COUNT = 7;
    private static final int OUTPUT_SLOT = 8;
    private static final int FAIRY_WEIGHT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1000;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MagicCrystalizerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MAGIC_CRYSTALIZER_BLOCK_ENTITY.get(), pPos, pBlockState);

        setConsumption(1.5f);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> MagicCrystalizerBlockEntity.this.progress;
                    case 1 -> MagicCrystalizerBlockEntity.this.maxProgress;
                    case 2 -> MagicCrystalizerBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> MagicCrystalizerBlockEntity.this.progress = i1;
                    case 1 -> MagicCrystalizerBlockEntity.this.maxProgress = i1;
                    case 2 -> MagicCrystalizerBlockEntity.this.setEnchanted(i1 == 1);
                };
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.magic_crystalizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MagicCrystalizerMenu(i, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of((() -> itemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("magic_crystalizer.progress", this.progress);
        pTag.putInt("magic_crystalizer.max_progress", this.maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("magic_crystalizer.progress");
        this.maxProgress = pTag.getInt("magic_crystalizer.max_progress");
    }

    @Override
    public int getFairyWeight() {
        return FAIRY_WEIGHT;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (canRun()){

            if (!Mth.equal(getConsumption(), 1f)){
                setConsumption(1f);
            }

            increaseProgress();

            if (hasProgressFinished()){
                resetProgress();
                craftItem();
                tick(pLevel, pPos, pState);
            }
        }
        else{

            if (Mth.equal(getConsumption(), 1f)){
                setConsumption(0);
            }

            resetProgress();
        }
    }

    public boolean canRun(){
        return hasRecipe() && isEnchanted();
    }

    public boolean isOutputClear(ItemStack primary){
        return isSlotClear(OUTPUT_SLOT, primary);
    }

    public void outputItems(ItemStack primary){
        this.itemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(primary.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + primary.getCount()));
    }

    public boolean isSlotClear(int slot, ItemStack item){
        return (this.itemHandler.getStackInSlot(slot).isEmpty() || this.itemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.itemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }

    public void craftItem() {
        MagicCrystalizerRecipe recipe = getCurrentRecipe().get();
        ItemStack result = recipe.getResultItem(null);

        this.itemHandler.extractItem(SAMPLE_INPUT_SLOT, 1, false);
        for (int slot = EXTRA_INPUT_SLOT_START; slot < EXTRA_INPUT_SLOT_START + EXTRA_INPUT_SLOT_COUNT; slot++){
            this.itemHandler.extractItem(slot, 1, false);
        }

        outputItems(result);
    }

    public boolean hasRecipe() {
        Optional<MagicCrystalizerRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        ItemStack result = recipe.get().getResultItem(null);
        return isOutputClear(result);
    }

    public Optional<MagicCrystalizerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(MagicCrystalizerRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseProgress() {
        this.progress++;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
