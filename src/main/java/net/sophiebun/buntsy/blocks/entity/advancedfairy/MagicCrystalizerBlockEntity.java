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

    private final ItemStackHandler sampleItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ItemStackHandler dustItemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int EXTRA_INPUT_SLOT_COUNT = 7;
    private static final int OUTPUT_SLOT = 0;
    private static final int FAIRY_WEIGHT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1000;

    private LazyOptional<IItemHandler> sampleLazyItemHandler = LazyOptional.of(() -> sampleItemHandler);
    private LazyOptional<IItemHandler> dustLazyItemHandler = LazyOptional.of(() -> dustItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public LazyOptional<IItemHandler> getSampleLazyItemHandler() {
        return sampleLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getDustLazyItemHandler() {
        return dustLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

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
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
            }
            else if (side.equals(Direction.UP)){
                return sampleLazyItemHandler.cast();
            }
            else{
                return dustLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        sampleLazyItemHandler = LazyOptional.of((() -> sampleItemHandler));
        dustLazyItemHandler = LazyOptional.of((() -> dustItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        sampleLazyItemHandler.invalidate();
        dustLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(9);
        inventory.setItem(0, sampleItemHandler.getStackInSlot(0));
        inventory.setItem(8, outputItemHandler.getStackInSlot(0));
        for (int i = 1; i < 8; i++) {
            inventory.setItem(i, dustItemHandler.getStackInSlot(i - 1));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("sampleInventory", sampleItemHandler.serializeNBT());
        pTag.put("dustInventory", dustItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("magic_crystalizer.progress", this.progress);
        pTag.putInt("magic_crystalizer.max_progress", this.maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.sampleItemHandler.deserializeNBT(pTag.getCompound("sampleInventory"));
        this.dustItemHandler.deserializeNBT(pTag.getCompound("dustInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
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
        this.outputItemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(primary.getItem(),
                        this.outputItemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + primary.getCount()));
    }

    public boolean isSlotClear(int slot, ItemStack item){
        return (this.outputItemHandler.getStackInSlot(slot).isEmpty() || this.outputItemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }

    public void craftItem() {
        MagicCrystalizerRecipe recipe = getCurrentRecipe().get();
        ItemStack result = recipe.getResultItem(null);

        this.sampleItemHandler.extractItem(0, 1, false);
        for (int slot = 0; slot < EXTRA_INPUT_SLOT_COUNT; slot++){
            this.dustItemHandler.extractItem(slot, 1, false);
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
        SimpleContainer inventory = new SimpleContainer(9);
        inventory.setItem(0, sampleItemHandler.getStackInSlot(0));
        inventory.setItem(8, outputItemHandler.getStackInSlot(0));
        for (int i = 1; i < 8; i++) {
            inventory.setItem(i, dustItemHandler.getStackInSlot(i - 1));
        }

        return this.level.getRecipeManager().getRecipeFor(MagicCrystalizerRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseProgress() {
        this.progress += getSpeedUp();
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
