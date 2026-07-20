package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkWinderBlock;
import net.sophiebun.buntsy.blocks.custom.entityblocks.WindupClockworkBlock;

public abstract class WindupClockworkEntity extends ClockworkBlockEntity{

    protected int windupRemaining = 0;
    private BlockPos winder = null;

    public WindupClockworkEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean isWindedUpBy(){
        return winder != null;
    }

    public boolean isWoundUp(){
        return windupRemaining > 0;
    }

    public abstract int getWindupWeight();

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.putBoolean("windup_clockwork_entity.has_winder", this.winder != null);
        if (this.winder != null){
            pTag.put("windup_clockwork_entity.winder", NbtUtils.writeBlockPos(this.winder));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        if (pTag.getBoolean("windup_clockwork_entity.has_winder")){
            this.winder = NbtUtils.readBlockPos(pTag, "windup_clockwork_entity.winder").get();
        }
    }

    public void windup(BlockPos pos){
        windupRemaining = 80;
        winder = pos;
        BlockState state = getBlockState();
        if (!state.getValue(WindupClockworkBlock.RUNNING)){
            level.setBlockAndUpdate(getBlockPos(), state.setValue(WindupClockworkBlock.RUNNING, true));
        }
    }

    protected void tickWindup(){
        windupRemaining -= getClockworkWindupAmount();
        if (windupRemaining <= 0){
            winder = null;
            windupRemaining = 0;
            BlockState state = getBlockState();
            if (state.getValue(WindupClockworkBlock.RUNNING)){
                level.setBlockAndUpdate(getBlockPos(), state.setValue(WindupClockworkBlock.RUNNING, false));
            }
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
