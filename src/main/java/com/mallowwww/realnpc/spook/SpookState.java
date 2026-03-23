package com.mallowwww.realnpc.spook;

import java.util.HashMap;
import java.util.Optional;

public class SpookState {
    private final HashMap<String, String> map;
    public SpookState() {
        map = new HashMap<>();
    }
    public Optional<Integer> setInt(String key, int n) {
        try {
            var old = Integer.parseInt(map.get(key));
            map.put(key, Integer.toString(n));
            return Optional.of(old);
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
}
