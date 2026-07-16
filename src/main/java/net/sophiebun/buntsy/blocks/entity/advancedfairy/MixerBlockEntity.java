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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
import net.sophiebun.buntsy.recipe.MixerRecipe;
import net.sophiebun.buntsy.screen.FumeDistilleryMenu;
import net.sophiebun.buntsy.screen.MixerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MixerBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(6) {
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
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    public MixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MIXER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> MixerBlockEntity.this.progress;
                    case 1 -> MixerBlockEntity.this.maxProgress;
                    case 2 -> MixerBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> MixerBlockEntity.this.progress = i1;
                    case 1 -> MixerBlockEntity.this.maxProgress = i1;
                    case 2 -> MixerBlockEntity.this.setEnchanted(i1 == 1);
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
        return Component.translatable("block.buntsy.mixer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MixerMenu(i, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
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
        outputLazyItemHandler = LazyOptional.of((() -> outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(7);
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(3, outputItemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("mixer.progress", this.progress);
        pTag.putInt("mixer.max_progress", this.maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inputItemHandler.deserializeNBT(pTag.getCompound("inputInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
        this.progress = pTag.getInt("mixer.progress");
        this.maxProgress = pTag.getInt("mixer.max_progress");
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

    private Integer getFirstFilledInputSlot(Item item) {
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++){
            if (this.inputItemHandler.getStackInSlot(i).is(item)){
                slotList.add(i);
            }
        }
        return slotList.isEmpty() ? null : slotList.get(0);
    }

    public void craftItem() {
        MixerRecipe recipe = getCurrentRecipe().get();
        ItemStack result = recipe.getResultItem(null);

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack ing : recipe.getInputs()){
            items.add(ing.copy());
        }

        while (!items.isEmpty()){
            int slot = getFirstFilledInputSlot(items.get(0).getItem());
            inputItemHandler.extractItem(slot, 1, false);
            items.get(0).shrink(1);
            if (items.get(0).isEmpty()){
                items.remove(0);
            }
        }

        outputItems(result);
    }

    public boolean hasRecipe() {
        Optional<MixerRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        ItemStack result = recipe.get().getResultItem(null);
        return isOutputClear(result);
    }

    public Optional<MixerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(6);
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(MixerRecipe.Type.INSTANCE, inventory, level);
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
