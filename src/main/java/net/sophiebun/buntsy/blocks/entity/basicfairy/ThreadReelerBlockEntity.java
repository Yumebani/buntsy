package net.sophiebun.buntsy.blocks.entity.basicfairy;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.basicfairy.BasicFairyBlockEntity;
import net.sophiebun.buntsy.blocks.inventory.OutputSlot;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.recipe.ThreadReelerRecipe;
import net.sophiebun.buntsy.screen.ThreadReelerMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class ThreadReelerBlockEntity extends BasicFairyBlockEntity implements MenuProvider, GeoBlockEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<ThreadReelerBlockEntity> controller;

    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(ModItems.COCOON.get()), Map.of(
                    ModItems.SILK.get(), ImmutableMultimap.of(3, 1f))),

            new TempRecipe(Ingredient.of(ItemTags.WOOL), Map.of(
                    Items.STRING, ImmutableMultimap.of(3, 1f))),

            new TempRecipe(Ingredient.of(ModItems.MOLTED_MOTH_WINGS.get()), Map.of(
                    ModItems.MOTH_WING_THREAD.get(), ImmutableMultimap.of(3, 1f))));

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

    @Override
    public List<TempRecipe> getRecipeList() {
        return recipeList;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void setAdditional(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean value = false;
        if (canRun() && tempGetCurrentRecipe().getIngredients().getItems()[0].is(ModItems.MOLTED_MOTH_WINGS.get())){
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controllers.add(controller);
    }
    private PlayState predicate(AnimationState<ThreadReelerBlockEntity> threadReelerBlockEntityAnimationState) {
        threadReelerBlockEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.thread_reeler.rotate", Animation.LoopType.LOOP));
        return getBlockState().getValue(ThreadReelerBlock.RUNNING) ? PlayState.CONTINUE : PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}