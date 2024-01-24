package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.screen.FairyOfferingBenchMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FairyOfferingBenchBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int FAIRY_FOOD_SLOT_START = 0;
    private static final int FAIRY_FOOD_SLOT_COUNT = 4;
    private static final int FAIRY_FOOD_OUTPUT_SLOT_START = 4;
    private static final int FAIRY_FOOD_OUTPUT_SLOT_COUNT = 4;

    private static final Map<Item, Integer> foodMaps = Map.of(
            Items.SUGAR, 400,
            Items.HONEY_BOTTLE, 800
    );

    private static final Map<Item, Float> chanceMaps = Map.of(
            Items.SUGAR, 0.5f,
            Items.HONEY_BOTTLE, 1f
    );

    public final ContainerData data;
    private final List<Integer> randomRotations;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public List<Integer> getRandomRotations() {
        return randomRotations;
    }

    public FairyOfferingBenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get(), pPos, pBlockState);

        Random random = new Random();

        randomRotations = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            randomRotations.add(random.nextInt(0, 270));
        }

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> FairyOfferingBenchBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> FairyOfferingBenchBlockEntity.this.setEnchanted(i1 == 1);
                }
                ;
            }

            @Override
            public int getCount() {
                return 1;
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
        return Component.translatable("block.buntsy.fairy_offering_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyOfferingBenchMenu(i, inventory, this, this.data);
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

    public List<ItemStack> getRenderItems(){

        List<Integer> inputs = getFilledInputSlotList();
        List<Integer> outputs = getFilledOutputSlotList();

        List<ItemStack> renderItems = new ArrayList<>();

        for (Integer i : inputs){
            if (renderItems.size() < 4){
                renderItems.add(this.itemHandler.getStackInSlot(i));
            }
        }

        for (Integer i : outputs){
            if (renderItems.size() < 4){
                renderItems.add(this.itemHandler.getStackInSlot(i));
            }
        }

        return renderItems;
    }

    public boolean hasFood() {
        return getFirstValidInputSlot() != null;
    }

    public Item getNextFoodItem(){
        int slot = getFirstValidInputSlot();
        Item foodItem = this.itemHandler.getStackInSlot(slot).getItem();
        return foodItem;
    }

    public void consumeFood() {
        int slot = getFirstValidInputSlot();
        Item foodItem = this.itemHandler.getStackInSlot(slot).getItem();

        outputFoodItem(new ItemStack(foodItem));
        this.itemHandler.extractItem(slot, 1, false);
    }

    public static float getChanceModifier(Item item) {
        return chanceMaps.containsKey(item) ? chanceMaps.get(item) : ((FairyFoodItem) item).getTerrariumChanceMult();
    }

    public static int getFoodTick(Item item) {
        return foodMaps.containsKey(item) ? foodMaps.get(item) : ((FairyFoodItem) item).getFoodTick();
    }

    private ItemStack getFoodItemToOutput(ItemStack consumed){
        return consumed.is(ModTags.Items.BOTTLED_ITEM) ?
                new ItemStack(Items.GLASS_BOTTLE) : consumed.is(ModTags.Items.BOWL_ITEM) ?
                new ItemStack(Items.BOWL) : null;
    }

    private void outputFoodItem(ItemStack consumed){
        ItemStack outputItem = getFoodItemToOutput(consumed);
        if (outputItem != null){
            int slot = getClearOutput(outputItem);
            this.itemHandler.setStackInSlot(slot,
                    new ItemStack(outputItem.getItem(),this.itemHandler.getStackInSlot(slot).getCount() + 1));
        }
    }

    private Integer getFirstValidInputSlot(){
        List<Integer> values = getFilledInputSlotList().stream().filter((value) -> isOutputClear(this.itemHandler.getStackInSlot(value))).toList();
        return values.isEmpty() ? null : values.get(0);
    }

    private List<Integer> getFilledInputSlotList() {

        List<Integer> slots = new ArrayList<Integer>();
        for (int i = FAIRY_FOOD_SLOT_START; i < FAIRY_FOOD_SLOT_START + FAIRY_FOOD_SLOT_COUNT; i++){
            if (!this.itemHandler.getStackInSlot(i).isEmpty() && this.itemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private List<Integer> getFilledOutputSlotList() {

        List<Integer> slots = new ArrayList<Integer>();
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
            if (!this.itemHandler.getStackInSlot(i).isEmpty() && this.itemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private boolean isOutputClear(ItemStack consumed) {
        ItemStack output = getFoodItemToOutput(consumed);
        return output == null ? true : getClearOutput(output) != null;
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
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