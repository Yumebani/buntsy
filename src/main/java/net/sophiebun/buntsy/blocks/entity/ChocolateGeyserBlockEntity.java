package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ChocolateGeyserBlock;
import net.sophiebun.buntsy.client.particle.ModParticleTypes;

public class ChocolateGeyserBlockEntity extends BlockEntity {

    private int geyserCooldown = 0;
    private int clientTicker = 0;

    public ChocolateGeyserBlockEntity(BlockPos pPos, BlockState pBlockState){
        super(ModBlockEntities.CHOCOLATE_GEYSER_ENTITY.get(), pPos, pBlockState);
    }

    public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {

        clientTicker++;
        if (pLevel.getBlockState(pPos.above()).isAir()){
            if (clientTicker % 10 == 0 && pState.getValue(ChocolateGeyserBlock.STAGE) == 1) {
                RandomSource random = pLevel.getRandom();
                pLevel.addParticle(ModParticleTypes.CHOCOLATE_DUST_PARTICLE.get(),
                        pPos.getX() + 0.5f + random.nextInt(-1, 2) / 2f,
                        pPos.getY() + 1f + random.nextInt(0, 2) / 2f,
                        pPos.getZ() + 0.5f + random.nextInt(-1, 2) / 2f,
                        random.nextInt(-6, 7) / 100f,
                        random.nextInt(-6, 7) / 100f,
                        random.nextInt(-6, 7) / 100f);
            } else if (pState.getValue(ChocolateGeyserBlock.STAGE) == 2){
                RandomSource random = pLevel.getRandom();
                if (clientTicker % 9 == 0) pLevel.playLocalSound(pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7f, 0.6f, false);
                for (int x = 0; x < 5; x++){
                    pLevel.addParticle(ModParticleTypes.CHOCOLATE_DUST_PARTICLE.get(),
                            pPos.getX() + 0.5f, pPos.getY() + 1f, pPos.getZ() + 0.5f,
                            random.nextInt(-2, 3) / 100f,
                            random.nextInt(35, 50) /  100f,
                            random.nextInt(-2, 3) / 100f);
                }
            }
        }

        if (clientTicker >= 20){
            clientTicker = 0;
        }
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        geyserCooldown--;
        if (geyserCooldown <= 0){
            if (pState.getValue(ChocolateGeyserBlock.STAGE) == 0){
                geyserCooldown = pLevel.getRandom().nextInt(300, 600);
                pLevel.setBlock(pPos, pState.setValue(ChocolateGeyserBlock.STAGE, 1), 2);
            } else if (pState.getValue(ChocolateGeyserBlock.STAGE) == 1){
                geyserCooldown = pLevel.getRandom().nextInt(600, 1200);
                pLevel.setBlock(pPos, pState.setValue(ChocolateGeyserBlock.STAGE, 2), 2);
            } else {
                geyserCooldown = pLevel.getRandom().nextInt(1200, 2400);
                pLevel.setBlock(pPos, pState.setValue(ChocolateGeyserBlock.STAGE, 0), 2);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("cooldown", geyserCooldown);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.geyserCooldown = tag.getInt("cooldown");
    }
}
