package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.LevelCapabilityData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.recipe.MagicCrystalizerRecipe;
import net.sophiebun.buntsy.screen.GiantCocoonMenu;
import net.sophiebun.buntsy.server.GiantCocoonSavedData;
import net.sophiebun.buntsy.server.ModGiantCocoonServerPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GiantCocoonBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler uroItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private boolean changedStack = false;

    private ItemStackHandler contentItemHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            changedStack = true;
        }
    };

    private boolean hasUro = false;

    protected final ContainerData data;

    private LazyOptional<IItemHandler> uroLazyItemHandler = LazyOptional.of(() -> uroItemHandler);

    private LazyOptional<IItemHandler> contentLazyItemHandler = LazyOptional.of(this::getContentItemHandler);
    private boolean initialized = false;

    public ItemStackHandler getContentItemHandler(){
        return contentItemHandler;
    }
    public void setContentItemHandler(CompoundTag tag){
        contentItemHandler.deserializeNBT(tag);
    }

    private int getUroId(){
        return this.getUro().getTag().getInt("buntsy.uro_id");
    }

    public LazyOptional<IItemHandler> getContentLazyItemHandler(Level pLevel) {
        return contentLazyItemHandler;
    }

    public GiantCocoonBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_COCOON_BLOCK_ENTITY.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> GiantCocoonBlockEntity.this.hasUro ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> GiantCocoonBlockEntity.this.hasUro = i1 == 1;
                };
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.giant_cocoon");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GiantCocoonMenu(i, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (!level.isClientSide() && this.hasUro){
                contentItemHandler = getInvData(level);
                return contentLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public ItemStackHandler getInvData(Level pLevel){
        GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(pLevel.getServer());
        return data.registerNewCocoon(this.getUro().getTag().getInt("buntsy.uro_id"), this);
    }

    public ItemStack getUro() {
        return uroItemHandler.getStackInSlot(0);
    }

    public boolean hasUro() {
        return hasUro;
    }

    public ItemStack extractUro(){
        if (!level.isClientSide()){
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(level.getServer());
            data.unregisterCocoon(this.getUroId(), this);
            this.hasUro = false;
            return uroItemHandler.extractItem(0, 1, false);
        }
        return ItemStack.EMPTY;
    }

    public void setUro(ItemStack uro, Level pLevel){
        uroItemHandler.setStackInSlot(0, uro);
        contentItemHandler = getInvData(pLevel);
        this.hasUro = true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        contentLazyItemHandler = LazyOptional.of((this::getContentItemHandler));
        uroLazyItemHandler = LazyOptional.of(() -> uroItemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        contentLazyItemHandler.invalidate();
        uroLazyItemHandler.invalidate();
    }

    public void drops() {
        if (!uroItemHandler.getStackInSlot(0).isEmpty()){
            SimpleContainer inventory = new SimpleContainer(1);
            inventory.setItem(0, extractUro());
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", uroItemHandler.serializeNBT());
        pTag.putBoolean("has_uro", this.hasUro());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.uroItemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.hasUro = pTag.getBoolean("has_uro");
        this.initialized = false;

    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (pLevel.isClientSide()){
            if (changedStack){
                ModPacketHandler.INSTANCE.sendToServer(new ModGiantCocoonServerPacket(getUroId(), contentItemHandler.serializeNBT(), getBlockPos()));
                changedStack = false;
            }
        }
        else if (!initialized){
            if (!uroItemHandler.getStackInSlot(0).isEmpty()){
                contentItemHandler = getInvData(pLevel);
            }
            initialized = true;
        }
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}
