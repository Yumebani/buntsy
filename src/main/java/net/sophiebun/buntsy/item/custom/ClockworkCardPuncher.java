package net.sophiebun.buntsy.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkFairyTerminalBlock;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkMaidenTerminalBlock;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.server.ClockworkCardPuncherPacket;
import net.sophiebun.buntsy.server.FairyStaffOperationType;
import net.sophiebun.buntsy.server.ModFairyStaffPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class ClockworkCardPuncher extends Item {

    private Integer selectedMaidenId = null;
    private BlockPos selectedMaidenTerminal = null;

    public ClockworkCardPuncher(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if (pContext.getLevel().isClientSide()){
            if (this.selectedMaidenId != null){
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenId, pContext.getClickedPos(), FairyStaffOperationType.SET_BLOCK));
                this.selectedMaidenId = null;
                return InteractionResult.SUCCESS;
            } else if (this.selectedMaidenTerminal != null && !(pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkMaidenTerminalBlock)){
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenTerminal, pContext.getClickedPos(), FairyStaffOperationType.SET_BLOCK));
                return InteractionResult.SUCCESS;
            } else if (pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkMaidenTerminalBlock) {
                this.selectedMaidenTerminal = pContext.getClickedPos();
                pContext.getPlayer().displayClientMessage(Component.literal("§aSelected maiden terminal"), true);
            } else if (this.selectedMaidenTerminal != null){
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenTerminal, null, FairyStaffOperationType.CLEAR_DATA));
                this.selectedMaidenTerminal = null;
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        if (pPlayer.level().isClientSide() && pInteractionTarget.getType() == ModEntities.CLOCKWORK_MAIDEN_ENTITY.get()){
            int maidenId = pInteractionTarget.getId();
            if (this.selectedMaidenId != null && this.selectedMaidenId == maidenId){
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenId, null, FairyStaffOperationType.CLEAR_DATA));
                this.selectedMaidenId = null;
            }
            else if (this.selectedMaidenTerminal == null){
                this.selectedMaidenId = maidenId;
                pPlayer.displayClientMessage(Component.literal("§aSelected maiden"), true);
            }
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

}
