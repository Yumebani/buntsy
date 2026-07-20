package net.sophiebun.buntsy.server;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;
import net.sophiebun.buntsy.codec.UroContent;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.server.packets.GiantCocoonClientPacket;

import java.util.*;

public class GiantCocoonSavedData extends SavedData {

    private int currentId = 0;
    private Map<Integer, ItemStackHandler> stackHandlers = new HashMap<>();
    private Map<Integer, List<GiantCocoonBlockEntity>> cocoons = new HashMap<>();
    private Map<Integer, Map<UUID, ServerPlayer>> playersToUpdate = new HashMap<>();

    public int generateId(){
        currentId = currentId + 1;
        setDirty();
        return currentId - 1;
    }

    public void registerNewPlayer(int id, ServerPlayer player){
        if (!playersToUpdate.containsKey(id)){
            playersToUpdate.put(id, new HashMap<>());
        }

        playersToUpdate.get(id).put(player.getUUID(), player);
    }

    public void unregisterNewPlayer(int id, ServerPlayer player){

        if (playersToUpdate.get(id).containsKey(player.getUUID())){
            playersToUpdate.get(id).remove(player.getUUID());
        }
    }


    public void loadNewStackHandler(HolderLookup.Provider registries, int key, CompoundTag nbt){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(registries, key);
        }
        stackHandlers.get(key).deserializeNBT(registries, nbt);
    }

    public void packetUpdate(HolderLookup.Provider registries, int key, CompoundTag nbt, BlockPos origin){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(registries, key);
        }

        stackHandlers.get(key).deserializeNBT(registries, nbt);

        distributePackets(registries, key, origin, null);
    }

    public void packetUpdatePlayer(HolderLookup.Provider registries, int key, CompoundTag nbt, ServerPlayer player){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(registries, key);
        }

        stackHandlers.get(key).deserializeNBT(registries, nbt);

        distributePackets(registries, key, null, player);
    }

    public void distributePackets(HolderLookup.Provider registries, int key, BlockPos origin, ServerPlayer originPlayer){
        if (cocoons.containsKey(key)){
            for (GiantCocoonBlockEntity cocoon : cocoons.get(key)){
                if (origin == null || !cocoon.getBlockPos().equals(origin)){
                    PacketDistributor.sendToPlayersInDimension(((ServerLevel) cocoon.getLevel()),
                            new GiantCocoonClientPacket(stackHandlers.get(key).serializeNBT(registries), cocoon.getBlockPos()));
                }
            }
        }

        if (playersToUpdate.containsKey(key)){
            for (ServerPlayer player : playersToUpdate.get(key).values()){
                if (originPlayer == null || !player.getUUID().equals(originPlayer.getUUID())){
                    updatePlayerItemTag(stackHandlers.get(key).serializeNBT(registries), player);
                }
            }
        }
    }

    private void updatePlayerItemTag(CompoundTag itemHandlerTag, ServerPlayer player){

        CompoundTag tag = new CompoundTag();
        tag.put("uro_contents", itemHandlerTag);
        tag.putBoolean("in_update", true);

        ItemStack handItem = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (!handItem.isEmpty() && handItem.is(ModItems.COCOON_BAG.get())){
            handItem.set(ModDataComponents.URO_CONTENT, new UroContent(itemHandlerTag, true, false));
        }
        else {
            offHand.set(ModDataComponents.URO_CONTENT, new UroContent(itemHandlerTag, true, false));
        }
    }

    private void createNewStackHandler(HolderLookup.Provider provider, int key){
        stackHandlers.put(key, new ItemStackHandler(27) {
            @Override
            protected void onContentsChanged(int slot) {

                distributePackets(provider, key, null, null);
                setDirty();
            }
        });
    }
    public void distributePacket(HolderLookup.Provider registries, int id, ServerPlayer player) {
        if (!stackHandlers.containsKey(id)){
            createNewStackHandler(registries, id);
        }

        updatePlayerItemTag(stackHandlers.get(id).serializeNBT(registries), player);

    }

    public ItemStackHandler registerNewCocoon(HolderLookup.Provider registries, int id, GiantCocoonBlockEntity cocoon){
        if (!stackHandlers.containsKey(id)){
            createNewStackHandler(registries, id);
        }

        cocoons.computeIfAbsent(id, k -> new ArrayList<>());

        if (!cocoons.get(id).contains(cocoon)){
            cocoons.get(id).add(cocoon);
        }

        return stackHandlers.get(id);
    }

    public void unregisterCocoon(int id, GiantCocoonBlockEntity cocoon){

        if (cocoons.get(id).contains(cocoon)){
            cocoons.get(id).remove(cocoon);
        }
    }

    public static GiantCocoonSavedData create(){
        return new GiantCocoonSavedData();
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public static GiantCocoonSavedData load(CompoundTag tag, HolderLookup.Provider levelRegistry){
        GiantCocoonSavedData data = GiantCocoonSavedData.create();
        data.setCurrentId(tag.getInt("current_id"));
        int loopCount = tag.getInt("inventory_count");
        for (int i = 0; i < loopCount; i++){
            data.loadNewStackHandler(levelRegistry, tag.getInt("inventory_id_" + i), tag.getCompound("inventory_" + i));
        }
        return data;
    }

    public static final SavedData.Factory<GiantCocoonSavedData> FACTORY = new SavedData.Factory<>(
            GiantCocoonSavedData::create,
            GiantCocoonSavedData::load,
            DataFixTypes.SAVED_DATA_MAP_DATA
    );

    public static GiantCocoonSavedData computeIfAbsent(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, "cocoons_data");
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag, HolderLookup.Provider provider) {
        pCompoundTag.putInt("current_id", currentId);
        pCompoundTag.putInt("inventory_count", stackHandlers.size());
        List<Integer> keys = stackHandlers.keySet().stream().toList();
        for (int i = 0; i < stackHandlers.size(); i++){
            pCompoundTag.putInt("inventory_id_" + i, keys.get(i));
            pCompoundTag.put("inventory_" + i, stackHandlers.get(i).serializeNBT(provider));
        }
        return pCompoundTag;

    }
}
