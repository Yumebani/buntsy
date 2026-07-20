package net.sophiebun.buntsy.blocks.entity.basicfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.recipe.SingleItemInput;
import net.sophiebun.buntsy.recipe.ThreadReelerRecipe;
import net.sophiebun.buntsy.screen.ThreadReelerMenu;
import net.sophiebun.buntsy.server.GiantCocoonSavedData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.*;

public class ThreadReelerBlockEntity extends BasicFairyBlockEntity implements MenuProvider, GeoBlockEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<ThreadReelerBlockEntity> controller;

    public ThreadReelerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(1f);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.thread_reeler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ThreadReelerMenu(i, inventory, this, this.data);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void craftItem() {
        Optional<RecipeHolder<ThreadReelerRecipe>> recipe = getCurrentRecipe();
        List<ItemStack> result = recipe.get().value().getResults(this.nextRollChance);

        this.inputItemHandler.extractItem(INPUT_SLOT, 1, false);

        if (result.get(0).is(ModItems.URO.get())){
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(this.level.getServer());
            ItemStack item = result.get(0);
            item.set(ModDataComponents.URO_ID, data.generateId());
            outputItems(item, null);
            return;
        }

        outputItems(result.get(0), result.size() == 1 ? null : result.get(1));
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<ThreadReelerRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        List<ItemStack> result = recipe.get().value().getResults(this.nextRollChance);
        return isOutputClear(result.get(0), result.size() == 1 ? null : result.get(1));
    }

    public Optional<RecipeHolder<ThreadReelerRecipe>> getCurrentRecipe() {
        SingleItemInput input = new SingleItemInput(inputItemHandler.getStackInSlot(0));
        return this.level.getRecipeManager().getRecipeFor(ThreadReelerRecipe.Type.INSTANCE, input, level);
    }

    @Override
    public void setAdditional(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean value = false;
        if (canRun() && getCurrentRecipe().get().value().getInputs().get(0).test(new ItemStack(ModItems.MOLTED_MOTH_WINGS.get()))){
            value = true;
        }

        if (getBlockState().getValue(ThreadReelerBlock.SPECIAL_PROCESS) != value){
            pState = pState.setValue(ThreadReelerBlock.SPECIAL_PROCESS, value);
            pLevel.setBlock(pPos, pState, 3);
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    }

    private PlayState predicate(AnimationState<ThreadReelerBlockEntity> threadReelerBlockEntityAnimationState) {
        threadReelerBlockEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.thread_reeler.rotate", Animation.LoopType.LOOP));
        return getBlockState().getValue(ThreadReelerBlock.RUNNING) ? PlayState.CONTINUE : PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}