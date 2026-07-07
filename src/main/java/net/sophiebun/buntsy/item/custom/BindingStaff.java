package net.sophiebun.buntsy.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.sophiebun.buntsy.server.ModBindingStaffPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;

public class BindingStaff extends Item {

    private BlockPos selectedBinding = null;

    public BindingStaff(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if (pContext.getLevel().isClientSide() && this.selectedBinding != null){
            ModPacketHandler.INSTANCE.sendToServer(new ModBindingStaffPacket(this.selectedBinding, pContext.getClickedPos()));
            this.selectedBinding = null;
            return InteractionResult.SUCCESS;
        }
        else if (pContext.getLevel().isClientSide()) {
            selectedBinding = pContext.getClickedPos();
            pContext.getPlayer().displayClientMessage(Component.literal("§aSelected block"), true);
        }

        return super.useOn(pContext);
    }
}
