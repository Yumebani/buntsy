package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class WindupClockworkEntity extends ClockworkBlockEntity{

    private int windupRemaining = 0;
    private BlockPos winder = null;

    public WindupClockworkEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean isWindedUpBy(){
        return winder == null;
    }

    public boolean isWoundUp(){
        return windupRemaining > 0;
    }

    public abstract int getWindupWeight();

    public void windup(BlockPos pos){
        windupRemaining = 80;
        winder = pos;
    }

    protected void tickWindup(){
        windupRemaining -= getClockworkWindupAmount();
        if (windupRemaining <= 0){
            winder = null;
            windupRemaining = 0;
        }
    }

    protected int getClockworkWindupAmount(){
        return switch (clockworkTier){
            case NONE -> 2;
            case SIMPLE -> 3;
            case INTRICATE -> 4;
            case COMPLEX -> 6;
        };
    }
}
