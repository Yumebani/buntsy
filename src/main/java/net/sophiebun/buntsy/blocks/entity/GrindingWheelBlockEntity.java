package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.GrindingWheelBlock;
import net.sophiebun.buntsy.screen.GrindingWheelMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrindingWheelBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(4);

    private static final int FAIRY_SLOT = 0;
    private static final int FAIRY_FOOD_SLOT = 1;
    private static final int INPUT_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;
    private int food = 0;
    private int maxFood = 1600;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public GrindingWheelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> GrindingWheelBlockEntity.this.progress;
                    case 1 -> GrindingWheelBlockEntity.this.maxProgress;
                    case 2 -> GrindingWheelBlockEntity.this.food;
                    case 3 -> GrindingWheelBlockEntity.this.maxFood;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> GrindingWheelBlockEntity.this.progress = i1;
                    case 1 -> GrindingWheelBlockEntity.this.maxProgress = i1;
                    case 2 -> GrindingWheelBlockEntity.this.food = i1;
                    case 3 -> GrindingWheelBlockEntity.this.maxFood = i1;
                };
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
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
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.grinding_wheel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GrindingWheelMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("grinding_wheel.progress", this.progress);
        pTag.putInt("grinding_wheel.food", this.food);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("grinding_wheel.progress");
        this.food = pTag.getInt("grinding_wheel.food");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (hasRecipe() && hasFairy()){
            if (!hasFood()){
                if (hasFoodItem()){
                    consumeFood();
                    setChanged();
                    tick(pLevel, pPos, pState);
                }
                else{
                    resetProgress();
                }
            }
            else{
                increaseProgress();
                decreaseFood();
                setChanged();

                if (hasProgressFinished()){
                    resetProgress();
                    craftItem();
                }
            }
        }
        else{
            resetProgress();
        }
    }

    private void craftItem() {
    }

    private boolean hasProgressFinished() {
        return this.progress == this.maxProgress;
    }

    private void decreaseFood() {
        this.food--;
    }

    private void increaseProgress() {
        this.progress++;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void consumeFood() {

    }

    private boolean hasFoodItem() {
    }

    private boolean hasFood() {
        return this.food != 0;
    }

    private boolean hasFairy() {
    }

    private boolean hasRecipe() {
    }
}
