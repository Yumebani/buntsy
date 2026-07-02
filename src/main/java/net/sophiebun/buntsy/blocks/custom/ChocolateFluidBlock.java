package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ChocolateFluidBlock extends LiquidBlock {


    public ChocolateFluidBlock(Supplier<? extends FlowingFluid> pFluid, Properties pProperties) {
        super(pFluid, pProperties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        if (pLevel.isClientSide() && pRandom.nextInt(1, 7) == 1){
            pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false,
                    pPos.getX(), pPos.getY() + 0.5f, pPos.getZ(),
                    0f,
                    0.1f,
                    0f);
        }

        if (pLevel.isClientSide() && pRandom.nextInt(1, 100) == 1){
            pLevel.playLocalSound(pPos, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.5f, 0.8f, false);
        }

        super.animateTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
}
