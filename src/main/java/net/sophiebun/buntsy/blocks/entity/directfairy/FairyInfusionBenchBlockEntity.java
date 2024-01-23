package net.sophiebun.buntsy.blocks.entity.directfairy;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.screen.FairyInfusionBenchMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FairyInfusionBenchBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT_START = 0;
    private static final int INPUT_SLOT_COUNT = 5;
    private static final int OUTPUT_SLOT_START = 5;
    private static final int OUTPUT_SLOT_COUNT = 5;

    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(ModItems.AMETHYST_DUST.get()), Map.of(
                    ModItems.FAIRY_DUST.get(), Map.of(1, 1f))));

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public FairyInfusionBenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(1.25f);
    }

    @Override
    public int getFairyWeight() {
        return 2;
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
        return Component.translatable("block.buntsy.fairy_infusion_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyInfusionBenchMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    public void infuse() {
        int slot = getRandomFilledInputSlot();
        TempRecipe recipe = tempGetCurrentInfusion(slot);
        ItemStack result = recipe.getResults(100).get(0);
        int outputSlot = getClearOutput(result);

        this.itemHandler.extractItem(slot, 1, false);

        this.itemHandler.setStackInSlot(outputSlot,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(outputSlot).getCount() + result.getCount()));
    }

    public boolean hasInfusion() {
        Integer slot = getRandomFilledInputSlot();
        if (slot == null){
            return false;
        }
        TempRecipe recipe = tempGetCurrentInfusion(getRandomFilledInputSlot());
        if (recipe == null){
            return false;
        }

        ItemStack result = recipe.getResults(100).get(0);
        return isOutputClear(result);
    }

    private TempRecipe tempGetCurrentInfusion(int slot) {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        for (TempRecipe recipe : recipeList){
            if (recipe.isPresent(inventory, new int[]{slot})){
                return recipe;
            }
        }

        return null;
    }

    private Integer getRandomFilledInputSlot() {
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = INPUT_SLOT_START; i < INPUT_SLOT_START + INPUT_SLOT_COUNT; i++){
            if (!this.itemHandler.getStackInSlot(i).isEmpty()){
                slotList.add(i);
            }
        }
        return slotList.isEmpty() ? null : slotList.get(level.random.nextInt(slotList.size()));
    }

    private boolean isOutputClear(ItemStack result) {
        return getClearOutput(result) != null;
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; i++){
            if ((itemHandler.getStackInSlot(i).isEmpty() || this.itemHandler.getStackInSlot(i).getItem() == result.getItem())
                    && (this.itemHandler.getStackInSlot(i).getCount() + result.getCount() <= result.getMaxStackSize())){
                return i;
            }
        }
        return null;
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