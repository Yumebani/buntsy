package net.sophiebun.buntsy.blocks.entity.basicfairy;

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
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.basicfairy.BasicFairyBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.recipe.TempRecipe;
import net.sophiebun.buntsy.recipe.ThreadReelerRecipe;
import net.sophiebun.buntsy.screen.GrindingWheelMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GrindingWheelBlockEntity extends BasicFairyBlockEntity implements MenuProvider {


    //TEMPORARY
    private static final List<TempRecipe> recipeList = List.of(
            new TempRecipe(Ingredient.of(Items.AMETHYST_SHARD), Map.of(
                    ModItems.AMETHYST_DUST.get(), Map.of(1, 1f))),
            new TempRecipe(Ingredient.of(Items.SUGAR_CANE), Map.of(
                    Items.SUGAR, Map.of(2, 1f))),
            new TempRecipe(Ingredient.of(ModItems.HOOTNIP.get()), Map.of(
                    ModItems.GROUND_HOOTNIP.get(), Map.of(1, 1f))));

    public GrindingWheelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(), pPos, pBlockState);
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
