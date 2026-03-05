package com.mallowwww.realnpc.spook;

public interface SpookEventDef {
    boolean isCancellable();
    boolean isInstant();
    boolean isFinished();
    void tick();
}
