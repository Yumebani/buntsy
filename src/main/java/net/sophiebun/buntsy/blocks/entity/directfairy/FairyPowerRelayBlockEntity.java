package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FairyPowerRelayBlockEntity extends FairyInteractBlockEntity {

    private BlockPos linked;

    public FairyPowerRelayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAIRY_POWER_RELAY_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(1f);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        if (linked == null){
            pTag.putBoolean("fairy_power_relay.has_linked", false);
        }
        else {
            pTag.putBoolean("fairy_power_relay.has_linked", true);
            pTag.put("fairy_power_relay.linked", NbtUtils.writeBlockPos(linked));
        }

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.getBoolean("fairy_power_relay.has_linked")){
            this.linked =  NbtUtils.readBlockPos(pTag.getCompound("fairy_power_relay.linked"));
        }
        else this.linked = null;
    }

    public void setLinked(BlockPos linked) {
        this.linked = linked;
    }

    public BlockPos getLinked() {
        return linked;
    }

    public void removeLinked(Level pLevel){
        if (linked != null){
            if (pLevel.getBlockEntity(linked) instanceof InfusionAltarBlockEntity){
                ((InfusionAltarBlockEntity) level.getBlockEntity(linked)).clearRelay(linked);
            }
            linked = null;
        }
    }

    @Override
    public int getFairyWeight() {
        return 2;
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

    public void animateParticles(ServerLevel pLevel, BlockPos pos) {
        if (isEnchanted()){
            ((ServerLevel) pLevel).sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX()+ 0.5f, pos.getY() + 1f, pos.getZ()+ 0.5f, 2,
                    0, 0, 0, 0.1f);
        }
    }
}