package net.sophiebun.buntsy.item.custom;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import net.sophiebun.buntsy.worldgen.dimension.BuntsyDimension;

public class FairyTaleBook extends Item implements ITeleporter {

    public FairyTaleBook(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.canChangeDimensions()) {

            if (!pLevel.isClientSide()) {
                MinecraftServer server = pLevel.getServer();
                if (pPlayer.level().dimension() == BuntsyDimension.BUNTSY_LEVEL_KEY){

                    if (!pPlayer.isPassenger()){
                        server.execute(() -> {
                            if (!pPlayer.isRemoved()) {
                                pPlayer.changeDimension(server.overworld(), this);
                            }
                        });
                    }

                } else if (pPlayer.level().dimension() == Level.OVERWORLD) {

                    ServerLevel dimension = server.getLevel(BuntsyDimension.BUNTSY_LEVEL_KEY);
                    if (!pPlayer.isPassenger()){
                        server.execute(() -> {
                            if (!pPlayer.isRemoved()) {
                                pPlayer.changeDimension(dimension, this);
                            }
                        });
                    }
                }
            }

            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        } else {
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }
    }
}
