package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;

import javax.annotation.Nullable;

public class HootnipCrop extends BushBlock implements BonemealableBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final int MAX_AGE_BOTTOM = 4;
    public static final int MAX_AGE_TOP = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)};



    public HootnipCrop(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                .setValue(this.getAgeProperty(), Integer.valueOf(0)));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.FARMLAND);
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    private static boolean isLower(BlockState state){
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER;
    }

    public int getMaxAge() {
        return MAX_AGE_BOTTOM + MAX_AGE_TOP;
    }

    public int getAge(BlockState pState) {
        return pState.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int pAge) {
        return pAge < MAX_AGE_BOTTOM ? this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(pAge))
                : this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(pAge))
                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
    }

    public final boolean isMaxAge(BlockState pState) {
        return isLower(pState) ? this.getAge(pState) >= MAX_AGE_BOTTOM - 1 : this.getAge(pState) >= this.getMaxAge();
    }
    public boolean isRandomlyTicking(BlockState pState) {
        return this.getAge(pState) != this.getMaxAge();
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            float f = getGrowthSpeed(this, pLevel, pPos);
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / f) + 1) == 0)){
                growCrop(pState, pLevel, pPos);
            }
        }

    }

    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = this.getBonemealAgeIncrease(pLevel);
        while (this.getAge(pState) < this.getMaxAge() && i != 0){
            i--;
            growCrop(pState, pLevel, pPos);
        }
    }

    public void growCrop(BlockState pState, Level pLevel, BlockPos pPos){
        int i = this.getAge(pState);
        if (isLower(pState)){
            if (!isMaxAge(pState)){
                pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }
            else if (isMaxAge(pState) && !pLevel.getBlockState(pPos.relative(Direction.UP, 1)).is(ModBlocks.HOOTNIP_CROP.get())){
                pLevel.setBlock(pPos.relative(Direction.UP, 1), this.getStateForAge(i + 1), 2);
            }
        }
        else if (i < this.getMaxAge() - 1) {
            pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }

    protected int getBonemealAgeIncrease(Level pLevel) {
        return Mth.nextInt(pLevel.random, 2, 5);
    }

    protected static float getGrowthSpeed(Block pBlock, BlockGetter pLevel, BlockPos pPos) {
        float f = 1.0F;
        BlockPos blockpos = pPos.below();
        if (pLevel.getBlockState(blockpos).is(ModBlocks.HOOTNIP_CROP.get())){
            blockpos = blockpos.below();
        }

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = pLevel.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.canSustainPlant(pLevel, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, (net.minecraftforge.common.IPlantable) pBlock)) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(pLevel, pPos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pPos.north();
        BlockPos blockpos2 = pPos.south();
        BlockPos blockpos3 = pPos.west();
        BlockPos blockpos4 = pPos.east();
        boolean flag = pLevel.getBlockState(blockpos3).is(pBlock) || pLevel.getBlockState(blockpos4).is(pBlock);
        boolean flag1 = pLevel.getBlockState(blockpos1).is(pBlock) || pLevel.getBlockState(blockpos2).is(pBlock);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = pLevel.getBlockState(blockpos3.north()).is(pBlock) || pLevel.getBlockState(blockpos4.north()).is(pBlock) || pLevel.getBlockState(blockpos4.south()).is(pBlock) || pLevel.getBlockState(blockpos3.south()).is(pBlock);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            if (pPlayer.isCreative()) {
                preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
            } else {
                dropResources(pState, pLevel, pPos, (BlockEntity)null, pPlayer, pPlayer.getMainHandItem());
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
    }

    protected static void preventCreativeDropFromBottomPart(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pPos.below();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                pLevel.setBlock(blockpos, blockstate1, 35);
                pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        if (!isLower(pState)){
            pPos = pPos.below();
            pState = pLevel.getBlockState(pPos);
            if (!pState.is(ModBlocks.HOOTNIP_CROP.get())){
                return false;
            }
        }
        else if ((!aboveState.isAir() && !aboveState.is(ModBlocks.HOOTNIP_CROP.get()))
        || (getAge(pState) >= MAX_AGE_BOTTOM && aboveState.is(ModBlocks.HOOTNIP_CROP.get()))){
            return false;
        }
        return (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos)) && super.canSurvive(pState, pLevel, pPos);
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof Ravager && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLevel, pEntity)) {
            pLevel.destroyBlock(pPos, true, pEntity);
        }

        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.HOOTNIP_SEEDS.get();
    }

    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(this.getBaseSeedId());
    }

    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return this.getAge(pState) != this.getMaxAge();
    }

    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        this.growCrops(pLevel, pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
        pBuilder.add(HALF);
    }
}
