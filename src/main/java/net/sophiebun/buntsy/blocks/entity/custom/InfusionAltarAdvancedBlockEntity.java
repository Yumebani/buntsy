package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.recipe.InfusionAltarAdvancedRecipe;
import net.sophiebun.buntsy.recipe.InfusionAltarInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfusionAltarAdvancedBlockEntity extends InfusionAltarBlockEntity {

    public InfusionAltarAdvancedBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INFUSION_ALTAR_ADVANCED_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        checkAltarValidity(pLevel, pPos);

        if (canRun(pLevel)){

            maxProgress = getCurrentRecipe(pLevel).get().value().getMaxProgress();
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
        InfusionAltarAdvancedRecipe recipe = getCurrentRecipe(pLevel).get().value();
        ItemStack result = recipe.getResultItem(null);

        this.itemHandler.extractItem(0, 1, false);
        for (int i = 0; i < 8; i++){
            extractItemFromPedestal(pLevel, infusionPedestals.get(i));
        }

        itemHandler.setStackInSlot(0, result);
    }

    public boolean hasRecipe(Level pLevel) {
        Optional<RecipeHolder<InfusionAltarAdvancedRecipe>> recipe = getCurrentRecipe(pLevel);

        return !recipe.isEmpty();
    }

    public Optional<RecipeHolder<InfusionAltarAdvancedRecipe>> getCurrentRecipe(Level pLevel) {
        List<ItemStack> items = new ArrayList<>();
        items.add(itemHandler.getStackInSlot(0));
        for (int i = 1; i <= 8; i++){
            items.add(getItemFromPedestal(pLevel, infusionPedestals.get(i - 1)));
        }
        InfusionAltarInput input = new InfusionAltarInput(items);

        return this.level.getRecipeManager().getRecipeFor(InfusionAltarAdvancedRecipe.Type.INSTANCE, input, level);
    }

    protected void checkAltarValidity(Level level, BlockPos pos) {
        checkPedestalPlacement(level, pos.relative(Direction.NORTH, 3));
        checkPedestalPlacement(level, pos.relative(Direction.NORTH, 2).relative(Direction.EAST, 2));
        checkPedestalPlacement(level, pos.relative(Direction.EAST, 3));
        checkPedestalPlacement(level, pos.relative(Direction.EAST, 2).relative(Direction.SOUTH, 2));
        checkPedestalPlacement(level, pos.relative(Direction.SOUTH, 3));
        checkPedestalPlacement(level, pos.relative(Direction.SOUTH, 2).relative(Direction.WEST, 2));
        checkPedestalPlacement(level, pos.relative(Direction.WEST, 3));
        checkPedestalPlacement(level, pos.relative(Direction.WEST, 2).relative(Direction.NORTH, 2));
    }


    protected boolean altarIsValid() {
        return infusionPedestals.size() == 8;
    }
}