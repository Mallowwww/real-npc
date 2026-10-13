package com.mallowwww.realnpc.spook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpookState {
    private final HashMap<String, String> map;
//    public static final Codec<SpookState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("map").forGetter(SpookState::getMap)
//    ).apply(instance, SpookState::new));
    public SpookState() {
        map = new HashMap<>();
    }

    public SpookState(Map<String, String> map) {
        this.map = new HashMap<>(map);
    }

    public Optional<Integer> setInt(String key, int n) {
        try {
            var old = (map.get(key));
            map.put(key, Integer.toString(n));
            return Optional.of(Integer.parseInt(old));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    public Optional<Integer> getInt(String key) {
        try {
            return Optional.of(Integer.parseInt(map.get(key)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    public Optional<Boolean> setBool(String key, boolean bool) {
        var old = map.put(key, Boolean.toString(bool));
        if (old == null)
            return Optional.empty();
        if (old.equals("true"))
            return Optional.of(true);
        if (old.equals("false"))
            return Optional.of(false);
        return Optional.empty();
    }
    public Optional<Boolean> getBool(String key) {
        var old = map.get(key);
        if (old == null)
            return Optional.empty();
        if (old.equals("true"))
            return Optional.of(true);
        if (old.equals("false"))
            return Optional.of(false);
        return Optional.empty();
    }
    public Optional<String> setString(String key, String val) {
        return Optional.ofNullable(map.put(key, val));
    }
    public Optional<String> getString(String key) {
        return Optional.ofNullable(map.get(key));
    }
    public boolean has(String s) {
        return map.containsKey(s);
    }
    public HashMap<String, String> getMap() {
        return new HashMap<>(map);
    }
    public CompoundTag toTag() {
        var tag = new CompoundTag();
        for (var x : map.entrySet()) {
            tag.put(x.getKey(), StringTag.valueOf(x.getValue()));
        }
        return tag;
    }
    public static SpookState fromTag(CompoundTag tag) {
        var state = new SpookState();
        for (var x : tag.getAllKeys()) {
            state.setString(x, tag.getString(x));
        }
        return state;
    }
}
