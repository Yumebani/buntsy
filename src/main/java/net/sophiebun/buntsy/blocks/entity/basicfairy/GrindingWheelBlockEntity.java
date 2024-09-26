package net.sophiebun.buntsy.blocks.entity.basicfairy;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.screen.GrindingWheelMenu;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Optional;

public class GrindingWheelBlockEntity extends BasicFairyBlockEntity implements MenuProvider, GeoBlockEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<GrindingWheelBlockEntity> controller;

    public GrindingWheelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(1f);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.grinding_wheel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GrindingWheelMenu(i, inventory, this, this.data);
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

    private PlayState predicate(AnimationState<GrindingWheelBlockEntity> threadReelerBlockEntityAnimationState) {
        threadReelerBlockEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.grinding_wheel.rotate", Animation.LoopType.LOOP));
        return getBlockState().getValue(ThreadReelerBlock.RUNNING) ? PlayState.CONTINUE : PlayState.STOP;
    }

    public void craftItem() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();
        List<ItemStack> result = recipe.get().getResults(this.nextRollChance);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        outputItems(result.get(0), result.size() == 1 ? null : result.get(1));
    }

    public boolean hasRecipe() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return false;
        }

        List<ItemStack> result = recipe.get().getResults(this.nextRollChance);
        return isOutputClear(result.get(0), result.size() == 1 ? null : result.get(1));
    }

    public Optional<GrindingWheelRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GrindingWheelRecipe.Type.INSTANCE, inventory, level);
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
}
