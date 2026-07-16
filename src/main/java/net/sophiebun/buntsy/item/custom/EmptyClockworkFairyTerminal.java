package net.sophiebun.buntsy.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.entity.animals.Fairy;

public class EmptyClockworkFairyTerminal extends Item {

    public EmptyClockworkFairyTerminal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide()){
            if (pInteractionTarget instanceof Fairy){
                if (((Fairy) pInteractionTarget).isTame()){
                    pPlayer.level().addFreshEntity(new ItemEntity(pPlayer.level(), pInteractionTarget.position().x(),
                            pInteractionTarget.position().y(), pInteractionTarget.position().z(), new ItemStack(ModBlocks.CLOCKWORK_FAIRY_TERMINAL.get())));
                    pInteractionTarget.remove(Entity.RemovalReason.DISCARDED);
                    pPlayer.getItemInHand(pUsedHand).shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
