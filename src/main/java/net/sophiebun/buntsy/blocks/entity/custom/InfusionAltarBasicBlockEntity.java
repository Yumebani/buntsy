package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.recipe.InfusionAltarBasicRecipe;

import java.util.Optional;

public class InfusionAltarBasicBlockEntity extends InfusionAltarBlockEntity {

    public InfusionAltarBasicBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INFUSION_ALTAR_BASIC_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        checkAltarValidity(pLevel, pPos);

        if (canRun(pLevel)){

            maxProgress = getCurrentRecipe(pLevel).get().getMaxProgress();
            animateParticles(pLevel, pPos);
            increaseProgress(pLevel);

            if (hasProgressFinished()){
                resetProgress();
                craftItem(pLevel);
                tick(pLevel, pPos, pState);
            }
        }
        else{
            resetProgress();
        }
    }

    public void craftItem(Level pLevel) {
        InfusionAltarBasicRecipe recipe = getCurrentRecipe(pLevel).get();
        ItemStack result = recipe.getResultItem(null);

        this.itemHandler.extractItem(0, 1, false);
        for (int i = 0; i < 4; i++){
            extractItemFromPedestal(pLevel, infusionPedestals.get(i));
        }

        itemHandler.setStackInSlot(0, result);
    }

    public boolean hasRecipe(Level pLevel) {
        Optional<InfusionAltarBasicRecipe> recipe = getCurrentRecipe(pLevel);

        return !recipe.isEmpty();
    }

    public Optional<InfusionAltarBasicRecipe> getCurrentRecipe(Level pLevel) {
        SimpleContainer inventory = new SimpleContainer(5);
        inventory.setItem(0, itemHandler.getStackInSlot(0));
        for (int i = 1; i <= 4; i++){
            inventory.setItem(i, getItemFromPedestal(pLevel, infusionPedestals.get(i - 1)));
        }

        return this.level.getRecipeManager().getRecipeFor(InfusionAltarBasicRecipe.Type.INSTANCE, inventory, level);
    }

    protected void checkAltarValidity(Level level, BlockPos pos) {
        checkPedestalPlacement(level, pos.relative(Direction.NORTH, 2));
        checkPedestalPlacement(level, pos.relative(Direction.EAST, 2));
        checkPedestalPlacement(level, pos.relative(Direction.SOUTH, 2));
        checkPedestalPlacement(level, pos.relative(Direction.WEST, 2));
    }

    protected boolean altarIsValid() {
        return infusionPedestals.size() == 4;
    }
}