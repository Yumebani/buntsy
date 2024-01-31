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
import net.minecraft.util.Tuple;
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
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.recipe.ThreadReelerRecipe;
import net.sophiebun.buntsy.screen.GrindingWheelMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.antlr.v4.runtime.misc.MultiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GrindingWheelBlockEntity extends BasicFairyBlockEntity implements MenuProvider, GeoBlockEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<GrindingWheelBlockEntity> controller;

    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(Items.AMETHYST_SHARD), Map.of(
                    ModItems.AMETHYST_DUST.get(), ImmutableMultimap.of(1, 1f))),

            new TempRecipe(Ingredient.of(ItemTags.IRON_ORES), Map.of(
                    Items.RAW_IRON, ImmutableMultimap.of(2, 1f, 1, 0.5f),
                    ModItems.PRISTINE_IRON_SAMPLE.get(), ImmutableMultimap.of(1, 0.05f))),
            new TempRecipe(Ingredient.of(Items.RAW_IRON), Map.of(
                    ModItems.IRON_DUST.get(), ImmutableMultimap.of(1, 1f,1, 0.33f))),
            new TempRecipe(Ingredient.of(ModItems.IRON_CRYSTAL.get()), Map.of(
                    ModItems.IRON_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.33f))),

            new TempRecipe(Ingredient.of(ItemTags.COPPER_ORES), Map.of(
                    Items.RAW_COPPER, ImmutableMultimap.of(7, 1f, 1, 0.5f, 1, 0.25f),
                    ModItems.PRISTINE_COPPER_SAMPLE.get(), ImmutableMultimap.of(1, 0.10f))),
            new TempRecipe(Ingredient.of(Items.RAW_COPPER), Map.of(
                    ModItems.COPPER_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.33f))),
            new TempRecipe(Ingredient.of(ModItems.COPPER_CRYSTAL.get()), Map.of(
                    ModItems.COPPER_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.33f))),

            new TempRecipe(Ingredient.of(ItemTags.GOLD_ORES), Map.of(
                    Items.RAW_GOLD, ImmutableMultimap.of(2, 1f, 1, 0.5f),
                    ModItems.PRISTINE_GOLD_SAMPLE.get(), ImmutableMultimap.of(1, 0.05f))),
            new TempRecipe(Ingredient.of(Items.RAW_GOLD), Map.of(
                    ModItems.GOLD_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.33f))),
            new TempRecipe(Ingredient.of(ModItems.GOLD_CRYSTAL.get()), Map.of(
                    ModItems.GOLD_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.33f))),

            new TempRecipe(Ingredient.of(Items.ANCIENT_DEBRIS), Map.of(
                    ModItems.NETHERITE_DUST.get(), ImmutableMultimap.of(1, 1f, 1, 0.5f),
                    ModItems.PRISTINE_DEBRIS_SAMPLE.get(), ImmutableMultimap.of(1, 0.1f))),

            new TempRecipe(Ingredient.of(ItemTags.REDSTONE_ORES), Map.of(
                    Items.REDSTONE, ImmutableMultimap.of(6, 1f, 1, 0.5f, 1, 0.25f),
                    ModItems.PRISTINE_REDSTONE_SAMPLE.get(), ImmutableMultimap.of(1, 0.1f))),
            new TempRecipe(Ingredient.of(ModItems.REDSTONE_CRYSTAL.get()), Map.of(
                    Items.REDSTONE, ImmutableMultimap.of(2, 1f, 1, 0.5f))),

            new TempRecipe(Ingredient.of(ItemTags.LAPIS_ORES), Map.of(
                    Items.LAPIS_LAZULI, ImmutableMultimap.of(14, 1f, 2, 0.5f, 1, 0.5f),
                    ModItems.PRISTINE_LAPIS_SAMPLE.get(), ImmutableMultimap.of(1, 0.1f))),
            new TempRecipe(Ingredient.of(ModItems.LAPIS_CRYSTAL.get()), Map.of(
                    Items.LAPIS_LAZULI, ImmutableMultimap.of(4, 1f, 1, 0.5f, 1, 0.25f))),

            new TempRecipe(Ingredient.of(ItemTags.DIAMOND_ORES), Map.of(
                    Items.DIAMOND, ImmutableMultimap.of(2, 1f, 1, 0.5f),
                    ModItems.PRISTINE_DIAMOND_SAMPLE.get(), ImmutableMultimap.of(1, 0.1f))),

            new TempRecipe(Ingredient.of(ItemTags.EMERALD_ORES), Map.of(
                    Items.EMERALD, ImmutableMultimap.of(2, 1f, 1, 0.5f),
                    ModItems.PRISTINE_EMERALD_SAMPLE.get(), ImmutableMultimap.of(1, 0.1f))),

            new TempRecipe(Ingredient.of(Items.SUGAR_CANE), Map.of(
                    Items.SUGAR, ImmutableMultimap.of(2, 1f))),
            new TempRecipe(Ingredient.of(ModItems.HOOTNIP.get()), Map.of(
                    ModItems.GROUND_HOOTNIP.get(), ImmutableMultimap.of(1, 1f))));

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
}
