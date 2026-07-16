package net.sophiebun.buntsy.blocks.entity.advancedfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.interfaces.IFumeAffectedEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.FumeSpreaderMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class FumeSpreaderBlockEntity extends BlockEntity implements MenuProvider {

    private static final Map<Integer, List<Integer>> FUME_CONSUMPTION_DETAILS = Map.of(
            0, List.of(500, 1),
            1, List.of(30, 40),
            2, List.of(30, 40),
            3, List.of(400, 2),
            4, List.of(10, 200),
            5, List.of(10, 200),
            6, List.of(500, 1),
            7, List.of(30, 40),
            8, List.of(30, 40),
            9, List.of(100, 20)
    );

    private static final int RANGE_X = 10;
    private static final int RANGE_Y = 5;
    private static final int RANGE_Y_DOWN = 5;

    private static final int GROWTH_RANGE_X = 4;
    private static final int GROWTH_RANGE_Y = 1;

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ItemStackHandler displayItemHandler = new ItemStackHandler(1) {
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

    protected final ContainerData data;
    private int fumeLeftMax = 0;
    private int fumeLeft = 0;
    private int fumeTick = 0;
    private int fumeTickPeriod = 0;
    private int fumeId = 0;
    private int fumeLevel = 0;

    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> displayLazyItemHandler = LazyOptional.of(() -> displayItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getDisplayLazyItemHandler() {
        return displayLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    public FumeSpreaderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FUME_SPREADER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> FumeSpreaderBlockEntity.this.fumeLeftMax;
                    case 1 -> FumeSpreaderBlockEntity.this.fumeLeft;
                    case 2 -> FumeSpreaderBlockEntity.this.fumeId;
                    case 3 -> FumeSpreaderBlockEntity.this.fumeLevel;
                    case 4 -> FumeSpreaderBlockEntity.this.fumeTick;
                    case 5 -> FumeSpreaderBlockEntity.this.fumeTickPeriod;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> FumeSpreaderBlockEntity.this.fumeLeftMax = i1;
                    case 1 -> FumeSpreaderBlockEntity.this.fumeLeft = i1;
                    case 2 -> FumeSpreaderBlockEntity.this.fumeId = i1;
                    case 3 -> FumeSpreaderBlockEntity.this.fumeLevel = i1;
                    case 4 -> FumeSpreaderBlockEntity.this.fumeTick = i1;
                    case 5 -> FumeSpreaderBlockEntity.this.fumeTickPeriod = i1;
                };
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.fume_spreader");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FumeSpreaderMenu(i, inventory, this, this.data);
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
        displayLazyItemHandler = LazyOptional.of((() -> displayItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        displayLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(2);
        inventory.setItem(0, inputItemHandler.getStackInSlot(0));
        inventory.setItem(1, outputItemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT());
        pTag.put("displayInventory", displayItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("fume_spreader.fumeLeftMax", this.fumeLeftMax);
        pTag.putInt("fume_spreader.fumeLeft", this.fumeLeft);
        pTag.putInt("fume_spreader.fumeId", this.fumeId);
        pTag.putInt("fume_spreader.fumeLevel", this.fumeLevel);
        pTag.putInt("fume_spreader.fumeTick", this.fumeTick);
        pTag.putInt("fume_spreader.fumeTickPeriod", this.fumeTickPeriod);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inputItemHandler.deserializeNBT(pTag.getCompound("inputInventory"));
        this.displayItemHandler.deserializeNBT(pTag.getCompound("displayInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
        this.fumeLeftMax = pTag.getInt("fume_spreader.fumeLeftMax");
        this.fumeLeft = pTag.getInt("fume_spreader.fumeLeft");
        this.fumeId = pTag.getInt("fume_spreader.fumeId");
        this.fumeLevel = pTag.getInt("fume_spreader.fumeLevel");
        this.fumeTick = pTag.getInt("fume_spreader.fumeTick");
        this.fumeTickPeriod = pTag.getInt("fume_spreader.fumeTickPeriod");
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (canRun()){
            if (this.fumeTick <= 0){
                this.fumeTick = this.fumeTickPeriod;
                this.fumeLeft--;

                activateFumeEffect(pLevel, pPos, pState);

            } else this.fumeTick--;
        }
        else {
            if (!displayItemHandler.getStackInSlot(0).isEmpty()){
                displayItemHandler.setStackInSlot(0, ItemStack.EMPTY);
                outputItems();
            }
            if (hasFume()){
                consumeFume();
            }
        }
    }

    private void activateFumeEffect(Level pLevel, BlockPos pPos, BlockState pState) {
        switch (this.fumeId) {
            case 1:
                applyFumeEffectToFairies(pLevel, pPos);
                break;
            case 2:
                applyFumeEffectToFairies(pLevel, pPos);
                break;
            case 3:
                randomTickRandomPlant(pLevel, pPos);
                break;
            case 4:
                changeEntityVariant(pLevel, pPos);
                break;
            case 5:
                turnRandomAgeableToBaby(pLevel, pPos);
                break;
            case 9:
                damageNearbyEntities(pLevel, pPos); //TODO TEST FOR PLAYER DAMAGE
                break;
            case 7:
                applyFumeEffectToFairies(pLevel, pPos);
                break;
            case 8:
                applyFumeEffectToSilkbuns(pLevel, pPos);
                break;


        }
    }

    private void changeEntityVariant(Level pLevel, BlockPos pPos){
        List<LivingEntity> entities = getAnyEntities(pLevel, pPos);
        for (LivingEntity entity : entities){
            if (entity instanceof Cat){
                Cat cat = (Cat)entity;
                setRandomCatVariant(pLevel, cat);
                return;
            }
            else if (entity instanceof Silkbun){
                Silkbun silkbun = (Silkbun)entity;
                silkbun.setVariant(Silkbun.getRandomSilkbunVariant(pLevel, pPos));
                return;
            }
        }
    }

    private void setRandomCatVariant(Level pLevel, Cat cat){
        boolean flag = pLevel.getMoonBrightness() > 0.9F;
        TagKey<CatVariant> tagkey = flag ? CatVariantTags.FULL_MOON_SPAWNS : CatVariantTags.DEFAULT_SPAWNS;
        BuiltInRegistries.CAT_VARIANT.getTag(tagkey).flatMap((p_289435_) -> {
            return p_289435_.getRandomElement(pLevel.getRandom());
        }).ifPresent((p_262565_) -> {
            cat.setVariant(p_262565_.value());
        });
    }

    private void damageNearbyEntities(Level pLevel, BlockPos pPos){
        for (LivingEntity entity : getAnyEntities(pLevel, pPos)){
            if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.GAS_MASK.get())){
                entity.hurt(entity.damageSources().mobAttack(entity), 2 * this.fumeLevel);
            }
        }
    }

    private void turnRandomAgeableToBaby(Level pLevel, BlockPos pPos){
        if (getAgeableMobsInRadius(pLevel, pPos).stream().anyMatch((entity -> !entity.isBaby()))){
            AgeableMob mob = (AgeableMob) getAgeableMobsInRadius(pLevel, pPos).stream().filter((entity) -> !entity.isBaby()).toArray()[0];
            mob.setBaby(true);
            mob.setAge(-24000);
        }
    }

    private void randomTickRandomPlant(Level pLevel, BlockPos pPos){

        BlockPos target = pPos.relative(Direction.Axis.X, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_X, GROWTH_RANGE_X))
                .relative(Direction.Axis.Z, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_X, GROWTH_RANGE_X))
                .relative(Direction.Axis.Y, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_Y, GROWTH_RANGE_Y));

        BlockState state = pLevel.getBlockState(target);

        int checkCount = 3;

        while (!state.is(ModTags.Blocks.FUME_TICKABLE) && checkCount > 0){
            checkCount--;

            target = pPos.relative(Direction.Axis.X, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_X, GROWTH_RANGE_X))
                    .relative(Direction.Axis.Z, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_X, GROWTH_RANGE_X))
                    .relative(Direction.Axis.Y, pLevel.random.nextIntBetweenInclusive(-GROWTH_RANGE_Y, GROWTH_RANGE_Y));

            state = pLevel.getBlockState(target);
        }

        if (state.is(ModTags.Blocks.FUME_TICKABLE)){
            for (int i = 0; i < this.fumeLevel; i++){
                state.randomTick(((ServerLevel) pLevel), target, pLevel.random);
            }
        }
    }

    private void applyFumeEffectToFairies(Level pLevel, BlockPos pPos){

        for (Entity entity : getFairiesInRadius(pLevel, pPos)){
            ((IFumeAffectedEntity)entity).addFume(this.fumeId, this.fumeLevel, 5);
        }
    }
    private void applyFumeEffectToSilkbuns(Level pLevel, BlockPos pPos){

        for (Entity entity : getSilkbunsInRadius(pLevel, pPos)){
            ((IFumeAffectedEntity)entity).addFume(this.fumeId, this.fumeLevel, 5);
        }
    }

    private List<Fairy> getFairiesInRadius (Level pLevel, BlockPos pPos){
        return pLevel.getEntitiesOfClass(Fairy.class,
                new AABB(pPos.relative(Direction.DOWN, RANGE_Y_DOWN).relative(Direction.EAST, RANGE_X).relative(Direction.NORTH, RANGE_X),
                        pPos.relative(Direction.UP, RANGE_Y).relative(Direction.WEST, RANGE_X).relative(Direction.SOUTH, RANGE_X)));
    }

    private List<Silkbun> getSilkbunsInRadius (Level pLevel, BlockPos pPos){
        return pLevel.getEntitiesOfClass(Silkbun.class,
                new AABB(pPos.relative(Direction.DOWN, RANGE_Y_DOWN).relative(Direction.EAST, RANGE_X).relative(Direction.NORTH, RANGE_X),
                        pPos.relative(Direction.UP, RANGE_Y).relative(Direction.WEST, RANGE_X).relative(Direction.SOUTH, RANGE_X)));
    }

    private List<AgeableMob> getAgeableMobsInRadius (Level pLevel, BlockPos pPos){
        return pLevel.getEntitiesOfClass(AgeableMob.class,
                new AABB(pPos.relative(Direction.DOWN, RANGE_Y_DOWN).relative(Direction.EAST, RANGE_X).relative(Direction.NORTH, RANGE_X),
                        pPos.relative(Direction.UP, RANGE_Y).relative(Direction.WEST, RANGE_X).relative(Direction.SOUTH, RANGE_X)));
    }

    private List<LivingEntity> getAnyEntities(Level pLevel, BlockPos pPos){
        return pLevel.getEntitiesOfClass(LivingEntity.class,
                new AABB(pPos.relative(Direction.DOWN, RANGE_Y_DOWN).relative(Direction.EAST, RANGE_X).relative(Direction.NORTH, RANGE_X),
                        pPos.relative(Direction.UP, RANGE_Y).relative(Direction.WEST, RANGE_X).relative(Direction.SOUTH, RANGE_X)));
    }

    private void consumeFume(){
        ItemStack item = this.inputItemHandler.extractItem(0, 1, false);
        this.displayItemHandler.setStackInSlot(0, item.copy());
        this.fumeId = item.getTag().getInt("buntsy.fumeType");
        this.fumeLevel = item.getTag().getInt("buntsy.fumeLevel");
        this.fumeLeftMax = FUME_CONSUMPTION_DETAILS.get(this.fumeId).get(0);
        this.fumeLeft = this.fumeLeftMax;
        this.fumeTick = 0;
        this.fumeTickPeriod = FUME_CONSUMPTION_DETAILS.get(this.fumeId).get(1);
    }

    public boolean canRun(){
        return this.fumeLeft > 0;
    }

    public boolean hasFume(){
        return this.inputItemHandler.getStackInSlot(0).is(ModItems.FUME_BOTTLE.get()) && isOutputClear();
    }

    public void outputItems(){
        ItemStack item = new ItemStack(Items.GLASS_BOTTLE, 1);
        this.outputItemHandler.setStackInSlot(0,
                new ItemStack(item.getItem(), this.outputItemHandler.getStackInSlot(0).getCount() + item.getCount()));
    }

    public boolean isOutputClear(){
        ItemStack item = new ItemStack(Items.GLASS_BOTTLE, 1);
        return (this.outputItemHandler.getStackInSlot(0).isEmpty() || this.outputItemHandler.getStackInSlot(0).getItem() == item.getItem())
                && (this.outputItemHandler.getStackInSlot(0).getCount() + item.getCount() <= item.getMaxStackSize());
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
