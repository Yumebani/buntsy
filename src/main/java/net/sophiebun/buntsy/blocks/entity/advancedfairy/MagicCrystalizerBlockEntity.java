package net.sophiebun.buntsy.blocks.entity.advancedfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.MagicCrystalizerRecipe;
import net.sophiebun.buntsy.recipe.MagicCrystallizerInput;
import net.sophiebun.buntsy.screen.MagicCrystalizerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MagicCrystalizerBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    public final ItemStackHandler sampleItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler dustItemHandler = new ItemStackHandler(7) {
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

    private static final int EXTRA_INPUT_SLOT_COUNT = 7;
    private static final int OUTPUT_SLOT = 0;
    private static final int FAIRY_WEIGHT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1000;

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

    public ItemStackHandler getItemHandler(Direction side) {
        if (side.equals(Direction.DOWN)){
            return outputItemHandler;
        }
        else if (side.equals(Direction.UP)){
            return sampleItemHandler;
        }
        else{
            return dustItemHandler;
        }
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("sampleInventory", sampleItemHandler.serializeNBT(registries));
        pTag.put("dustInventory", dustItemHandler.serializeNBT(registries));
        pTag.put("outputInventory", outputItemHandler.serializeNBT(registries));
        pTag.putInt("magic_crystalizer.progress", this.progress);
        pTag.putInt("magic_crystalizer.max_progress", this.maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.sampleItemHandler.deserializeNBT(registries, pTag.getCompound("sampleInventory"));
        this.dustItemHandler.deserializeNBT(registries, pTag.getCompound("dustInventory"));
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
        MagicCrystalizerRecipe recipe = getCurrentRecipe().get().value();
        ItemStack result = recipe.getResultItem(null);

        this.sampleItemHandler.extractItem(0, 1, false);
        for (int slot = 0; slot < EXTRA_INPUT_SLOT_COUNT; slot++){
            this.dustItemHandler.extractItem(slot, 1, false);
        }

        outputItems(result);
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<MagicCrystalizerRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        ItemStack result = recipe.get().value().getResultItem(null);
        return isOutputClear(result);
    }

    public Optional<RecipeHolder<MagicCrystalizerRecipe>> getCurrentRecipe() {
        List<Ingredient> list = new ArrayList<>();
        list.add(Ingredient.of(sampleItemHandler.getStackInSlot(0)));
        for (int i = 0; i < 7; i++) {
            list.add(Ingredient.of(dustItemHandler.getStackInSlot(i)));
        }

        return this.level.getRecipeManager().getRecipeFor(MagicCrystalizerRecipe.Type.INSTANCE, new MagicCrystallizerInput(list), level);
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
