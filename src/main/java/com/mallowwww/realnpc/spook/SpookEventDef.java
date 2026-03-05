package com.mallowwww.realnpc.spook;

import net.minecraft.world.level.Level;

public interface SpookEventDef {
    boolean isCancellable();
    boolean canRun(Level level);
    int tickLength();
    default boolean isInstant() {
        return tickLength() == 1;
    }

    void tick(Level level, int ticksRan);
}
