package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FairyInteractBlockEntity extends BlockEntity {

    private static final int FAIRY_WEIGHT = 1;

    private boolean isWatched = false;
    private boolean isEnchanted = false;
    private float consumption;

    public FairyInteractBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putFloat("fairy_interaction_block.consumption", this.consumption);
        pTag.putBoolean("fairy_interaction_block.is_enchanted", this.isEnchanted);
        pTag.putBoolean("fairy_interaction_block.is_watched", this.isWatched);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.consumption = pTag.getFloat("fairy_interaction_block.consumption");
        this.isEnchanted = pTag.getBoolean("fairy_interaction_block.is_enchanted");
        this.isWatched = pTag.getBoolean("fairy_interaction_block.is_watched");
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isEnchanted() {
        return isEnchanted;
    }

    public void setEnchanted(boolean enchanted) {
        isEnchanted = enchanted;
    }

    public float getConsumption() {
        return this.consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public int getFairyWeight(){
        return this.FAIRY_WEIGHT;
    }

    public boolean isValidForInteraction(){
        return !this.level.getBlockState(this.getBlockPos().above(1)).isSolid();
    }
}
