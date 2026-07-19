package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.screen.clockwork.ClockworkCrafterMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ClockworkCrafterEntity extends WindupClockworkEntity implements MenuProvider {

    protected final ItemStackHandler inputItemHandler = new ItemStackHandler(18) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> this.inputItemHandler);
    public LazyOptional<IItemHandler> getInputLazyItemHandler() {return inputLazyItemHandler;}

    protected final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> this.outputItemHandler);
    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {return outputLazyItemHandler;}

    protected final ItemStackHandler patternItemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> patternLazyItemHandler = LazyOptional.of(() -> this.patternItemHandler);
    public LazyOptional<IItemHandler> getPatternLazyItemHandler() {return patternLazyItemHandler;}

    protected final ItemStackHandler resultItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> resultLazyItemHandler = LazyOptional.of(() -> this.resultItemHandler);
    public LazyOptional<IItemHandler> getResultLazyItemHandler() {return resultLazyItemHandler;}

    private final ContainerData data;

    private int progress = 0;
    private int maxProgress = 200;

    private int nextCheck = 0;

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_crafter");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkCrafterMenu(i, inventory, this, this.data);
    }

    public ClockworkCrafterEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_CRAFTER_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> ClockworkCrafterEntity.this.progress;
                    case 1 -> ClockworkCrafterEntity.this.maxProgress;
                    case 2 -> ClockworkCrafterEntity.this.isWoundUp() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ClockworkCrafterEntity.this.progress = i1;
                    case 1 -> ClockworkCrafterEntity.this.maxProgress = i1;
                };
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
            }
            else {
                return inputLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputLazyItemHandler = LazyOptional.of((() -> this.inputItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> this.outputItemHandler));
        patternLazyItemHandler = LazyOptional.of((() -> this.patternItemHandler));
        resultLazyItemHandler = LazyOptional.of((() -> this.resultItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
        patternLazyItemHandler.invalidate();
        resultLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(19);

        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }

        inventory.setItem(18, outputItemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("clockwork_syrup_extractor.inputInventory", inputItemHandler.serializeNBT());
        pTag.put("clockwork_syrup_extractor.outputInventory", outputItemHandler.serializeNBT());
        pTag.put("clockwork_syrup_extractor.patternInventory", patternItemHandler.serializeNBT());
        pTag.putInt("clockwork_syrup_extractor.progress", this.progress);
        pTag.putInt("clockwork_syrup_extractor.max_progress", this.maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inputItemHandler.deserializeNBT(pTag.getCompound("clockwork_syrup_extractor.inputInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("clockwork_syrup_extractor.outputInventory"));
        this.patternItemHandler.deserializeNBT(pTag.getCompound("clockwork_syrup_extractor.patternInventory"));
        this.progress = pTag.getInt("clockwork_syrup_extractor.progress");
        this.maxProgress = pTag.getInt("clockwork_syrup_extractor.max_progress");
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

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    public boolean isSlotClear(int slot, ItemStack item){
        return (this.outputItemHandler.getStackInSlot(slot).isEmpty() || this.outputItemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }


    public void insertIntoSlot(int slot, ItemStack item){
        ItemStack inserted;
        if (this.outputItemHandler.getStackInSlot(slot).isEmpty()){
            inserted = item;
        } else {
            inserted = new ItemStack(item.getItem(),
                    this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount());
        }
        if (item.hasTag()) inserted.setTag(item.getTag());
        this.outputItemHandler.setStackInSlot(slot, inserted);
    }

    public Integer getAvailableSlot(ItemStack item){
        for (int i = 0; i < 1; i++){
            if (isSlotClear(i, item)) {
                return i;
            }
        }
        return null;
    }


    public boolean isOutputClear(ItemStack stack){
        return getAvailableSlot(stack) != null;
    }

    public void outputItems(ItemStack stack){
        int pSlot = getAvailableSlot(stack);
        insertIntoSlot(pSlot, stack);
    }

    public boolean hasInputStacks(NonNullList<Ingredient> ingredients){

        HashMap<String, Integer> collector = new HashMap<>();
        for (int i = 0; i < 18; i++){
            ItemStack stack = inputItemHandler.getStackInSlot(i);
            String key = stack.getItem().getDescriptionId();
            if (collector.containsKey(key)){
                collector.put(key, collector.get(key) + stack.getCount());
            } else {
                collector.put(key, stack.getCount());
            }
        }

        for (Ingredient ingredient : ingredients){

            if (!ingredient.isEmpty()){
                int total = ingredient.getItems()[0].getCount();

                for (ItemStack itemStack : ingredient.getItems()){
                    String key = itemStack.getItem().getDescriptionId();
                    if (collector.containsKey(key)){
                        int remainder = collector.get(key) - total;
                        if (remainder >= 0){
                            collector.put(key, remainder);
                            if (!itemStack.getCraftingRemainingItem().isEmpty() && !canOutputRemainder(itemStack.getCraftingRemainingItem(), total)){
                                return false;
                            }
                            total = 0;
                            break;
                        } else {
                            total -= collector.get(key);
                            if (!itemStack.getCraftingRemainingItem().isEmpty() && !canOutputRemainder(itemStack.getCraftingRemainingItem(), collector.get(key))){
                                return false;
                            }
                            collector.put(key, 0);
                        }
                    }
                }

                if (total > 0){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canOutputRemainder(ItemStack remainingStack, int count){
        int totalRemaining = count;
        for (int i = 0; i < 18; i++){
            ItemStack stack = inputItemHandler.getStackInSlot(i);
            if (stack.isEmpty()){
                inputItemHandler.setStackInSlot(i, remainingStack);
            } else if (stack.is(remainingStack.getItem())){
                int remainder = stack.getCount() + totalRemaining;
                if (remainder > remainingStack.getMaxStackSize()){
                    totalRemaining = remainder - remainingStack.getMaxStackSize();
                } else {
                    return true;
                }
            }
        }
        return totalRemaining == 0;
    }

    public void takeFromInputs(NonNullList<Ingredient> ingredients){

        for (Ingredient ingredient : ingredients){

            if (!ingredient.isEmpty()){
                int total = ingredient.getItems()[0].getCount();
                for (ItemStack itemStack : ingredient.getItems()){

                    for (int i = 0; i < 18; i++){
                        ItemStack stack = inputItemHandler.getStackInSlot(i);
                        if (stack.is(itemStack.getItem())){
                            int remainder = stack.getCount() - total;
                            ItemStack remainingStack = itemStack.getCraftingRemainingItem();
                            if (remainder >= 0){
                                stack.setCount(remainder);
                                if (!remainingStack.isEmpty()){
                                    outputRemainder(remainingStack, remainder);
                                }
                                break;
                            } else {
                                total -= stack.getCount();
                                stack.setCount(0);
                                if (!remainingStack.isEmpty()){
                                    outputRemainder(remainingStack, stack.getCount());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void outputRemainder(ItemStack remainingStack, int count){
        int totalRemaining = count;
        for (int i = 0; i < 18; i++){
            ItemStack stack = inputItemHandler.getStackInSlot(i);
            if (stack.isEmpty()){
                inputItemHandler.setStackInSlot(i, remainingStack);
            } else if (stack.is(remainingStack.getItem())){
                int remainder = stack.getCount() + totalRemaining;
                if (remainder > remainingStack.getMaxStackSize()){
                    totalRemaining = remainder - remainingStack.getMaxStackSize();
                    stack.setCount(stack.getMaxStackSize());
                } else {
                    stack.setCount(remainder);
                    break;
                }
            }
        }
    }

    public Optional<CraftingRecipe> getCurrentRecipe() {
        CraftingContainer inventory = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
            @Override
            public ItemStack quickMoveStack(Player player, int slot) { return ItemStack.EMPTY; }
            @Override
            public boolean stillValid(Player player) { return true; }
        }, 3, 3);

        for (int i = 0; i < patternItemHandler.getSlots(); i++) {
            inventory.setItem(i, patternItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, level);
    }

    public boolean canCraft(Level level) {
        Optional<CraftingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            if (resultItemHandler.getStackInSlot(0).getCount() > 0 || !resultItemHandler.getStackInSlot(0).isEmpty()){
                resultItemHandler.setStackInSlot(0, ItemStack.EMPTY);
            }
            return false;
        }

        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        resultItemHandler.setStackInSlot(0, new ItemStack(result.getItem()));

        return isOutputClear(result) && hasInputStacks(recipe.get().getIngredients());
    }

    public void craft(Level level) {
        Optional<CraftingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        outputItems(new ItemStack(result.getItem(), result.getCount()));
        takeFromInputs(recipe.get().getIngredients());
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (this.isWoundUp()){
            this.tickWindup();

            if (this.canCraft(level)){
                this.progress += getClockworkProgressAmount();

                if (this.progress > this.maxProgress){
                    this.craft(level);
                    this.progress = 0;
                }

            } else {
                this.progress = 0;
            }
        }
    }

    @Override
    public int getWindupWeight() {
        return 1;
    }
}
