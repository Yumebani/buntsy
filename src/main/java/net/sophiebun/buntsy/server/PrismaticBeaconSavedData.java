package net.sophiebun.buntsy.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PrismaticBeaconSavedData extends SavedData {

    private int currentBeaconId = 0;
    private Map<Integer, Tuple<UUID, List<Tuple<MobEffect, Integer>>>> playerEffects = new HashMap<>();
    private Map<Integer, Boolean> validMap = new HashMap<>();
    private int lastTick = 0;

    public int getLastTick(){
        return lastTick;
    }

    public void resetLastTick(){
        lastTick = 0;
    }

    public int generateId(){
        currentBeaconId = currentBeaconId + 1;
        setDirty();
        return currentBeaconId - 1;
    }

    public Map<Integer, Boolean> getValid() {
        return validMap;
    }

    public Map<Integer, Tuple<UUID, List<Tuple<MobEffect, Integer>>>> getPlayerEffects() {
        return playerEffects;
    }

    public void updateBeaconEffects(int id, Tuple<UUID, List<Tuple<MobEffect, Integer>>> effects){
        playerEffects.put(id, effects);
        setDirty();
    }

    public void updateBeaconValidity(int id, boolean valid){
        validMap.put(id, valid);
        setDirty();
    }

    public void registerNewBeaconData(int id){
        playerEffects.put(id, null);
        validMap.put(id, false);
        setDirty();
    }

    public void unregisterBeaconData(int blockId) {
        playerEffects.remove(blockId);
        validMap.remove(blockId);
        setDirty();
    }

    public static PrismaticBeaconSavedData create(){
        return new PrismaticBeaconSavedData();
    }

    public void setCurrentBeaconId(int currentId) {
        this.currentBeaconId = currentId;
    }

    public static PrismaticBeaconSavedData load(CompoundTag tag){
        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.create();
        data.setCurrentBeaconId(tag.getInt("current_beacon_id"));
        int loopCount = tag.getInt("player_effects_count");
        for (int i = 0; i < loopCount; i++){
            int beaconId = tag.getInt("beacon_id_" + i);
            UUID playerUUID = tag.getUUID("player_uuid_" + i);
            boolean valid = tag.getBoolean("beacon_validity_" + i);
            List<Tuple<MobEffect, Integer>> finalValues = new ArrayList<>();
            int loopCount2 = tag.getInt("effects_count_" + i);
            for (int j = 0; j < loopCount2; j++){
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.getHolder(tag.getInt("potion_id_" + i + "_" + j)).get().get();
                int effectStrength = tag.getInt("effect_strength_" + i + "_" + j);
                finalValues.add(new Tuple<>(effect, effectStrength));
            }
            data.registerNewBeaconData(beaconId);
            data.updateBeaconEffects(beaconId, new Tuple<>(playerUUID, finalValues));
            data.updateBeaconValidity(beaconId, valid);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("current_beacon_id", currentBeaconId);
        pCompoundTag.putInt("player_effects_count", playerEffects.size());
        List<Integer> keys = playerEffects.keySet().stream().toList();
        for (int i = 0; i < playerEffects.size(); i++){
            Tuple<UUID, List<Tuple<MobEffect, Integer>>> vals = playerEffects.get(keys.get(i));
            pCompoundTag.putInt("beacon_id_" + i, keys.get(i));
            pCompoundTag.putUUID("player_uuid_" + i, vals.getA());
            pCompoundTag.putBoolean("beacon_validity_" + i, validMap.get(keys.get(i)));
            pCompoundTag.putInt("effects_count_" + i, vals.getB().size());
            for (int j = 0; j < vals.getB().size(); j++){
                pCompoundTag.putInt("potion_id_" + i + "_" + j, BuiltInRegistries.MOB_EFFECT.getId(vals.getB().get(j).getA()));
                pCompoundTag.putInt("effect_strength_" + i + "_" + j, vals.getB().get(j).getB());
            }
        }
        return pCompoundTag;
    }

    public static PrismaticBeaconSavedData computeIfAbsent(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(PrismaticBeaconSavedData::load, PrismaticBeaconSavedData::create, "prismatic_data");
    }

    public void tickUp() {
        lastTick++;
    }
}
