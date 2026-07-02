package net.sophiebun.buntsy.blocks.custom.entityblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.sophiebun.buntsy.blocks.entity.ChocolateGeyserBlockEntity;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.client.particle.ModParticleTypes;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ChocolateGeyserBlock extends BaseEntityBlock {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    public ChocolateGeyserBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STAGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChocolateGeyserBlockEntity(pPos, pState);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.CHOCOLATE_GEYSER_ENTITY.get(),
                    (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.clientTick(pLevel1, pPos, pState1));
        } else {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.CHOCOLATE_GEYSER_ENTITY.get(),
                    (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
