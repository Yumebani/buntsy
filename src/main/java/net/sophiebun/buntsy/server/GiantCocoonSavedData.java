package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import org.checkerframework.checker.units.qual.C;

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


    public void loadNewStackHandler(int key, CompoundTag nbt){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(key);
        }
        stackHandlers.get(key).deserializeNBT(nbt);
    }

    public void packetUpdate(int key, CompoundTag nbt, BlockPos origin){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(key);
        }

        stackHandlers.get(key).deserializeNBT(nbt);

        distributePackets(key, origin, null);
    }

    public void packetUpdatePlayer(int key, CompoundTag nbt, ServerPlayer player){
        if (!stackHandlers.containsKey(key)){
            createNewStackHandler(key);
        }

        stackHandlers.get(key).deserializeNBT(nbt);

        distributePackets(key, null, player);
    }

    public void distributePackets(int key, BlockPos origin, ServerPlayer originPlayer){
        if (cocoons.containsKey(key)){
            for (GiantCocoonBlockEntity cocoon : cocoons.get(key)){
                if (origin == null || !cocoon.getBlockPos().equals(origin)){
                    ModPacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> cocoon.getLevel().dimension()),
                            new ModGiantCocoonClientPacket(stackHandlers.get(key).serializeNBT(), cocoon.getBlockPos()));
                }
            }
        }

        if (playersToUpdate.containsKey(key)){
            for (ServerPlayer player : playersToUpdate.get(key).values()){
                if (originPlayer == null || !player.getUUID().equals(originPlayer.getUUID())){
                    updatePlayerItemTag(stackHandlers.get(key).serializeNBT(), player);
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

        if (!handItem.isEmpty() && handItem.hasTag() && handItem.is(ModItems.COCOON_BAG.get())){
            tag.putInt("buntsy.uro_id", CocoonBag.getUroId(handItem));
            handItem.setTag(tag);
        }
        else {
            tag.putInt("buntsy.uro_id", CocoonBag.getUroId(offHand));
            offHand.setTag(tag);
        }
    }

    private void createNewStackHandler(int key){
        stackHandlers.put(key, new ItemStackHandler(27) {
            @Override
            protected void onContentsChanged(int slot) {

                distributePackets(key, null, null);
                setDirty();
            }
        });
    }
    public void distributePacket(int id, ServerPlayer player) {
        if (!stackHandlers.containsKey(id)){
            createNewStackHandler(id);
        }

        updatePlayerItemTag(stackHandlers.get(id).serializeNBT(), player);

    }

    public ItemStackHandler registerNewCocoon(int id, GiantCocoonBlockEntity cocoon){
        if (!stackHandlers.containsKey(id)){
            createNewStackHandler(id);
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

    public static GiantCocoonSavedData load(CompoundTag tag){
        GiantCocoonSavedData data = GiantCocoonSavedData.create();
        data.setCurrentId(tag.getInt("current_id"));
        int loopCount = tag.getInt("inventory_count");
        for (int i = 0; i < loopCount; i++){
            data.loadNewStackHandler(tag.getInt("inventory_id_" + i), tag.getCompound("inventory_" + i));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("current_id", currentId);
        pCompoundTag.putInt("inventory_count", stackHandlers.size());
        List<Integer> keys = stackHandlers.keySet().stream().toList();
        for (int i = 0; i < stackHandlers.size(); i++){
            pCompoundTag.putInt("inventory_id_" + i, keys.get(i));
            pCompoundTag.put("inventory_" + i, stackHandlers.get(i).serializeNBT());
        }
        return pCompoundTag;
    }

    public static GiantCocoonSavedData computeIfAbsent(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(GiantCocoonSavedData::load, GiantCocoonSavedData::create, "cocoons");
    }

}
