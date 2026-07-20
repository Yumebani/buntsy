package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.screen.clockwork.ClockworkCrafterMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ClockworkCrafterEntity extends WindupClockworkEntity implements MenuProvider {

    public final ItemStackHandler inputItemHandler = new ItemStackHandler(18) {
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

    public final ItemStackHandler patternItemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler resultItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

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
                    case 2 -> ClockworkCrafterEntity.this.windupRemaining;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ClockworkCrafterEntity.this.progress = i1;
                    case 1 -> ClockworkCrafterEntity.this.maxProgress = i1;
                    case 2 -> ClockworkCrafterEntity.this.windupRemaining = i1;
                };
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public ItemStackHandler getItemHandler(Direction side) {
        if (side.equals(Direction.DOWN)){
            return outputItemHandler;
        }
        else {
            return inputItemHandler;
        }
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("clockwork_syrup_extractor.inputInventory", inputItemHandler.serializeNBT(registries));
        pTag.put("clockwork_syrup_extractor.outputInventory", outputItemHandler.serializeNBT(registries));
        pTag.put("clockwork_syrup_extractor.patternInventory", patternItemHandler.serializeNBT(registries));
        pTag.putInt("clockwork_syrup_extractor.progress", this.progress);
        pTag.putInt("clockwork_syrup_extractor.max_progress", this.maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inputItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_syrup_extractor.inputInventory"));
        this.outputItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_syrup_extractor.outputInventory"));
        this.patternItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_syrup_extractor.patternInventory"));
        this.progress = pTag.getInt("clockwork_syrup_extractor.progress");
        this.maxProgress = pTag.getInt("clockwork_syrup_extractor.max_progress");
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
        inserted.applyComponents(item.getComponents());
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
                return true;
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
                                if (!remainingStack.isEmpty()){
                                    outputRemainder(remainingStack, stack.getCount() - remainder);
                                }
                                stack.setCount(remainder);
                                break;
                            } else {
                                total -= stack.getCount();
                                if (!remainingStack.isEmpty()){
                                    outputRemainder(remainingStack, stack.getCount());
                                }
                                stack.setCount(0);
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
                break;
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

    public Optional<RecipeHolder<CraftingRecipe>> getCurrentRecipe() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < patternItemHandler.getSlots(); i++) {
            items.add(patternItemHandler.getStackInSlot(i));
        }
        CraftingInput input = CraftingInput.of(3, 3, items);

        return this.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
    }

    public boolean canCraft(Level level) {
        Optional<RecipeHolder<CraftingRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            if (resultItemHandler.getStackInSlot(0).getCount() > 0 || !resultItemHandler.getStackInSlot(0).isEmpty()){
                resultItemHandler.setStackInSlot(0, ItemStack.EMPTY);
            }
            return false;
        }

        ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
        resultItemHandler.setStackInSlot(0, new ItemStack(result.getItem()));

        return isOutputClear(result) && hasInputStacks(recipe.get().value().getIngredients());
    }

    public void craft(Level level) {
        Optional<RecipeHolder<CraftingRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
        outputItems(new ItemStack(result.getItem(), result.getCount()));
        takeFromInputs(recipe.get().value().getIngredients());
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
        } else {
            this.canCraft(level);
        }
    }

    @Override
    public int getWindupWeight() {
        return 1;
    }
}
