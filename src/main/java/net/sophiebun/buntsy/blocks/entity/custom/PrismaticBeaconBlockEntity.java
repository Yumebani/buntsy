package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
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

    private static final Map<Block, Holder<MobEffect>> effectMap = Map.of(
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
    private boolean valid = false;

    public PrismaticBeaconBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PRISMATIC_BEACON_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void assignNewPlayer(UUID uuid){
        this.assignedPlayer = uuid;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.putBoolean("prismatic.has_player", assignedPlayer != null);
        if (assignedPlayer != null){
            pTag.putUUID("prismatic.assigned_player", assignedPlayer);
        }
        pTag.putInt("prismatic.block_id", blockId);
        pTag.putBoolean("prismatic.valid", this.valid);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        if (pTag.getBoolean("prismatic.has_player")){
            this.assignedPlayer = pTag.getUUID("prismatic.assigned_player");
        }
        this.blockId = pTag.getInt("prismatic.block_id");
        this.valid = pTag.getBoolean("prismatic.valid");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (blockId == -1){
            PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
            blockId = data.generateId();
            data.registerNewBeaconData(blockId);
        }

        if (assignedPlayer != null){

            if (checkTick < MAX_CHECK_TICK){
                checkTick++;
            }
            else{

                PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
                if (beaconIsValid(pLevel, pPos)){
                    data.updateBeaconValidity(blockId, true);
                    applyBeaconEffects(pLevel, pPos);
                } else {
                    data.updateBeaconValidity(blockId, false);
                }

                checkTick = 0;
            }
        }
    }

    private void applyBeaconEffects(Level pLevel, BlockPos pPos) {
        Map<Holder<MobEffect>, Integer> effects = new HashMap<>();
        for (int x = startingPoint[0]; x <= -startingPoint[0]; x++){
            for (int y = startingPoint[1]; y < beaconStructure.length; y++){
                for (int z = startingPoint[2]; z <= -startingPoint[2]; z++){
                    if (beaconStructure[y][z-startingPoint[2]].charAt(x-startingPoint[0]) == 'E'){
                        if (pLevel.getBlockState(pPos.offset(x, -y + 1, z)).is(ModTags.Blocks.PRISMATIC_BEACON_EFFECT_BLOCK)){
                            Holder<MobEffect> effect = effectMap.get(pLevel.getBlockState(pPos.offset(x, -y + 1, z)).getBlock());
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

        List<Tuple<Holder<MobEffect>, Integer>> finalValues = new ArrayList<>();
        for (Holder<MobEffect> effect : effects.keySet()){
            finalValues.add(new Tuple<>(effect, effects.get(effect)));
        }

        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(pLevel.getServer());
        data.updateBeaconEffects(blockId, new Tuple<>(assignedPlayer, finalValues));
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
