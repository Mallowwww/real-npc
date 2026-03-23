package com.mallowwww.realnpc.spook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface SpookEventDef {
    boolean isCancellable();
    boolean canRun(SpookState state);
    boolean isDone(SpookState state);
    ResourceLocation location();
    void tick(Level level, SpookState state);
    default boolean equals(SpookEventDef o) {
        return o.location().toString().equals(location().toString());
    }
}
