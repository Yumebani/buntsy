package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.item.ClockworkTier;
import net.sophiebun.buntsy.item.custom.ClockworkUpgradeItem;

public class ClockworkBlockEntity extends BlockEntity {

    protected ClockworkTier clockworkTier = ClockworkTier.NONE;
    private ItemStack upgradeItem = null;

    public ClockworkBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("clockwork_base.clockwork_tier", this.clockworkTier.ordinal());
        pTag.putBoolean("clockwork_base.has_upgrade_item", upgradeItem != null);
        if (upgradeItem != null){
            pTag.put("clockwork_base.upgrade_item", upgradeItem.serializeNBT());
        }

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.clockworkTier = ClockworkTier.values()[pTag.getInt("clockwork_base.clockwork_tier")];
        if (pTag.getBoolean("clockwork_base.has_upgrade_item")){
            this.upgradeItem = ItemStack.EMPTY;
            this.upgradeItem.deserializeNBT(pTag.getCompound("clockwork_base.upgrade_item"));
        } else {
            this.upgradeItem = null;
        }
    }

    public InteractionResult putUpgrade(ItemStack potentialUpgrade, Level level, Player player, InteractionHand hand){

        if (potentialUpgrade.getItem() instanceof ClockworkUpgradeItem){

            this.ejectItem(level, player);
            this.upgradeItem = potentialUpgrade;
            this.clockworkTier = ((ClockworkUpgradeItem) this.upgradeItem.getItem()).getTier();
            player.setItemInHand(hand, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void ejectItem(Level level, Player player){
        if (this.upgradeItem != null){
            level.addFreshEntity(new ItemEntity(level, player.position().x, player.position().y + 1f, player.position().z, this.upgradeItem));
            this.clockworkTier = ClockworkTier.NONE;
            this.upgradeItem = ItemStack.EMPTY;
        }
    }

    protected int getClockworkProgressAmount(){
        return switch (clockworkTier){
            case NONE -> 1;
            case SIMPLE -> 2;
            case INTRICATE -> 4;
            case COMPLEX -> 8;
        };
    }
}
