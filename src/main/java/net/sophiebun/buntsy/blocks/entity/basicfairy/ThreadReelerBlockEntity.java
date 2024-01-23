package net.sophiebun.buntsy.blocks.entity.basicfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

import java.util.*;

public class ThreadReelerBlockEntity extends BasicFairyBlockEntity implements MenuProvider {

    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(ModItems.COCOON.get()), Map.of(
                    ModItems.SILK.get(), Map.of(3, 1f))),
            new TempRecipe(Ingredient.of(ModItems.MOLTED_MOTH_WINGS.get()), Map.of(
                    ModItems.MOTH_WING_THREAD.get(), Map.of(3, 1f))));

    public ThreadReelerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(), pPos, pBlockState);
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
}