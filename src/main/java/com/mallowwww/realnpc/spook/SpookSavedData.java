package com.mallowwww.realnpc.spook;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.UUID;

public class SpookSavedData extends SavedData {
    public final HashMap<UUID, SpookState> STATES;
    private SpookSavedData() {
        STATES = new HashMap<>();

    }
    private SpookSavedData(HashMap<UUID, SpookState> STATES) {
        this.STATES = STATES;
    }
    public static SpookSavedData create() {
        return new SpookSavedData();
    }
    public static SpookSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        var map = new HashMap<UUID, SpookState>();
        var size = tag.getInt("size");
        for (int i = 0; i < size; i++) {
            var state = new SpookState();
            var uuid = tag.getUUID("key"+i);
            var stateTag = (CompoundTag)tag.get("value"+i);
            var stateSize = stateTag.getInt("size");
            for (int j = 0; j < stateSize; j++) {
                var key = stateTag.getString("key"+j);
                var value = stateTag.getString("value"+j);
                state.setString(key, value);
            }
            map.put(uuid, state);
        }
        return new SpookSavedData(map);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("size", STATES.size());
        int i = 0;
        for (var x : STATES.entrySet()) {
            tag.putUUID("key"+i, x.getKey());
            var state = x.getValue().getMap();
            var stateTag = new CompoundTag();
            stateTag.putInt("size", state.size());
            int j = 0;
            for (var y : state.entrySet()) {
                stateTag.putString("key"+j, y.getKey());
                stateTag.putString("value"+j, y.getValue());
                j++;
            }
            tag.put("value"+i, stateTag);
            i++;
        }
        return null;
    }
}
