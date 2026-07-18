package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkFisherBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.screen.clockwork.ClockworkFisherMenu;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClockworkFisherEntity extends ClockworkPassiveCollectorEntity implements MenuProvider {

    private int lastCheckTick = 0;
    private int functionality = 0;

    public ClockworkFisherEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_FISHER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_fisher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkFisherMenu(i, inventory, this, this.data);
    }
    @Override
    protected int getProgressCheck(Level level, BlockPos pos, RandomSource random) {
        return random.nextInt(2400 / functionality, 3600 / functionality);
    }

    @Override
    protected ItemStack generateOutput(RandomSource random) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);

        LootParams emptyParams = new LootParams.Builder(((ServerLevel) level))
                .create(LootContextParamSets.EMPTY);

        List<ItemStack> generatedLoot = lootTable.getRandomItems(emptyParams);

        if (!generatedLoot.isEmpty()) {
            return generatedLoot.get(0);
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canWork(Level level, BlockPos pPos, BlockState pState) {
        lastCheckTick--;
        if (lastCheckTick <= 0){
            if (!(pState.getBlock() instanceof ClockworkFisherBlock) || !pState.getValue(ClockworkFisherBlock.WATERLOGGED)){
                return false;
            }
            int x = pPos.getX();
            int y = pPos.getY();
            int z = pPos.getZ();
            int count = 1;
            for (BlockState state : level.getBlockStates(new AABB(x - 1, y, z - 1, x + 1, y - 3, z + 1)).toList()){
                if (state.getFluidState().is(Fluids.WATER)){
                    count++;
                }
            }
            if (count < 18){return false;}
            functionality = Math.floorDiv(count, 8);
            lastCheckTick = 20;
        }
        return true;
    }
}
