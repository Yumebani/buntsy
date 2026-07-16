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
import net.sophiebun.buntsy.recipe.FumeDistilleryRecipe;
import net.sophiebun.buntsy.recipe.MagicCrystalizerRecipe;
import net.sophiebun.buntsy.screen.FumeDistilleryMenu;
import net.sophiebun.buntsy.screen.MagicCrystalizerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FumeDistilleryBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ItemStackHandler bottleItemHandler = new ItemStackHandler(1) {
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

    private static final int FAIRY_WEIGHT = 1;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> bottleLazyItemHandler = LazyOptional.of(() -> bottleItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getBottleLazyItemHandler() {
        return bottleLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    public FumeDistilleryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FUME_DISTILLERY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> FumeDistilleryBlockEntity.this.progress;
                    case 1 -> FumeDistilleryBlockEntity.this.maxProgress;
                    case 2 -> FumeDistilleryBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> FumeDistilleryBlockEntity.this.progress = i1;
                    case 1 -> FumeDistilleryBlockEntity.this.maxProgress = i1;
                    case 2 -> FumeDistilleryBlockEntity.this.setEnchanted(i1 == 1);
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
        return Component.translatable("block.buntsy.fume_distillery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FumeDistilleryMenu(i, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
            }
            else if (side.equals(Direction.UP)){
                return bottleLazyItemHandler.cast();
            }
            else{
                return inputLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputLazyItemHandler = LazyOptional.of((() -> inputItemHandler));
        bottleLazyItemHandler = LazyOptional.of((() -> bottleItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        bottleLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(9);
        for (int i = 0; i < 2; i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(2, bottleItemHandler.getStackInSlot(0));
        inventory.setItem(3, outputItemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT());
        pTag.put("bottleInventory", bottleItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("magic_crystalizer.progress", this.progress);
        pTag.putInt("magic_crystalizer.max_progress", this.maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inputItemHandler.deserializeNBT(pTag.getCompound("inputInventory"));
        this.bottleItemHandler.deserializeNBT(pTag.getCompound("bottleInventory"));
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
        return (this.outputItemHandler.getStackInSlot(0).isEmpty() || this.outputItemHandler.getStackInSlot(0).getItem() == primary.getItem())
                && (this.outputItemHandler.getStackInSlot(0).getCount() + primary.getCount() <= primary.getMaxStackSize());
    }

    public void outputItems(ItemStack primary){
        ItemStack newItem = new ItemStack(primary.getItem(), this.outputItemHandler.getStackInSlot(0).getCount() + primary.getCount());
        newItem.setTag(primary.getTag());
        this.outputItemHandler.setStackInSlot(0, newItem);
    }

    public void craftItem() {
        FumeDistilleryRecipe recipe = getCurrentRecipe().get();
        ItemStack result = recipe.getResultItem(null);

        this.inputItemHandler.extractItem(0, 1, false);
        this.bottleItemHandler.extractItem(0, result.getCount(), false);

        outputItems(result);
    }

    public boolean hasRecipe() {
        Optional<FumeDistilleryRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        ItemStack result = recipe.get().getResultItem(null);
        return isOutputClear(result);
    }

    public Optional<FumeDistilleryRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(4);
        for (int i = 0; i < 2; i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(2, bottleItemHandler.getStackInSlot(0));
        inventory.setItem(3, outputItemHandler.getStackInSlot(0));

        return this.level.getRecipeManager().getRecipeFor(FumeDistilleryRecipe.Type.INSTANCE, inventory, level);
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
