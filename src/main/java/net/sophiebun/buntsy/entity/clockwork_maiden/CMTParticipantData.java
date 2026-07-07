package net.sophiebun.buntsy.entity.clockwork_maiden;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.sophiebun.buntsy.screen.CMTParticipantScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMTParticipantData {

    private final Map<Integer, boolean[]> enablings;
    private final Map<Integer, MaidenInteractionConfig> insertConfigs;
    private final Map<Integer, MaidenInteractionConfig> extractConfigs;

    private final Map<Integer, boolean[]> changed;

    public CMTParticipantData(
            Map<Integer, boolean[]> enablings,
            Map<Integer, MaidenInteractionConfig> insertConfigs,
            Map<Integer, MaidenInteractionConfig> extractConfigs,
            Map<Integer, boolean[]> changed){

        this.enablings = enablings;
        this.insertConfigs = insertConfigs;
        this.extractConfigs = extractConfigs;
        this.changed = changed;
    }


    public CMTParticipantData(){
        this.insertConfigs = new HashMap<>();
        this.extractConfigs = new HashMap<>();
        this.enablings = new HashMap<>();

        for (int i = 0; i < CMTParticipantScreen.MAX_CHANNELS; i++){
            this.enablings.put(i, new boolean[]{false, false});
        }

        this.changed = new HashMap<>();
    }

    public void setConfig(boolean inserting, int channel, MaidenInteractionConfig config){
        if (inserting){
            insertConfigs.put(channel, config.copy());
        } else {
            extractConfigs.put(channel, config.copy());
        }
    }

    public Map<Integer, boolean[]> getChanged(){
        return this.changed;
    }

    public MaidenInteractionConfig getConfig(boolean inserting, int channel){
        return (inserting ? insertConfigs : extractConfigs).get(channel);
    }

    public Map<Integer, MaidenInteractionConfig> getAllConfigs(boolean inserting){
        return (inserting ? insertConfigs : extractConfigs);
    }

    public void setChanged(boolean inserting, int channel){
        if (!this.changed.containsKey(channel)){
            this.changed.put(channel, new boolean[]{inserting, !inserting});
        } else {
            this.changed.get(channel)[inserting ? 0 : 1] = true;
        }
    }

    public boolean isEnabled(boolean inserting, int channel){
        return enablings.get(channel)[inserting ? 0 : 1];
    }

    public void setEnabled(boolean inserting, int channel, boolean value){
        enablings.get(channel)[inserting ? 0 : 1] = value;
    }

    public void checkOrMakeChannel(boolean inserting, int channel, BlockPos pos) {
        Map<Integer, MaidenInteractionConfig> configs = inserting ? insertConfigs : extractConfigs;
        if (!configs.containsKey(channel)){
            configs.put(channel, MaidenInteractionConfig.makeNewConfig(inserting, pos));
        }
    }

    public CompoundTag getCompound() {

        CompoundTag tag = new CompoundTag();

        for (int i = 0; i < CMTParticipantScreen.MAX_CHANNELS; i++){
            tag.putBoolean("cmt_data.inserting_enabled_" + i, enablings.get(i)[0]);
            tag.putBoolean("cmt_data.extracting_enabled_" + i, enablings.get(i)[1]);
        }

        tag.putInt("cmt_data.inserting_config_size", insertConfigs.size());
        int i = 0;
        for (Integer key : insertConfigs.keySet()){
            tag.putInt("cmt_data.inserting_config_key_" + i, key);
            tag.put("cmt_data.inserting_config_" + i, insertConfigs.get(key).getCompound());
            i++;
        }

        tag.putInt("cmt_data.extracting_config_size", extractConfigs.size());
        i = 0;
        for (Integer key : extractConfigs.keySet()){
            tag.putInt("cmt_data.extracting_config_key_" + i, key);
            tag.put("cmt_data.extracting_config_" + i, extractConfigs.get(key).getCompound());
            i++;
        }

        tag.putInt("cmt_data.changed_size", changed.size());
        i = 0;
        for (Integer key : changed.keySet()){
            tag.putInt("cmt_data.changed_key_" + i, key);
            tag.putBoolean("cmt_data.inserting_changed_" + i, changed.get(key)[0]);
            tag.putBoolean("cmt_data.extracting_changed_" + i, changed.get(key)[1]);
            i++;
        }

        return tag;
    }

    public static CMTParticipantData parseCompound(CompoundTag tag) {

        Map<Integer, boolean[]> enablings = new HashMap<>();
        for (int i = 0; i < CMTParticipantScreen.MAX_CHANNELS; i++){
            enablings.put(i, new boolean[]{
                    tag.getBoolean("cmt_data.inserting_enabled_" + i),
                    tag.getBoolean("cmt_data.extracting_enabled_" + i)
            });
        }

        Map<Integer, MaidenInteractionConfig> insertConfigs = new HashMap<>();
        int count = tag.getInt("cmt_data.inserting_config_size");
        for (int i = 0; i < count; i++){
            insertConfigs.put(
                    tag.getInt("cmt_data.inserting_config_key_" + i),
                    MaidenInteractionConfig.parseCompound(tag.getCompound("cmt_data.inserting_config_" + i))
            );
        }

        Map<Integer, MaidenInteractionConfig> extractConfigs = new HashMap<>();
        count = tag.getInt("cmt_data.extracting_config_size");
        for (int i = 0; i < count; i++){
            extractConfigs.put(
                    tag.getInt("cmt_data.extracting_config_key_" + i),
                    MaidenInteractionConfig.parseCompound(tag.getCompound("cmt_data.extracting_config_" + i))
            );
        }

        Map<Integer, boolean[]>  changed = new HashMap<>();
        count = tag.getInt("cmt_data.changed_size");
        for (int i = 0; i < count; i++){
            changed.put(tag.getInt("cmt_data.changed_key_" + i), new boolean[]{
                    tag.getBoolean("cmt_data.inserting_changed_" + i),
                    tag.getBoolean("cmt_data.extracting_changed_" + i)
            });
        }

        return new CMTParticipantData(enablings, insertConfigs, extractConfigs, changed);
    }
}
