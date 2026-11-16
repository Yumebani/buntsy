package net.sophiebun.buntsy.server;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class PersistentAmbientMobRegulator extends SavedData {

    private int MAX_CONCENTRATION = 2;

    private Map<String, Map<Pair<Integer, Integer>, Integer>> data = new HashMap<>();

    public void saturate(String entity, ChunkPos pos){

        setDirty();

        Map<Pair<Integer, Integer>, Integer> localData = getDataByEntity(entity);
        Pair<Integer, Integer> localPos = Pair.of(pos.x, pos.z);

        int localSaturation = getLocalSaturation(localData, localPos);
        for (int x = -1; x <= 1; x++){
            for (int z = -1; z <= 1; z++){
                if (x != 0 || z != 0){
                    Pair<Integer, Integer> neighborPos = Pair.of(pos.x + x, pos.z + z);
                    int neighborSaturation = getLocalSaturation(localData, neighborPos);
                    if (neighborSaturation < localSaturation){
                        addLocalSaturation(localData, neighborPos);
                        return;
                    }
                }
            }
        }
        addLocalSaturation(localData, localPos);
    }

    public boolean isMaxSaturated(String entity, ChunkPos pos){

        setDirty();

        Map<Pair<Integer, Integer>, Integer> localData = getDataByEntity(entity);
        Pair<Integer, Integer> localPos = Pair.of(pos.x, pos.z);

        int localSaturation = getLocalSaturation(localData, localPos);
        for (int x = -1; x <= 1; x++){
            for (int z = -1; z <= 1; z++){
                if (x != 0 || z != 0){
                    Pair<Integer, Integer> neighborPos = Pair.of(pos.x + x, pos.z + z);
                    int neighborSaturation = getLocalSaturation(localData, neighborPos);
                    if (neighborSaturation < localSaturation){
                        return false;
                    }
                }
            }
        }
        return localSaturation >= MAX_CONCENTRATION;
    }

    public void desaturate(String entity, ChunkPos pos){

        setDirty();

        Map<Pair<Integer, Integer>, Integer> localData = getDataByEntity(entity);
        Pair<Integer, Integer> localPos = Pair.of(pos.x, pos.z);

        int localSaturation = getLocalSaturation(localData, localPos);
        for (int x = -1; x <= 1; x++){
            for (int z = -1; z <= 1; z++){
                if (x != 0 || z != 0){
                    Pair<Integer, Integer> neighborPos = Pair.of(pos.x + x, pos.z + z);
                    int neighborSaturation = getLocalSaturation(localData, neighborPos);
                    if (neighborSaturation > localSaturation){
                        subLocalSaturation(localData, neighborPos);
                        return;
                    }
                }
            }
        }
        subLocalSaturation(localData, localPos);
    }

    public void subLocalSaturation(Map<Pair<Integer, Integer>, Integer> localData, Pair<Integer, Integer> localPos){
        localData.put(localPos, localData.get(localPos) - 1);
    }

    public void addLocalSaturation(Map<Pair<Integer, Integer>, Integer> localData, Pair<Integer, Integer> localPos){
        localData.put(localPos, localData.get(localPos) + 1);
    }

    public int getLocalSaturation(Map<Pair<Integer, Integer>, Integer> localData, Pair<Integer, Integer> localPos){
        if (!localData.containsKey(localPos)){
            localData.put(localPos, 0);
        }
        return localData.get(localPos);
    }

    public Map<Pair<Integer, Integer>, Integer> getDataByEntity(String entity){
        if (!data.containsKey(entity)){
            data.put(entity, new HashMap<>());
        }
        return data.get(entity);
    }

    public PersistentAmbientMobRegulator loadData(Map<String, Map<Pair<Integer, Integer>, Integer>> data){
        this.data = data;
        return this;
    }

    public static PersistentAmbientMobRegulator create(){
        return new PersistentAmbientMobRegulator();
    }

    public static PersistentAmbientMobRegulator load(CompoundTag tag, HolderLookup.Provider registries){
        PersistentAmbientMobRegulator data = PersistentAmbientMobRegulator.create();

        Map<String, Map<Pair<Integer, Integer>, Integer>> dataLoad = new HashMap<>();
        int mobCount = tag.getInt("mob_count");
        for (int i = 0; i < mobCount; i++){
            String mobType = tag.getString("mob_" + i);
            Map<Pair<Integer, Integer>, Integer> localData = new HashMap<>();
            int loopCount = tag.getInt("mob_" + i + "_total");
            for (int ii = 0; i < loopCount; ii++){
                int posX = tag.getInt("mob_" + i + "_" + ii + "_posx");
                int posZ = tag.getInt("mob_" + i + "_" + ii + "_posz");
                int saturation = tag.getInt("mob_" + i + "_" + ii + "_saturation");
                localData.put(Pair.of(posX, posZ), saturation);
            }
            dataLoad.put(mobType, localData);
        }

        return data.loadData(dataLoad);
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag, HolderLookup.Provider registries) {
        Map<String, Map<Pair<Integer, Integer>, Integer>> dataLoad = new HashMap<>();
        pCompoundTag.putInt("mob_count", data.size());
        for (int i = 0; i < data.size(); i++){
            String mobType = data.keySet().stream().toList().get(i);
            pCompoundTag.putString("mob_" + i, mobType);
            pCompoundTag.putInt("mob_" + i + "_total", data.get(mobType).size());
            for (int ii = 0; i < data.get(mobType).size(); ii++){
                Pair<Integer, Integer> entry = data.get(mobType).keySet().stream().toList().get(ii);
                pCompoundTag.putInt("mob_" + i + "_" + ii + "_posx", entry.getFirst());
                pCompoundTag.putInt("mob_" + i + "_" + ii + "_posz", entry.getSecond());
                pCompoundTag.putInt("mob_" + i + "_" + ii + "_saturation", data.get(mobType).get(entry));
            }
        }
        return pCompoundTag;
    }

    public static final Factory<PersistentAmbientMobRegulator> FACTORY = new Factory<>(
            PersistentAmbientMobRegulator::create,
            PersistentAmbientMobRegulator::load,
            DataFixTypes.SAVED_DATA_MAP_DATA
    );

    public static PersistentAmbientMobRegulator computeIfAbsent(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, "persistent_ambient_mob_regulator");
    }
}
