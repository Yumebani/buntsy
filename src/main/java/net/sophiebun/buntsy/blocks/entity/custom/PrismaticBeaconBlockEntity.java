package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.screen.GiantCocoonMenu;
import net.sophiebun.buntsy.server.GiantCocoonSavedData;
import net.sophiebun.buntsy.server.ModGiantCocoonServerPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;
import net.sophiebun.buntsy.server.PrismaticBeaconSavedData;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PrismaticBeaconBlockEntity extends BlockEntity {

    private static final Block BASE_BLOCK = ModBlocks.PRISMATIC_BEACON_BASE.get();

    private int[] startingPoint = {-3, 0, -3};

    private static final String[][] beaconStructure = {
            {
            "OOOOOOO",
            "OEOOOEO",
            "OOOOOOO",
            "OOOOOOO",
            "OOOOOOO",
            "OEOOOEO",
            "OOOOOOO"
        },{
            "O#OOO#O",
            "#OOOOO#",
            "OOOOOOO",
            "OOOOOOO",
            "OOOOOOO",
            "#OOOOO#",
            "O#OOO#O"
        },{
            "O#OOO#O",
            "#O###O#",
            "O#####O",
            "O#####O",
            "O#####O",
            "#O###O#",
            "O#OOO#O"
        },{
            "#O###O#",
            "OOOOOOO",
            "#O###O#",
            "#O###O#",
            "#O###O#",
            "OOOOOOO",
            "#O###O#"
        },{
            "O#OOO#O",
            "#OOOOO#",
            "OOOOOOO",
            "OOOOOOO",
            "OOOOOOO",
            "#OOOOO#",
            "O#OOO#O"
        }
    };

    private static final Map<Block, MobEffect> effectMap = Map.of(
        ModBlocks.BEACON_HASTE_MODIFIER.get(), MobEffects.DIG_SPEED,
        ModBlocks.BEACON_FIRE_RESISTANCE_MODIFIER.get(), MobEffects.FIRE_RESISTANCE,
        ModBlocks.BEACON_HEALTH_BOOST_MODIFIER.get(), MobEffects.HEALTH_BOOST,
        ModBlocks.BEACON_REGENERATION_MODIFIER.get(), MobEffects.REGENERATION,
        ModBlocks.BEACON_JUMP_BOOST_MODIFIER.get(), MobEffects.JUMP,
        ModBlocks.BEACON_RESISTANCE_MODIFIER.get(), MobEffects.DAMAGE_RESISTANCE,
        ModBlocks.BEACON_SPEED_MODIFIER.get(), MobEffects.MOVEMENT_SPEED,
        ModBlocks.BEACON_STRENGTH_MODIFIER.get(), MobEffects.DAMAGE_BOOST,
        ModBlocks.BEACON_WATER_BREATHING_MODIFIER.get(), MobEffects.WATER_BREATHING
    );

    private static final int MAX_EFFECT_LEVEL = 2;
    private static final int MAX_CHECK_TICK = 20;

    private int checkTick = 0;
    private UUID assignedPlayer;
    private int blockId = -1;
    private boolean unloaded = false;

    public PrismaticBeaconBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PRISMATIC_BEACON_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    public void assignNewPlayer(UUID uuid){
        this.assignedPlayer = uuid;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("prismatic.has_player", assignedPlayer != null);
        if (assignedPlayer != null){
            pTag.putUUID("prismatic.assigned_player", assignedPlayer);
        }
        pTag.putInt("prismatic.block_id", blockId);
        pTag.putBoolean("prismatic.unloaded", this.unloaded);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.getBoolean("prismatic.has_player")){
            this.assignedPlayer = pTag.getUUID("prismatic.assigned_player");
        }
        this.blockId = pTag.getInt("prismatic.block_id");
        this.unloaded = pTag.getBoolean("prismatic.unloaded");

    }

    @Override
    public void onChunkUnloaded() {
        if (!level.isClientSide()){
            unloaded = true;
            PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(this.level.getServer());
            data.unloadBeacon(blockId);
        }
        super.onChunkUnloaded();
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (blockId == -1){
            PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
            blockId = data.generateId();
        }

        if (this.unloaded){
            PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
            data.loadBeacon(blockId);
            this.unloaded = false;
        }

        if (assignedPlayer != null){

            if (checkTick < MAX_CHECK_TICK){
                checkTick++;
            }
            else{
                if (beaconIsValid(pLevel, pPos)){
                    applyBeaconEffects(pLevel, pPos);
                }
                checkTick = 0;
            }
        }
    }

    private void applyBeaconEffects(Level pLevel, BlockPos pPos) {
        Map<MobEffect, Integer> effects = new HashMap<>();
        for (int x = startingPoint[0]; x <= -startingPoint[0]; x++){
            for (int y = startingPoint[1]; y < beaconStructure.length; y++){
                for (int z = startingPoint[2]; z <= -startingPoint[2]; z++){
                    if (beaconStructure[y][z-startingPoint[2]].charAt(x-startingPoint[0]) == 'E'){
                        if (pLevel.getBlockState(pPos.offset(x, -y + 1, z)).is(ModTags.Blocks.PRISMATIC_BEACON_EFFECT_BLOCK)){
                            MobEffect effect = effectMap.get(pLevel.getBlockState(pPos.offset(x, -y + 1, z)).getBlock());
                            if (!effects.containsKey(effect)){
                                effects.put(effect, 1);
                            }
                            else {
                                if (effects.get(effect) != MAX_EFFECT_LEVEL){
                                    effects.put(effect, effects.get(effect) + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        List<Tuple<MobEffect, Integer>> finalValues = new ArrayList<>();
        for (MobEffect effect : effects.keySet()){
            finalValues.add(new Tuple<>(effect, effects.get(effect)));
        }

        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
        data.registerNewBeaconData(blockId, new Tuple<>(assignedPlayer, finalValues));
    }

    public void removeData(Level pLevel) {
        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
        data.unregisterBeaconData(blockId);
    }

    private boolean beaconIsValid(Level pLevel, BlockPos pPos) {
        for (int x = startingPoint[0]; x <= -startingPoint[0]; x++){
            for (int y = startingPoint[1]; y < beaconStructure.length; y++){
                for (int z = startingPoint[2]; z <= -startingPoint[2]; z++){
                    if (beaconStructure[y][z-startingPoint[2]].charAt(x-startingPoint[0]) == '#'){
                        if (!pLevel.getBlockState(pPos.offset(x, -y + 1, z)).is(BASE_BLOCK)){
                            System.out.println("base block not present at " + x + " " + (-y + 1) + " " + z);
                            return false;
                        }
                    }
                    else if (beaconStructure[y][z-startingPoint[2]].charAt(x-startingPoint[0]) == 'E'){
                        if (!pLevel.getBlockState(pPos.offset(x, -y + 1, z)).is(ModTags.Blocks.PRISMATIC_BEACON_EFFECT_BLOCK)){
                            System.out.println("effect block not present at " + x + " " + (-y + 1) + " " + z);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
