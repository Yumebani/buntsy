package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyInfusionBenchBlockEntity;
import net.sophiebun.buntsy.recipe.FairyOfferingRecipe;
import net.sophiebun.buntsy.screen.clockwork.ClockworkFairyTerminalMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class ClockworkFairyTerminalEntity extends WindupClockworkEntity implements MenuProvider, GeoBlockEntity {

    private boolean isWatched = false;
    private boolean isEnchanted = false;
    private int speedUp = 1;
    private float consumption;

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private final ItemStackHandler outputItemHandler = new ItemStackHandler(4) {
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
    private static final int FAIRY_FOOD_OUTPUT_SLOT_START = 0;
    private static final int FAIRY_FOOD_OUTPUT_SLOT_COUNT = 4;

    public final ContainerData data;

    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public ClockworkFairyTerminalEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_FAIRY_TERMINAL_ENTITY.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ClockworkFairyTerminalEntity.this.isEnchanted() ? 1 : 0;
                    case 1 -> ClockworkFairyTerminalEntity.this.isWoundUp() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> ClockworkFairyTerminalEntity.this.setEnchanted(i1 == 1);
                }
                ;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
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

    @Override
    public int getWindupWeight() {
        return 2;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_fairy_terminal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ClockworkFairyTerminalMenu(i, inventory, this, this.data);
    }

    public boolean hasFood() {
        return getFirstValidInputSlot() != null;
    }

    public Optional<FairyOfferingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(FAIRY_FOOD_SLOT_COUNT + FAIRY_FOOD_OUTPUT_SLOT_COUNT);
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < FAIRY_FOOD_SLOT_COUNT; i++) {
            inventory.setItem(i + FAIRY_FOOD_OUTPUT_SLOT_COUNT, outputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(FairyOfferingRecipe.Type.INSTANCE, inventory, level);
    }

    public void consumeFood() {
        outputFoodItem(getCurrentRecipe().get().getResultItem(null));
        int slot = getFirstValidInputSlot();
        this.inputItemHandler.extractItem(slot, 1, false);
    }

    private void outputFoodItem(ItemStack outputItem){
        if (!outputItem.is(Blocks.AIR.asItem())){
            int slot = getClearOutput(outputItem);
            this.outputItemHandler.setStackInSlot(slot,
                    new ItemStack(outputItem.getItem(),this.outputItemHandler.getStackInSlot(slot).getCount() + 1));
        }
    }

    private Integer getFirstValidInputSlot(){
        List<Integer> values = getFilledInputSlotList().stream().filter((value) -> isOutputClear()).toList();
        return values.isEmpty() ? null : values.get(0);
    }

    private List<Integer> getFilledInputSlotList() {

        List<Integer> slots = new ArrayList<>();
        for (int i = FAIRY_FOOD_SLOT_START; i < FAIRY_FOOD_SLOT_START + FAIRY_FOOD_SLOT_COUNT; i++){
            if (!this.inputItemHandler.getStackInSlot(i).isEmpty() && this.inputItemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private List<Integer> getFilledOutputSlotList() {

        List<Integer> slots = new ArrayList<>();
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
            if (!this.outputItemHandler.getStackInSlot(i).isEmpty() && this.outputItemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private boolean isOutputClear() {
        ItemStack output = getCurrentRecipe().get().getResultItem(null);
        return output.is(Blocks.AIR.asItem()) || getClearOutput(output) != null;
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
            if ((outputItemHandler.getStackInSlot(i).isEmpty() || this.outputItemHandler.getStackInSlot(i).getItem() == result.getItem())
                    && (this.outputItemHandler.getStackInSlot(i).getCount() + result.getCount() <= result.getMaxStackSize())){
                return i;
            }
        }
        return null;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (this.isWoundUp()) {
            this.tickWindup();

            this.generalTick(pLevel);
            this.updateConsumptionRate(pLevel);
            this.updateRegisteredBlocks(pLevel);
            this.fairyEatFromOfferings(pLevel);
            this.taskTick(pLevel);
        }
    }

    public void generalTick(Level level){

        if (this.fumeTickCount <= 0){
            this.fumeTickCount = 20;
            this.tickFumes();
        }
        else {
            this.fumeTickCount--;
        }

        if (this.getFood() <= 0){

            if (!isHungry()){
                setHungry(true);
                setUpdateBlocksFlag(true);
            }
            this.setEnchanted(false);
        }

        if (this.getFood() > 0){
            this.toConsume += this.getConsumptionRate() / (fumes.containsKey(1) ? fumes.get(1).get(0) + 1 : 1);
            this.decrementFood(Mth.ceil(this.toConsume));
            this.toConsume %= 1;
        }
    }

    public void taskTick(Level level){
        if (this.task != null){
            if (!this.task.hasStarted()){
                if (this.task.canUse(level)){
                    this.task.start(level);
                }
            } else if (this.task.canContinueToUse(level)){
                this.task.tick(level);
            }
        }
    }

    public void fairyEatFromOfferings(Level level){

        if (this.getFood() <= 0) {
            for (int i = 0; i < (this.fumes.containsKey(7) ? this.fumes.get(7).get(0) + 1 : 1); i++){
                if (this.hasFood()){
                    FairyOfferingRecipe recipe = this.getCurrentRecipe().get();
                    this.consumeFood();

                    this.addFood(recipe.getFoodTick());
                    this.setFoodModifier(recipe.getChanceModifier());

                    this.setEnchanted(true);

                    if (this.isHungry()) {
                        this.setHungry(false);
                        this.setUpdateBlocksFlag(true);
                    }
                }
            }
        }
    }

    public void updateRegisteredBlocks(Level level){
        if (registeredBlocksRequiresUpdate(level)){
            this.setUpdateBlocksFlag(true);
        }

        if (this.getUpdateBlocksFlag()){
            for (BlockPos pos : this.getRegisteredBlockPosArray()){
                if (level.getBlockEntity(pos) == null || this.currentWeight > this.getMaxFairyWeight()){
                    this.currentWeight -= this.registeredUtilBlockEntityPos.get(pos);
                    this.registeredUtilBlockEntityPos.remove(pos);
                }
                else if (this.currentWeight > this.getMaxFairyWeight()){
                    this.unregisterBlock(level.getBlockEntity(pos), level);
                }
                else if (this.isHungry()){
                    ((FairyInteractBlockEntity) level.getBlockEntity(pos)).setEnchanted(false);
                }
                else{
                    ((FairyInteractBlockEntity) level.getBlockEntity(pos)).setEnchanted(true);

                    if (this.fumes.containsKey(2)){
                        ((FairyInteractBlockEntity) level.getBlockEntity(pos)).setSpeedUp(this.fumes.get(2).get(0) + 1);
                    }
                    else {
                        ((FairyInteractBlockEntity) level.getBlockEntity(pos)).setSpeedUp(1);
                    }
                }
            }

            this.setUpdateBlocksFlag(false);
        }
    }

    public boolean registeredBlocksRequiresUpdate(Level level){

        for (BlockPos pos : this.getRegisteredBlockPosArray()){
            if (level.getBlockEntity(pos) == null){
                return true;
            }
        }

        return false;
    }

    public void updateConsumptionRate(Level level){
        if (level.random.nextInt(5) == 0 && (!this.registeredUtilBlockEntityPos.isEmpty() || this.getConsumptionRate() > this.BASE_CONSUMPTION + 0.1f)){
            float finalConsumption = this.BASE_CONSUMPTION;
            for (BlockPos pos : this.getRegisteredBlockPosArray()){
                if (level.getBlockEntity(pos) != null){
                    finalConsumption += ((FairyInteractBlockEntity) level.getBlockEntity(pos)).getConsumption();
                }
            }
            this.setConsumptionRate(finalConsumption);
        }
    }

    private static final float BASE_CONSUMPTION = 0.01f;
    private static final int FAIRY_TERMINAL_BASE_POOL = 4;
    private static final int FAIRY_TERMINAL_BASE_RANGE = 8;

    private boolean updateBlocksFlag;

    private final Map<BlockPos, Integer> registeredUtilBlockEntityPos = new HashMap<BlockPos, Integer>();
    private int currentWeight;

    private final Map<Integer, List<Integer>> fumes = new HashMap<>();
    private int fumeTickCount = 0;

    private FairyTerminalTask task;

    private float toConsume;
    private int food;
    private boolean hasTitular;
    private BlockPos titularBlockPos;
    private boolean hungry;
    private float foodModifier;
    private float consumptionRate;

    public void drops(Level level){
        SimpleContainer inventory = new SimpleContainer(FAIRY_FOOD_SLOT_COUNT + FAIRY_FOOD_OUTPUT_SLOT_COUNT);
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i + FAIRY_FOOD_SLOT_COUNT, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);

        clearBlockEntityData(level);
    }

    public int getMaxFairyWeight() {
        return FAIRY_TERMINAL_BASE_POOL + (fumes.containsKey(1) ? fumes.get(1).get(0) : 0);
    }

    public void setUpdateBlocksFlag(boolean updateBlocksFlag) {
        this.updateBlocksFlag = updateBlocksFlag;
    }

    public boolean getUpdateBlocksFlag() {
        return updateBlocksFlag;
    }

    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }

    public boolean isHungry() {
        return hungry;
    }

    public void addFood(int food) {
        this.food += food;
    }

    public int getFood() {
        return food;
    }

    public void decrementFood(int food){
        this.food -= food;
    }

    public void setFoodModifier(float foodModifier) {
        this.foodModifier = foodModifier;
    }

    public float getFoodModifier() {
        return foodModifier;
    }

    public float getConsumptionRate() {
        return (consumptionRate * (fumes.containsKey(2) ? fumes.get(2).get(0) + 1 : 1)) / (fumes.containsKey(1) ? fumes.get(1).get(0) + 1 : 1);
    }

    public void setConsumptionRate(float consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public List<BlockPos> getRegisteredBlockPosArray(){
        return this.registeredUtilBlockEntityPos.keySet().stream().toList();
    }

    public void clearBlockEntityData(Level level){
        if (this.registeredUtilBlockEntityPos.size() != 0){
            for (BlockPos pos : getRegisteredBlockPosArray()){
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null){
                    unregisterBlock(blockEntity, level);
                }
            }
            this.registeredUtilBlockEntityPos.clear();
        }

        this.setUpdateBlocksFlag(true);
    }

    public int canRegisterNewBlock(BlockEntity blockEntity){
        if (((FairyInteractBlockEntity) blockEntity).isTitular() && hasTitular){
            return -2;
        } else if (((FairyInteractBlockEntity) blockEntity).getFairyWeight() + this.currentWeight <= this.getMaxFairyWeight()){
            return 0;
        } else {
            return -1;
        }
    }

    public boolean isBlockRegistered(BlockEntity blockEntity){
        for (BlockPos pos : getRegisteredBlockPosArray()){
            if (pos != null && pos.equals(blockEntity.getBlockPos())){
                return true;
            }
        }
        return false;
    }

    public void registerNewBlock(BlockEntity blockEntity){
        FairyInteractBlockEntity entity = (FairyInteractBlockEntity) blockEntity;
        entity.setWatched(true);
        if (entity.isTitular()){
            this.hasTitular = true;
            titularBlockPos = blockEntity.getBlockPos();
            if (entity instanceof FairyCollectionTrayBlockEntity){
                task = new FairyCollectResources(this, entity.getBlockPos(), 55);
            } else if (entity instanceof FairyInfusionBenchBlockEntity){
                task = new FairyEnchantResources(this, titularBlockPos, 30);
            }
        }
        int fairyWeight = entity.getFairyWeight();
        this.currentWeight += fairyWeight;
        this.registeredUtilBlockEntityPos.put(blockEntity.getBlockPos(), fairyWeight);
        this.setUpdateBlocksFlag(true);
    }

    public void unregisterBlock(BlockEntity blockEntity, Level level){
        for (BlockPos pos : getRegisteredBlockPosArray()){
            if (pos != null && pos.equals(blockEntity.getBlockPos())){
                FairyInteractBlockEntity entity = (FairyInteractBlockEntity) level.getBlockEntity(pos);
                if (entity.isTitular()){
                    this.hasTitular = false;
                    this.titularBlockPos = null;
                    this.task = null;
                }
                entity.setWatched(false);
                entity.setEnchanted(false);
                this.currentWeight -= this.registeredUtilBlockEntityPos.get(pos);
                this.registeredUtilBlockEntityPos.remove(pos);
                this.setUpdateBlocksFlag(true);
            }
        }
    }

    @Override
    public void load( CompoundTag pCompound) {
        super.load(pCompound);

        this.consumption = pCompound.getFloat("clockwork_fairy_terminal.consumption");
        this.speedUp = pCompound.getInt("clockwork_fairy_terminal.speedUp");
        this.isEnchanted = pCompound.getBoolean("clockwork_fairy_terminal.is_enchanted");
        this.isWatched = pCompound.getBoolean("clockwork_fairy_terminal.is_watched");

        this.inputItemHandler.deserializeNBT(pCompound.getCompound("clockwork_fairy_terminal.inputInventory"));
        this.outputItemHandler.deserializeNBT(pCompound.getCompound("clockwork_fairy_terminal.outputInventory"));

        this.hasTitular = pCompound.getBoolean("clockwork_fairy_terminal.has_titular");
        if (hasTitular){
            titularBlockPos = NbtUtils.readBlockPos(pCompound.getCompound("clockwork_fairy_terminal.titular_block_pos"));
            task = (pCompound.getString("clockwork_fairy_terminal.titular_task")).equals("collect") ?
                    new FairyCollectResources(this, titularBlockPos, 55) : new FairyEnchantResources(this, titularBlockPos, 30);
        }

        this.currentWeight = pCompound.getInt("clockwork_fairy_terminal.current_weight");
        this.food = pCompound.getInt("clockwork_fairy_terminal.food");
        this.foodModifier = pCompound.getFloat("clockwork_fairy_terminal.food_modifier");

        int loopCount = pCompound.getInt("clockwork_fairy_terminal.registered_block_entity_count");

        for (int i = 0; i < loopCount; i++){
            this.registeredUtilBlockEntityPos.put(
                    NbtUtils.readBlockPos(pCompound.getCompound("clockwork_fairy_terminal.registered_block_entity_pos_" + i)),
                    pCompound.getInt("clockwork_fairy_terminal.registered_block_entity_weight_" + i));
        }

        int fumeCount = pCompound.getInt("clockwork_fairy_terminal.applied_fume_count");

        for (int i = 0; i < fumeCount; i++){
            List<Integer> list = new ArrayList<>();
            list.add(pCompound.getInt("clockwork_fairy_terminal.applied_fume_level_" + i));
            list.add(pCompound.getInt("clockwork_fairy_terminal.applied_fume_duration_" + i));
            this.fumes.put(
                    pCompound.getInt("clockwork_fairy_terminal.applied_fume_type_" + i), list);
        }

        this.fumeTickCount = pCompound.getInt("clockwork_fairy_terminal.fume_tick_count");


    }

    @Override
    public void saveAdditional(CompoundTag pCompound) {

        pCompound.putFloat("clockwork_fairy_terminal.consumption", this.consumption);
        pCompound.putInt("clockwork_fairy_terminal.speedUp", this.speedUp);
        pCompound.putBoolean("clockwork_fairy_terminal.is_enchanted", this.isEnchanted);
        pCompound.putBoolean("clockwork_fairy_terminal.is_watched", this.isWatched);

        pCompound.put("clockwork_fairy_terminal.inputInventory", inputItemHandler.serializeNBT());
        pCompound.put("clockwork_fairy_terminal.outputInventory", outputItemHandler.serializeNBT());

        pCompound.putBoolean("clockwork_fairy_terminal.has_titular", hasTitular);
        if (hasTitular){
            String titularTask = task instanceof FairyCollectResources ? "collect" : "enchant";
            pCompound.put("clockwork_fairy_terminal.titular_block_pos", NbtUtils.writeBlockPos(titularBlockPos));
            pCompound.putString("clockwork_fairy_terminal.titular_task", titularTask);
        }

        pCompound.putInt("clockwork_fairy_terminal.current_weight", this.currentWeight);
        pCompound.putInt("clockwork_fairy_terminal.food", this.food);
        pCompound.putFloat("clockwork_fairy_terminal.food_modifier", this.foodModifier);

        pCompound.putInt("clockwork_fairy_terminal.registered_block_entity_count", this.registeredUtilBlockEntityPos.size());

        for (int i = 0; i < this.registeredUtilBlockEntityPos.size(); i++){
            BlockPos pos = (BlockPos) this.registeredUtilBlockEntityPos.keySet().toArray()[i];
            pCompound.put("clockwork_fairy_terminal.registered_block_entity_pos_" + i, NbtUtils.writeBlockPos(pos));
            pCompound.putInt("clockwork_fairy_terminal.registered_block_entity_weight_" + i, this.registeredUtilBlockEntityPos.get(pos));
        }

        pCompound.putInt("clockwork_fairy_terminal.applied_fume_count", this.fumes.size());
        if (!this.fumes.keySet().isEmpty()){
            List<Integer> keys = this.fumes.keySet().stream().toList();

            for (int i = 0; i < this.fumes.size(); i++){
                pCompound.putInt("clockwork_fairy_terminal.applied_fume_type_" + i, keys.get(i));
                pCompound.putInt("clockwork_fairy_terminal.applied_fume_level_" + i, this.fumes.get(keys.get(i)).get(0));
                pCompound.putInt("clockwork_fairy_terminal.applied_fume_duration_" + i, this.fumes.get(keys.get(i)).get(1));
            }
        }

        pCompound.putInt("clockwork_fairy_terminal.fume_tick_count", this.fumeTickCount);

        super.saveAdditional(pCompound);
    }

    public void addFume(int fumeType, int level, int duration){
        List<Integer> list = new ArrayList<>();
        list.add(level);
        list.add(duration);
        fumes.put(fumeType, list);
        setUpdateBlocksFlag(true);
    }

    public void tickFumes(){
        for (Integer key : fumes.keySet().stream().toList()){
            List<Integer> list = fumes.get(key);
            list.set(1, list.get(1) - 1);
            if (list.get(1) <= 0){
                fumes.remove(key);
                setUpdateBlocksFlag(true);
            }
        }
    }

    public boolean isEnchanted() {
        return isEnchanted;
    }

    public void setEnchanted(boolean enchanted) {
        isEnchanted = enchanted;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public boolean isBlockEntityInRange(BlockEntity blockEntity) {
        return isInRange(blockEntity.getBlockPos());
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<ClockworkFairyTerminalEntity> controller;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controllers.add(controller);
    }

    private PlayState predicate(AnimationState<ClockworkFairyTerminalEntity> clockworkFairyTerminalEntityAnimationState) {
        clockworkFairyTerminalEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.clockwork_fairy_terminal.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private interface FairyTerminalTask {
        boolean canUse(Level level);
        boolean canContinueToUse(Level level);
        void start(Level level);
        void tick(Level level);
        boolean hasStarted();
    }

    private class FairyEnchantResources implements FairyTerminalTask {
        protected final ClockworkFairyTerminalEntity entity;

        private final int USE_TIME;

        private boolean hasStarted;

        private int usageTime;
        protected BlockPos infusionBench;

        private float nextUseTicks = 0;

        public FairyEnchantResources(ClockworkFairyTerminalEntity entity, BlockPos infusionBench, int useTime) {
            this.infusionBench = infusionBench;
            this.USE_TIME = useTime;
            this.entity = entity;
        }

        @Override
        public boolean canUse(Level level) {
            if (nextUseTicks > 0){
                nextUseTicks -= entity.foodModifier * (entity.fumes.containsKey(2) ? entity.fumes.get(2).get(0) + 1 : 1);
            }
            else if (this.entity.getFood() > 0 && level.isLoaded(infusionBench)
                    && ((FairyInfusionBenchBlockEntity) level.getBlockEntity(infusionBench)).hasInfusion()) {
                return true;
            } else {
                nextUseTicks = 20;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse(Level level) {
            return entity.getFood() > 0 && level.isLoaded(infusionBench);
        }

        @Override
        public void start(Level level) {
            this.hasStarted = true;
            this.usageTime = 0;
            this.nextUseTicks = 150 + level.random.nextInt(50);
        }

        protected void playUseParticle(Level level) {
            this.makeSparkParticle(level);
        }

        protected void makeSparkParticle(Level level){
            ((ServerLevel) level).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, infusionBench.getX(), infusionBench.getY(), infusionBench.getZ(), level.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void playUseSound(Level level) {
            level.playSound(null, infusionBench, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        protected void playFinishUseParticle(Level level) {
            this.makeSparkParticle(level);
        }

        protected void playFinishUseSound(Level level) {
            level.playSound(null, infusionBench, SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        protected void makeSparkleTrail(Level level){
            Vec3 pos1 = entity.getBlockPos().getCenter();
            Vec3 pos2 = infusionBench.getCenter();

            double delta = 0.2f / pos2.subtract(pos1).length();
            double accumulator = 0;
            while (accumulator < 1){
                Vec3 lerped = pos1.lerp(pos2, accumulator);
                ((ServerLevel) level).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, lerped.x, lerped.y, lerped.z, 3,
                        0.25f, 0.25f, 0.25f, 0.1f);
                accumulator += delta;
            }
        }

        protected void useBlock(Level level) {
            FairyInfusionBenchBlockEntity bench = ((FairyInfusionBenchBlockEntity) level.getBlockEntity(infusionBench));
            for (int i = 0; i < level.random.nextInt(1,3); i++){
                if (bench.hasInfusion()){
                    bench.infuse();
                }
            }
            this.playFinishUseParticle(level);
            this.playFinishUseSound(level);
        }

        @Override
        public void tick(Level level) {
            if (this.usageTime < this.USE_TIME) {
                if (this.usageTime % 15 == 0) {
                    makeSparkleTrail(level);
                    this.playUseParticle(level);
                    this.playUseSound(level);
                }
                this.usageTime++;
            }
            else if (this.usageTime >= this.USE_TIME) {
                makeSparkleTrail(level);
                makeSparkParticle(level);
                this.useBlock(level);
                this.hasStarted = false;
            }
        }

        @Override
        public boolean hasStarted() {
            return hasStarted;
        }
    }

    private class FairyCollectResources implements FairyTerminalTask {

        private final ClockworkFairyTerminalEntity entity;

        protected static final TagKey<Block> VALID_BLOCK_TAG = ModTags.Blocks.FAIRY_HARVESTABLE;
        private final int COLLECT_TIME;

        private int collectionTime;
        private BlockPos targetTray;
        private int nextStartTick = 100;

        private boolean hasStarted = false;

        private BlockPos blockPos;

        public FairyCollectResources(ClockworkFairyTerminalEntity entity, BlockPos targetTray, int collectTime) {
            this.targetTray = targetTray;
            this.entity = entity;
            this.COLLECT_TIME = collectTime;
        }

        @Override
        public boolean hasStarted(){
            return hasStarted;
        }

        @Override
        public boolean canUse(Level level) {
            if (!level.isLoaded(targetTray)) return false;

            if (this.nextStartTick > 0){
                this.nextStartTick -= (int) Math.round(Math.ceil(entity.foodModifier * (entity.fumes.containsKey(2) ? entity.fumes.get(2).get(0) + 1 : 1)));
            }
            else if (entity.getFood() > 0 && !entity.registeredUtilBlockEntityPos.isEmpty()) {
                if (this.findNearestBlock(level)) {
                    return true;
                }
                else{
                    this.nextStartTick = 50 + level.random.nextInt(50);
                }
            }
            return false;
        }

        public boolean findNearestBlock(Level level){
            int maxDistance = getMaxDistance();
            for (BlockPos pos : BlockPos.withinManhattan(entity.getBlockPos(), maxDistance, maxDistance, maxDistance)){
                if (isValidTarget(level, pos)){
                    this.blockPos = pos;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse(Level level) {
            return (level.isLoaded(targetTray) && entity.getFood() > 0 && hasStarted && isValidTarget(level, blockPos));
        }

        @Override
        public void start(Level level) {
            this.nextStartTick = 50 + level.random.nextInt(50);
            this.collectionTime = 0;
            hasStarted = true;
        }

        protected void playMineralMiningSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        protected void playMineralBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        protected void playFlowerCopySound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        protected void playPlantBreakingSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.NEUTRAL,
                    0.8f, level.getRandom().nextInt(8,12) * 0.1f);
        }

        protected void playPlantBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.NEUTRAL,
                    1f, level.getRandom().nextInt(8,12) * 0.1f);
        }


        protected void makeBlockParticle(Level level, BlockState block, Vec3 pos, int count){
            ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block), pos.x, pos.y, pos.z, level.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void makeSparkleParticle(Level level, Vec3 pos, int count){
            ((ServerLevel) level).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y, pos.z, level.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void makeSparkleTrail(Level level, Vec3 pos1, Vec3 pos2){
            double delta = 0.2f / pos2.subtract(pos1).length();
            double accumulator = 0;
            while (accumulator < 1){
                Vec3 lerped = pos1.lerp(pos2, accumulator);
                ((ServerLevel) level).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, lerped.x, lerped.y, lerped.z, 3,
                        0.25f, 0.25f, 0.25f, 0.1f);
                accumulator += delta;
            }
        }

        protected void playStoreSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.NEUTRAL,
                    1f, level.random.nextInt(8,12) * 0.1f);
        }

        @Override
        public void tick(Level level) {
            if (hasStarted){
                collectionTime++;
                if (collectionTime % 10 == 0){
                    BlockState block = level.getBlockState(blockPos);
                    if (block.is(BlockTags.FLOWERS)){
                        makeSparkleParticle(level, blockPos.getCenter(), 3);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }
                    else if (block.getBlock() instanceof CropBlock){
                        playPlantBreakingSound(level, blockPos);
                        makeBlockParticle(level, block, blockPos.getCenter(), 3);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }
                    else{
                        playMineralMiningSound(level, blockPos);
                        makeBlockParticle(level, block, blockPos.getCenter(), 3);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }
                }
                if (collectionTime >= COLLECT_TIME + 5){
                    BlockState block = level.getBlockState(blockPos);
                    if (block.is(BlockTags.FLOWERS)){
                        playFlowerCopySound(level, blockPos);
                        makeSparkleParticle(level, blockPos.getCenter(), 25);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }
                    else if (block.getBlock() instanceof CropBlock){
                        playPlantBreakSound(level, blockPos);
                        makeSparkleParticle(level, blockPos.getCenter(), 25);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }
                    else{
                        playMineralBreakSound(level, blockPos);
                        makeBlockParticle(level, block, blockPos.getCenter(), 25);
                        makeSparkleTrail(level, entity.getBlockPos().getCenter(), blockPos.getCenter());
                    }

                    collectFromBlock(level);
                    hasStarted = false;
                }
            }
        }

        protected void collectFromBlock(Level level){

            BlockState blockState = level.getBlockState(this.blockPos);

            if (blockState.is(BlockTags.FLOWERS)){
                makeSparkleTrail(level, entity.getBlockPos().getCenter(), targetTray.getCenter().add(0, 0.5f, 0));
                makeSparkleParticle(level, targetTray.getCenter().add(0, 0.5f, 0), 25);
                ((FairyCollectionTrayBlockEntity) level.getBlockEntity(targetTray)).depositItem(new ItemStack(blockState.getBlock().asItem()));
            }
            else if (blockState.getBlock() instanceof CropBlock){
                makeSparkleTrail(level, entity.getBlockPos().getCenter(), targetTray.getCenter().add(0, 0.5f, 0));
                makeSparkleParticle(level, targetTray.getCenter().add(0, 0.5f, 0), 25);

                LootTable lootTable = level.getServer().getLootData().getLootTable(blockState.getBlock().getLootTable());
                LootParams emptyParams = new LootParams.Builder(((ServerLevel) level))
                        .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                        .withParameter(LootContextParams.BLOCK_STATE, blockState)
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .create(LootContextParamSets.BLOCK);

                FairyCollectionTrayBlockEntity trayEntity = ((FairyCollectionTrayBlockEntity) level.getBlockEntity(targetTray));
                List<ItemStack> items = lootTable.getRandomItems(emptyParams);

                for (ItemStack itemStack : items){
                    trayEntity.depositItem(itemStack);
                }

                level.setBlock(this.blockPos, blockState.getBlock().defaultBlockState(), 2);
            }
            else {
                LootParams.Builder params = new LootParams.Builder(((ServerLevel) level))
                        .withParameter(LootContextParams.ORIGIN, new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()))
                        .withParameter(LootContextParams.BLOCK_STATE, blockState)
                        .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));


                makeSparkleTrail(level, entity.getBlockPos().getCenter(), targetTray.getCenter().add(0, 0.5f, 0));
                makeSparkleParticle(level, targetTray.getCenter().add(0, 0.5f, 0), 25);
                ((FairyCollectionTrayBlockEntity) level.getBlockEntity(targetTray)).depositItem(
                        new ItemStack(blockState.getDrops(params).get(0).getItem(), level.random.nextInt(2,5)));

                ModGrowableMineral mineral = ((ModGrowableMineral) blockState.getBlock());
                BlockState newBlockState = mineral.getStages().get(level.random.nextInt(2)).get().defaultBlockState();

                level.setBlockAndUpdate(this.blockPos, newBlockState
                        .setValue(AmethystClusterBlock.FACING, blockState.getValue(AmethystClusterBlock.FACING))
                        .setValue(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getType() == Fluids.WATER));
            }

            this.nextStartTick = 50 + level.getRandom().nextInt(50);
        }

        protected boolean isValidTarget(Level level, BlockPos pos) {
            BlockState blockState = level.getBlockState(pos);
            if (isInRange(pos)){
                if (blockState.is(VALID_BLOCK_TAG)){
                    if (blockState.is(BlockTags.FLOWERS)){
                        return level.getRandom().nextInt(32) == 0;
                    }
                    return true;
                } else if (blockState.getBlock() instanceof CropBlock){
                    return ((CropBlock) blockState.getBlock()).isMaxAge(blockState);
                }
            }
            return false;
        }
    }

    private boolean isInRange(BlockPos blockPos) {
        Vec3 distance = this.getBlockPos().getCenter().subtract(blockPos.getCenter());
        int maxDistance = getMaxDistance();
        return distance.x() < maxDistance && distance.y() < maxDistance && distance.z() < maxDistance;
    }

    protected int getMaxDistance(){
        return FAIRY_TERMINAL_BASE_RANGE + getClockworkRangeAmount();
    }

    protected int getClockworkRangeAmount() {
        return switch (clockworkTier){
            case NONE -> 0;
            case SIMPLE -> 2;
            case INTRICATE -> 4;
            case COMPLEX -> 8;
        };
    }
}
