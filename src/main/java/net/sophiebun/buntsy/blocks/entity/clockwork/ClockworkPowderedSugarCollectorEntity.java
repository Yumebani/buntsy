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
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.clockwork.ClockworkPowderedSugarCollectorMenu;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;
import org.jetbrains.annotations.Nullable;

public class ClockworkPowderedSugarCollectorEntity extends ClockworkPassiveCollectorEntity implements MenuProvider {

    public ClockworkPowderedSugarCollectorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_POWDERED_SUGAR_COLLECTOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_powdered_sugar_collector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkPowderedSugarCollectorMenu(i, inventory, this, this.data);
    }
    @Override
    protected int getProgressCheck(Level level, BlockPos pos, RandomSource random) {
        return level.isRaining() ? random.nextInt(600, 900) : random.nextInt(1200, 1800);
    }

    @Override
    protected ItemStack generateOutput(RandomSource random) {
        return new ItemStack(random.nextInt(0, 3) == 0 ? ModItems.SWICE_SHARDS.get() : ModItems.COLD_POWDERED_SUGAR.get(), random.nextInt(1, 3));
    }

    @Override
    protected boolean canWork(Level level, BlockPos pPos, BlockState pState) {
        return level.getBiome(pPos).is(ModBiomes.POWDERY_TUNDRA_BIOME) || level.getBiome(pPos).is(ModBiomes.CHOCOLATE_SPRINGS_BIOME);
    }
}
