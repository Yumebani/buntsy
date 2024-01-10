package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.inventory.OutputSlot;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.recipe.ThreadReelerRecipe;
import net.sophiebun.buntsy.screen.ThreadReelerMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ThreadReelerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int FAIRY_SLOT = 0;
    private static final int FAIRY_FOOD_SLOT = 1;
    private static final int FAIRY_FOOD_OUTPUT_SLOT = 2;
    private static final int INPUT_SLOT = 3;
    private static final int OUTPUT_SLOT = 4;

    private static final Map<Item, Integer> foodMaps = Map.of(
            Items.SUGAR, 400,
            Items.HONEY_BOTTLE, 800
    );

    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(ModItems.COCOON.get()), new ItemStack(ModItems.SILK.get(), 3)),
            new TempRecipe(Ingredient.of(ModItems.MOLTED_MOTH_WINGS.get()), new ItemStack(ModItems.MOTH_WING_THREAD.get(), 3))
    );

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int food = 0;
    private int maxFood = 100;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ThreadReelerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> ThreadReelerBlockEntity.this.progress;
                    case 1 -> ThreadReelerBlockEntity.this.maxProgress;
                    case 2 -> ThreadReelerBlockEntity.this.food;
                    case 3 -> ThreadReelerBlockEntity.this.maxFood;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ThreadReelerBlockEntity.this.progress = i1;
                    case 1 -> ThreadReelerBlockEntity.this.maxProgress = i1;
                    case 2 -> ThreadReelerBlockEntity.this.food = i1;
                    case 3 -> ThreadReelerBlockEntity.this.maxFood = i1;
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
        return Component.translatable("block.buntsy.thread_reeler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ThreadReelerMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("thread_reeler.progress", this.progress);
        pTag.putInt("thread_reeler.max_progress", this.maxProgress);
        pTag.putInt("thread_reeler.food", this.food);
        pTag.putInt("thread_reeler.max_food", this.maxFood);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("thread_reeler.progress");
        this.maxProgress = pTag.getInt("thread_reeler.max_progress");
        this.food = pTag.getInt("thread_reeler.food");
        this. maxFood = pTag.getInt("thread_reeler.max_food");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        
        if (tempHasRecipe() && hasFairy()){

            if (!hasFood()){
                if (hasFoodItem() && foodHasOutput()){
                    consumeFood();
                    tick(pLevel, pPos, pState);
                }
                else{
                    resetProgress();
                }
            }
            else{
                increaseProgress();
                decreaseFood();

                if (hasProgressFinished()){
                    resetProgress();
                    tempCraftItem();
                    tick(pLevel, pPos, pState);
                }
            }
        }
        else{
            resetProgress();
        }
    }
    private boolean hasFairy() {
        return !this.itemHandler.getStackInSlot(FAIRY_SLOT).isEmpty();
    }

    private void consumeFood() {
        Item foodItem = this.itemHandler.getStackInSlot(FAIRY_FOOD_SLOT).getItem();

        setFoodTick(foodItem);

        outputFoodItem();
        this.itemHandler.extractItem(FAIRY_FOOD_SLOT, 1, false);
    }

    private Item getFoodItemToOutput(){
        return this.itemHandler.getStackInSlot(FAIRY_FOOD_SLOT).is(ModTags.Items.BOTTLED_ITEM) ?
                Items.GLASS_BOTTLE : this.itemHandler.getStackInSlot(FAIRY_FOOD_SLOT).is(ModTags.Items.BOWL_ITEM) ?
                Items.BOWL : null;
    }

    private boolean foodHasOutput() {
        Item item = getFoodItemToOutput();

        if (item != null){
            ItemStack itemToPlace = new ItemStack(getFoodItemToOutput());
            return isOutputClear(itemToPlace, FAIRY_FOOD_OUTPUT_SLOT);
        }

        return true;
    }

    private void outputFoodItem(){
        Item itemToPlace = getFoodItemToOutput();

        if (itemToPlace != null){
            this.itemHandler.setStackInSlot(FAIRY_FOOD_OUTPUT_SLOT,
                    new ItemStack(itemToPlace,this.itemHandler.getStackInSlot(FAIRY_FOOD_OUTPUT_SLOT).getCount() + 1));
        }
    }


    private void setFoodTick(Item item) {
        int tickCount = foodMaps.containsKey(item) ? foodMaps.get(item) : ((FairyFoodItem) item).getFoodTick();
        this.maxFood = tickCount;
        this.food = tickCount;
    }

    private void decreaseFood() {
        this.food--;
    }

    private boolean hasFoodItem() {
        return this.itemHandler.getStackInSlot(FAIRY_FOOD_SLOT).is(ModTags.Items.FAIRY_FOOD);
    }

    private boolean hasFood() {
        return this.food > 0;
    }

    private void tempCraftItem() {
        TempRecipe recipe = tempGetCurrentRecipe();
        ItemStack result = recipe.getResult();

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean tempHasRecipe() {
        TempRecipe recipe = tempGetCurrentRecipe();
        if (recipe == null){
            return false;
        }

        ItemStack result = recipe.getResult();
        return isOutputClear(result, OUTPUT_SLOT);
    }

    private TempRecipe tempGetCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        for (TempRecipe recipe : recipeList){
            if (recipe.isPresent(inventory, new int[]{INPUT_SLOT})){
                return recipe;
            }
        }

        return null;
    }

    private void craftItem() {
        Optional<ThreadReelerRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<ThreadReelerRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            System.out.println("no recipe detected");
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);

        return isOutputClear(result, OUTPUT_SLOT);
    }

    private boolean isOutputClear(ItemStack result, int slot) {
        return (this.itemHandler.getStackInSlot(slot).isEmpty() || this.itemHandler.getStackInSlot(slot).getItem() == result.getItem())
                && (this.itemHandler.getStackInSlot(slot).getCount() + result.getCount() <= result.getMaxStackSize());
    }


    private Optional<ThreadReelerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ThreadReelerRecipe.Type.INSTANCE, inventory, level);
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