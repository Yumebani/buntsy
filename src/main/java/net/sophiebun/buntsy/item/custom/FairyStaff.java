package net.sophiebun.buntsy.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.server.FairyStaffOperationType;
import net.sophiebun.buntsy.server.ModFairyStaffPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;
import net.sophiebun.buntsy.tag.ModTags;

public class FairyStaff extends Item {

    private Integer selectedFairyId = null;

    public FairyStaff(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if (pContext.getLevel().isClientSide() && this.selectedFairyId != null){
            ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyId, pContext.getClickedPos(), FairyStaffOperationType.SET_BLOCK));
            this.selectedFairyId = null;
            return InteractionResult.SUCCESS;
        }

        return super.useOn(pContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        if (pPlayer.level().isClientSide() && pInteractionTarget.getType() == ModEntities.FAIRY_ENTITY.get()){
            int fairyID = pInteractionTarget.getId();
            if (this.selectedFairyId != null && this.selectedFairyId == fairyID){
                ModPacketHandler.INSTANCE.sendToServer(new ModFairyStaffPacket(this.selectedFairyId, null, FairyStaffOperationType.CLEAR_DATA));
            }
            else{
                this.selectedFairyId = fairyID;
                pPlayer.displayClientMessage(Component.literal("§aSelected fairy"), true);
            }
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

}
