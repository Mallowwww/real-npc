package com.mallowwww.realnpc;

import com.mallowwww.realnpc.spook.SpookEventDef;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class ModRegistries {
    private static AtomicInteger numSpooksRegistered;
    private static int lastTickSpookRan = 0;
    private static int currentTickTimer = 100;
    private static Optional<SpookEventDef> currentSpook;
    public static final Registry<SpookEventDef> SPOOKS = new RegistryBuilder<SpookEventDef>(ResourceKey.createRegistryKey(RealNPCMod.path("spooks")))
            .onAdd((reg, i, key, def) -> numSpooksRegistered.incrementAndGet())
            .create();
    public static int numSpooks() {
        return numSpooksRegistered.get();
    }
    @SubscribeEvent
    public static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(SPOOKS);
    }
    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        var level = event.getLevel();
        var num = numSpooks();
        var rand = level.random;
        var ticks = (int)level.getGameTime();
        if (ticks - lastTickSpookRan >= currentTickTimer)
            currentSpook = Optional.empty();
        if (currentSpook.isEmpty() || currentSpook.get().isCancellable())
            for (int i = 0; i < 4; i++) {
                var randInt = rand.nextInt(0, num);
                AtomicBoolean doEscape = new AtomicBoolean(false);
                SPOOKS.getHolder(randInt).ifPresent(spookRef -> {
                    var spook = spookRef.value();
                    if (spook.canRun(level)) {
                        if (spook.isInstant())
                            spook.tick(level, ticks - lastTickSpookRan);
                        else
                            currentSpook = Optional.of(spook);
                        lastTickSpookRan = ticks;
                        currentTickTimer = rand.nextInt(100, 200); // TODO need to figure out what we want our bound to be
                    }
                });
                if (doEscape.get())
                    break;
            }
        currentSpook.ifPresent(spookEventDef -> spookEventDef.tick(level, ticks - lastTickSpookRan));

    }
}
