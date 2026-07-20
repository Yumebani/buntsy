package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FairyInteractBlockEntity extends BlockEntity {

    private static final int FAIRY_WEIGHT = 1;

    private boolean isWatched = false;
    private boolean isEnchanted = false;
    private int speedUp = 1;
    private float consumption;

    public FairyInteractBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putFloat("fairy_interaction_block.consumption", this.consumption);
        tag.putInt("fairy_interaction_block.speedUp", this.speedUp);
        tag.putBoolean("fairy_interaction_block.is_enchanted", this.isEnchanted);
        tag.putBoolean("fairy_interaction_block.is_watched", this.isWatched);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.consumption = tag.getFloat("fairy_interaction_block.consumption");
        this.speedUp = tag.getInt("fairy_interaction_block.speedUp");
        this.isEnchanted = tag.getBoolean("fairy_interaction_block.is_enchanted");
        this.isWatched = tag.getBoolean("fairy_interaction_block.is_watched");
    }

    public void setSpeedUp(int speed){
        this.speedUp = speed;
    }

    public int getSpeedUp() {
        return this.speedUp;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public boolean isTitular() {
        return false;
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
