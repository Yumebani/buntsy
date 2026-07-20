package net.sophiebun.buntsy.item.custom;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.worldgen.dimension.BuntsyDimension;

public class FairyTaleBook extends Item {

    public FairyTaleBook(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            MinecraftServer server = pLevel.getServer();
            if (pPlayer.level().dimension() == BuntsyDimension.BUNTSY_LEVEL_KEY){

                if (!pPlayer.isPassenger()){
                    server.execute(() -> {
                        if (!pPlayer.isRemoved()) {
                            pPlayer.changeDimension(new DimensionTransition(server.overworld(), pPlayer.position(), Vec3.ZERO, pPlayer.getYRot(), pPlayer.getXRot(), DimensionTransition.DO_NOTHING));
                        }
                    });
                }

            } else if (pPlayer.level().dimension() == Level.OVERWORLD) {

                ServerLevel dimension = server.getLevel(BuntsyDimension.BUNTSY_LEVEL_KEY);
                if (!pPlayer.isPassenger()){
                    server.execute(() -> {
                        if (!pPlayer.isRemoved()) {
                            pPlayer.changeDimension(new DimensionTransition(dimension, pPlayer.position(), Vec3.ZERO, pPlayer.getYRot(), pPlayer.getXRot(), DimensionTransition.DO_NOTHING));
                        }
                    });
                }
            }
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
