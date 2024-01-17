package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.screen.FairyTerrariumMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class FairyCollectionTrayBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int OUTPUT_SLOT_START = 0;
    private static final int OUTPUT_SLOT_COUNT = 15;

    protected final ContainerData data;
    private int food = 0;
    private int maxFood = 100;
    private float chanceModifier = 0;
    private boolean flowerValid = false;
    private int ticksPassedSinceCheck = 0;

    private static final float BASE_CHANCE = 0.0025f;
    private static final float INFUSION_CHANCE = 0.5f;
    private static final int VALID_FLOWER_COUNT = 16;
    private static final int FLOWER_CHECK_TICKS = 30;

    private static final Random random = new Random();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public FairyCollectionTrayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAIRY_FLOWER_TRAY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.food;
                    case 1 -> FairyCollectionTrayBlockEntity.this.maxFood;
                    case 2 -> Math.round(FairyCollectionTrayBlockEntity.this.chanceModifier * 100);
                    case 3 -> FairyCollectionTrayBlockEntity.this.flowerValid ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.food = i1;
                    case 1 -> FairyCollectionTrayBlockEntity.this.maxFood = i1;
                    case 2 -> FairyCollectionTrayBlockEntity.this.chanceModifier = i1 / 100f;
                    case 3 -> FairyCollectionTrayBlockEntity.this.flowerValid = i1 == 1;
                };
            }

            @Override
            public int getCount() {
                return 3;
            }

            public float getFloat(int i) {
                return switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.chanceModifier;
                    default -> 0;
                };
            }

            public void setFloat(int i, float i1) {
                switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.chanceModifier = i1;
                };
            }

            public int getFloatCount() {
                return 1;
            }

            public boolean getBool(int i) {
                return switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.flowerValid;
                    default -> false;
                };
            }

            public void setBool(int i, boolean i1) {
                switch(i) {
                    case 0 -> FairyCollectionTrayBlockEntity.this.flowerValid = i1;
                };
            }

            public int getBoolCount() {
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
        return Component.translatable("block.buntsy.fairy_terrarium");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyTerrariumMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("fairy_terrarium.food", this.food);
        pTag.putInt("fairy_terrarium.max_food", this.maxFood);
        pTag.putFloat("fairy_terrarium.chance_modifier", this.chanceModifier);
        pTag.putBoolean("fairy_terrarium.flower_valid", this.flowerValid);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.food = pTag.getInt("fairy_terrarium.food");
        this.maxFood = pTag.getInt("fairy_terrarium.max_food");
        this.chanceModifier = pTag.getFloat("fairy_terrarium.chance_modifier");
        this.flowerValid = pTag.getBoolean("fairy_terrarium.flower_valid");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        increaseTicksPassed();

        if (timeForCheck()){
            checkHasFlowers(pLevel, pPos);
        }

        if (hasFairy() && checkFlowerValid()){
            if (!hasFood()){
                if (hasFoodItem() && foodHasOutput()){
                    consumeFood();
                    tick(pLevel, pPos, pState);
                }
            }
            else {
                decreaseFood();

                if (rollProduce()){
                    if (tempHasInfusion() && rollInfuse()){
                        tempInfuseItem();
                    }

                    tryMakeFlower(pLevel, pPos);
                }
            }
        }
    }

    private boolean rollInfuse() {
        return random.nextFloat(0f, 1f) <= INFUSION_CHANCE;
    }

    private boolean rollProduce() {
        return random.nextFloat(0f, 1f) <= BASE_CHANCE * this.chanceModifier;
    }

    private boolean checkFlowerValid() {
        return this.flowerValid;
    }

    private void checkHasFlowers(Level pLevel, BlockPos pPos) {
        this.ticksPassedSinceCheck = 0;
        this.flowerValid = hasFlowers(pLevel, pPos);
    }

    private boolean timeForCheck() {
        return this.ticksPassedSinceCheck >= FLOWER_CHECK_TICKS;
    }

    private void increaseTicksPassed() {
        this.ticksPassedSinceCheck++;
    }

    private void tryMakeFlower(Level pLevel, BlockPos pPos) {
        List<Item> flowers = getFlowerList(pLevel, pPos);
        ItemStack result = new ItemStack(flowers.get(random.nextInt(flowers.size())));
        Integer outputSlot = getClearOutput(result);

        if (outputSlot != null){
            this.itemHandler.setStackInSlot(outputSlot,
                    new ItemStack(result.getItem(),
                            this.itemHandler.getStackInSlot(outputSlot).getCount() + result.getCount()));
        }
    }

    private boolean hasFlowers(Level pLevel, BlockPos pPos) {
        return getFlowerList(pLevel, pPos).size() >= VALID_FLOWER_COUNT;
    }

    private List<Item> getFlowerList(Level pLevel, BlockPos pPos){
        List<Item> flowers = new ArrayList<>();

        for (int x = pPos.getX() + 4; x >= pPos.getX() - 4; x--){
            for (int z = pPos.getZ() + 4; z >= pPos.getZ() - 4; z--){
                for (int y = pPos.getY() + 2; y >= pPos.getY() - 2; y--){

                    BlockState block = pLevel.getBlockState(new BlockPos(x, y, z));
                    if (block.is(BlockTags.FLOWERS)){
                        flowers.add(block.getBlock().asItem());
                    }

                }
            }
        }

        return flowers;
    }

    private boolean hasFairy() {
        return !this.itemHandler.getStackInSlot(FAIRY_SLOT).isEmpty();
    }

    private void consumeFood() {
        Item foodItem = this.itemHandler.getStackInSlot(FAIRY_FOOD_SLOT).getItem();

        setFoodTick(foodItem);
        setChanceModifier(foodItem);

        outputFoodItem();
        this.itemHandler.extractItem(FAIRY_FOOD_SLOT, 1, false);
    }

    private void setChanceModifier(Item item) {
        float chanceMod = chanceMaps.containsKey(item) ? chanceMaps.get(item) : ((FairyFoodItem) item).getTerrariumChanceMult();
        this.chanceModifier = chanceMod;
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
            return isOutputClearSpecific(itemToPlace, FAIRY_FOOD_OUTPUT_SLOT);
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

    private boolean isOutputClearSpecific(ItemStack result, int slot) {
        return (this.itemHandler.getStackInSlot(slot).isEmpty() || this.itemHandler.getStackInSlot(slot).getItem() == result.getItem())
                && (this.itemHandler.getStackInSlot(slot).getCount() + result.getCount() <= result.getMaxStackSize());
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

    private void tempInfuseItem() {
        int slot = getRandomFilledInputSlot();
        TempRecipe recipe = tempGetCurrentInfusion(slot);
        ItemStack result = recipe.getResult();
        int outputSlot = getClearOutput(result);

        this.itemHandler.extractItem(slot, 1, false);

        this.itemHandler.setStackInSlot(outputSlot,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(outputSlot).getCount() + result.getCount()));
    }

    private boolean tempHasInfusion() {
        TempRecipe recipe = tempGetCurrentInfusion(getRandomFilledInputSlot());
        if (recipe == null){
            return false;
        }

        ItemStack result = recipe.getResult();
        return isOutputClear(result);
    }

    private TempRecipe tempGetCurrentInfusion(int slot) {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        for (TempRecipe recipe : infusionRecipeList){
            if (recipe.isPresent(inventory, new int[]{slot})){
                return recipe;
            }
        }

        return null;
    }

    private int getRandomFilledInputSlot() {
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = INFUSION_SLOT_START; i < INFUSION_SLOT_START + INFUSION_SLOT_COUNT; i++){
            if (!this.itemHandler.getStackInSlot(i).isEmpty()){
                slotList.add(i);
            }
        }
        return slotList.isEmpty() ? 3 : slotList.get(random.nextInt(slotList.size()));
    }

    private void craftItem() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(OUTPUT_SLOT_START, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT_START,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT_START).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            System.out.println("no recipe detected");
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);

        return isOutputClear(result);
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


    private Optional<GrindingWheelRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GrindingWheelRecipe.Type.INSTANCE, inventory, level);
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

    public List<String> maisFrequentes (Stream<String> stream, int i){
        Map<String, Integer> map = new HashMap<>();
        stream.map(String::toLowerCase)
                .map((y) -> map.containsKey(y) ? map.put(y, map.get(y) + 1) : map.put(y, 1));
        return map.entrySet().stream().limit(i).sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();
    }
}