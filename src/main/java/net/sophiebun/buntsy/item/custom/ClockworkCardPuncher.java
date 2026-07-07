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
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkMaidenTerminalBlock;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.server.ClockworkCardPuncherPacket;
import net.sophiebun.buntsy.server.ConfigureStaffOperationType;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class ClockworkCardPuncher extends Item {

    private Integer selectedMaidenId = null;
    private BlockPos selectedMaidenTerminal = null;
    private boolean editingMode;

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
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenId, pContext.getClickedPos(), ConfigureStaffOperationType.SET_BLOCK));
                this.selectedMaidenId = null;
                return InteractionResult.SUCCESS;
            } else if (this.selectedMaidenTerminal != null && !(pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkMaidenTerminalBlock) && pContext.getLevel().getBlockEntity(pContext.getClickedPos()) != null){

                if (editingMode){
                    ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenTerminal, pContext.getClickedPos(), ConfigureStaffOperationType.EDIT_DATA));
                } else {
                    ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenTerminal, pContext.getClickedPos(), ConfigureStaffOperationType.SET_BLOCK));
                }
                return InteractionResult.SUCCESS;

            } else if (this.selectedMaidenTerminal == null && pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkMaidenTerminalBlock) {

                this.selectedMaidenTerminal = pContext.getClickedPos();
                pContext.getPlayer().displayClientMessage(Component.literal("§aSelected maiden terminal"), true);

            } else if (this.selectedMaidenTerminal != null  && pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ClockworkMaidenTerminalBlock){

                this.editingMode = !editingMode;
                pContext.getPlayer().displayClientMessage(Component.literal("§aChanged to " + (editingMode ? "editing" : "binding") + " mode"), true);

            } else {

                this.selectedMaidenId = null;
                this.selectedMaidenTerminal = null;
                this.editingMode = false;
                pContext.getPlayer().displayClientMessage(Component.literal("§aCleared selection"), true);
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        if (pPlayer.level().isClientSide() && pInteractionTarget.getType() == ModEntities.CLOCKWORK_MAIDEN_ENTITY.get()){
            int maidenId = pInteractionTarget.getId();
            if (this.selectedMaidenId != null && this.selectedMaidenId == maidenId){
                ModPacketHandler.INSTANCE.sendToServer(new ClockworkCardPuncherPacket(this.selectedMaidenId, null, ConfigureStaffOperationType.CLEAR_DATA));
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
