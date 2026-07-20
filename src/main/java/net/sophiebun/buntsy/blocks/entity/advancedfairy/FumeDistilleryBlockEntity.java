package net.sophiebun.buntsy.blocks.entity.advancedfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.FumeDistilleryInput;
import net.sophiebun.buntsy.recipe.FumeDistilleryRecipe;
import net.sophiebun.buntsy.screen.FumeDistilleryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FumeDistilleryBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    public final ItemStackHandler inputItemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler bottleItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
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

    public IItemHandler getItemHandler(Direction side) {
        if (side.equals(Direction.DOWN)){
            return outputItemHandler;
        }
        else if (side.equals(Direction.UP)){
            return bottleItemHandler;
        }
        else{
            return inputItemHandler;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT(registries));
        pTag.put("bottleInventory", bottleItemHandler.serializeNBT(registries));
        pTag.put("outputInventory", outputItemHandler.serializeNBT(registries));
        pTag.putInt("magic_crystalizer.progress", this.progress);
        pTag.putInt("magic_crystalizer.max_progress", this.maxProgress);

        super.saveAdditional(pTag, registries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inputItemHandler.deserializeNBT(registries, pTag.getCompound("inputInventory"));
        this.bottleItemHandler.deserializeNBT(registries, pTag.getCompound("bottleInventory"));
        this.outputItemHandler.deserializeNBT(registries, pTag.getCompound("outputInventory"));
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
        newItem.applyComponents(primary.getComponents());
        this.outputItemHandler.setStackInSlot(0, newItem);
    }

    public void craftItem() {
        FumeDistilleryRecipe recipe = getCurrentRecipe().get().value();
        ItemStack result = recipe.getResultItem(null);

        this.inputItemHandler.extractItem(0, 1, false);
        this.bottleItemHandler.extractItem(0, result.getCount(), false);

        outputItems(result);
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<FumeDistilleryRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        ItemStack result = recipe.get().value().getOutput();
        return isOutputClear(result);
    }

    public Optional<RecipeHolder<FumeDistilleryRecipe>> getCurrentRecipe() {
        FumeDistilleryInput input = new FumeDistilleryInput(
                inputItemHandler.getStackInSlot(0),
                inputItemHandler.getStackInSlot(1),
                bottleItemHandler.getStackInSlot(0));

        return this.level.getRecipeManager().getRecipeFor(FumeDistilleryRecipe.Type.INSTANCE, input, level);
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
