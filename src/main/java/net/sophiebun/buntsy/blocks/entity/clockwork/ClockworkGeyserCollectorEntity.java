package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ChocolateGeyserBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.ClockworkGeyserCollectorMenu;
import org.jetbrains.annotations.Nullable;

public class ClockworkGeyserCollectorEntity extends ClockworkPassiveCollectorEntity implements MenuProvider {

    public ClockworkGeyserCollectorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_GEYSER_COLLECTOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_geyser_collector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkGeyserCollectorMenu(i, inventory, this, this.data);
    }
    @Override
    protected int getProgressCheck(Level level, BlockPos pos, RandomSource random) {
        return random.nextInt(200, 300);
    }

    @Override
    protected ItemStack generateOutput(RandomSource random) {
        return new ItemStack(ModItems.CHOCOLATE_FLAKES.get(), random.nextInt(1, 4));
    }

    @Override
    protected boolean canWork(Level level, BlockPos pPos, BlockState pState) {
        BlockState state = level.getBlockState(pPos.below());
        return (state.getBlock() instanceof ChocolateGeyserBlock) && state.getValue(ChocolateGeyserBlock.STAGE) == 2;
    }
}
