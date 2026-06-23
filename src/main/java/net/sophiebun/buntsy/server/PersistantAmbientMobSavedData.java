package net.sophiebun.buntsy.server;

import com.ibm.icu.impl.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PersistantAmbientMobSavedData extends SavedData {

    private static final List<Pair<Integer, Integer>> chunkSpreadOrder =
            List.of(Pair.of(0, 1),
                    Pair.of(-1, 0),
                    Pair.of(1, 0),
                    Pair.of(0, -1),
                    Pair.of(1, 1),
                    Pair.of(-1, 1),
                    Pair.of(1, -1),
                    Pair.of(-1, -1));

    private final Map<String, Map<Pair<Integer, Integer>, Integer>> chunkSaturations;

    public void saturateChunk(String entityType, int chunkX, int chunkZ) {

        System.out.println("Saturating!");

        Map<Pair<Integer, Integer>, Integer> map = getMap(entityType);
        int actualSat = fishForChunk(map, chunkX, chunkZ) + 1;

        for (Pair<Integer, Integer> next : chunkSpreadOrder){
            int x = next.first, z = next.second;

            int spreadTargetSaturation = fishForChunk(map, chunkX + x, chunkZ + z);
            if (spreadTargetSaturation < actualSat - 1){
                addToChunk(map, chunkX + x, chunkZ + z, 1);
                setDirty();
                return;
            }
        }

        setChunk(map, chunkX, chunkZ, actualSat);
        setDirty();
    }


    public void deSaturateChunk(String entityType, int chunkX, int chunkZ) {

        System.out.println("deSaturating!");

        Map<Pair<Integer, Integer>, Integer> map = getMap(entityType);
        int actualSat = fishForChunk(map, chunkX, chunkZ) - 1;

        for (Pair<Integer, Integer> next : chunkSpreadOrder){
            int x = next.first, z = next.second;

            int spreadTargetSaturation = fishForChunk(map, chunkX + x, chunkZ + z);
            if (spreadTargetSaturation > actualSat + 1){
                subFromChunk(map, chunkX + x, chunkZ + z, 1);
                setDirty();
                return;
            }
        }

        setChunk(map, chunkX, chunkZ, actualSat);
        setDirty();
    }

    public boolean isChunkSaturated(String entityType, int chunkX, int chunkZ) {
        System.out.println("Is chunk saturated?");
        return fishForChunk(getMap(entityType), chunkX, chunkZ) > 0;
    }

    private Map<Pair<Integer, Integer>, Integer> getMap(String entityType){

        if (!chunkSaturations.containsKey(entityType)){
            chunkSaturations.put(entityType, new HashMap<>());
        }

        return  chunkSaturations.get(entityType);
    }

    private int fishForChunk(Map<Pair<Integer, Integer>, Integer> map, int x, int z){

        if (!map.containsKey(Pair.of(x, z))){
            map.put(Pair.of(x, z), 0);
        }

        return map.get(Pair.of(x, z));
    }

    private void addToChunk(Map<Pair<Integer, Integer>, Integer> map, int x, int z, int count){
        map.put(Pair.of(x, z), map.get(Pair.of(x, z)) + count);
    }

    private void subFromChunk(Map<Pair<Integer, Integer>, Integer> map, int x, int z, int count){
        map.put(Pair.of(x, z), map.get(Pair.of(x, z)) - count);
    }

    private void setChunk(Map<Pair<Integer, Integer>, Integer> map, int x, int z, int count){
        map.put(Pair.of(x, z), count);
    }

    PersistantAmbientMobSavedData(){
        chunkSaturations = new HashMap<>();
    }

    PersistantAmbientMobSavedData(Map<String, Map<Pair<Integer, Integer>, Integer>> map){
        chunkSaturations = map;
    }

    public static PersistantAmbientMobSavedData create(){
        return new PersistantAmbientMobSavedData();
    }

    public static PersistantAmbientMobSavedData create(Map<String, Map<Pair<Integer, Integer>, Integer>> map){
        return new PersistantAmbientMobSavedData(map);
    }

    public static PersistantAmbientMobSavedData load(CompoundTag tag){

        Map<String, Map<Pair<Integer, Integer>, Integer>> map = new HashMap<>();

        int entityCount = tag.getInt("entity_count");
        for (int x = 0; x < entityCount; x++){
            Map<Pair<Integer, Integer>, Integer> mapInner = new HashMap<>();
            map.put(tag.getString("entity_name_" + x), mapInner);

            int chunkCount = tag.getInt("chunk_count_" + x);
            for (int y = 0; y < chunkCount; y++){
                String ref = "chunks_" + x + "_number_" + y;
                mapInner.put(Pair.of(tag.getInt(ref + "_x"), tag.getInt(ref + "_z")), tag.getInt(ref + "_sat"));
            }
        }

        return PersistantAmbientMobSavedData.create(map);
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {

        pCompoundTag.putInt("entity_count", chunkSaturations.size());
        int x = 0;
        for (String name : chunkSaturations.keySet()){
            pCompoundTag.putString("entity_name_" + x, name);
            Map<Pair<Integer, Integer>, Integer> mapInner = chunkSaturations.get(name);
            pCompoundTag.putInt("chunk_count_" + x, mapInner.size());

            int y = 0;
            for (Pair<Integer, Integer> chunk : mapInner.keySet()){
                String ref = "chunks_" + x + "_number_" + y;
                pCompoundTag.putInt(ref + "_x", chunk.first);
                pCompoundTag.putInt(ref + "_z", chunk.second);
                pCompoundTag.putInt(ref + "_sat", mapInner.get(chunk));
                y++;
            }

            x++;
        }

        return pCompoundTag;
    }

    public static PersistantAmbientMobSavedData computeIfAbsent(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(PersistantAmbientMobSavedData::load, PersistantAmbientMobSavedData::create, "persistant_ambient_mob");
    }
}
