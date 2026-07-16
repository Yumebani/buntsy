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
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.server.ConfigureStaffOperationType;
import net.sophiebun.buntsy.server.ModFairyStaffPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class FairyStaff extends Item {

    private Integer selectedFairyId = null;
    private BlockPos selectedFairyTerminal = null;

    public FairyStaff(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if (pContext.getLevel().isClientSide()){
            if (this.selectedFairyId != null){
                ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyId, pContext.getClickedPos(), ConfigureStaffOperationType.SET_BLOCK));
                this.selectedFairyId = null;
                return InteractionResult.SUCCESS;
            } else if (this.selectedFairyTerminal != null && !(pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkFairyTerminalBlock) && pContext.getLevel().getBlockEntity(pContext.getClickedPos()) != null){
                ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyTerminal, pContext.getClickedPos(), ConfigureStaffOperationType.SET_BLOCK));
                this.selectedFairyTerminal = null;
                return InteractionResult.SUCCESS;
            } else if (this.selectedFairyTerminal == null && pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkFairyTerminalBlock) {
                this.selectedFairyTerminal = pContext.getClickedPos();
                pContext.getPlayer().displayClientMessage(Component.literal("§aSelected fairy terminal"), true);
            } else if (this.selectedFairyTerminal != null  && pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkFairyTerminalBlock){
                ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyTerminal, null, ConfigureStaffOperationType.CLEAR_DATA));
                this.selectedFairyTerminal = null;
            } else {
                this.selectedFairyTerminal = null;
                this.selectedFairyId = null;
                pContext.getPlayer().displayClientMessage(Component.literal("§aCleared selection"), true);
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        if (pPlayer.level().isClientSide() && pInteractionTarget.getType() == ModEntities.FAIRY_ENTITY.get()){
            int fairyID = pInteractionTarget.getId();
            if (this.selectedFairyId != null && this.selectedFairyId == fairyID){
                ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyId, null, ConfigureStaffOperationType.CLEAR_DATA));
                this.selectedFairyId = null;
            }
            else if (this.selectedFairyTerminal == null){
                this.selectedFairyId = fairyID;
                pPlayer.displayClientMessage(Component.literal("§aSelected fairy"), true);
            }
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

}
