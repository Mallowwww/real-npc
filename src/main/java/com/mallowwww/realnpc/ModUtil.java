package com.mallowwww.realnpc;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.function.Consumer;

public class ModUtil {
    public static void scheduleTask(int delayTicks, Runnable task) {
        int[] remaining = {delayTicks};

        // Hold a reference so we can unregister
        Consumer<ServerTickEvent.Post>[] holder = new Consumer[1];
        holder[0] = (event) -> {
            if (--remaining[0] <= 0) {
                task.run();
                NeoForge.EVENT_BUS.unregister(holder[0]);
            }
        };

        NeoForge.EVENT_BUS.addListener(holder[0]);
    }
}
