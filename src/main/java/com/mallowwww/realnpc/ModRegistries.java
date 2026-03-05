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

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class ModRegistries {
    private static int lastTickSpookRan = 0;
    private static int currentTickTimer = 100;
    private static Optional<SpookEventDef> currentSpook = Optional.empty();
    public static final Registry<SpookEventDef> SPOOKS = new RegistryBuilder<SpookEventDef>(ResourceKey.createRegistryKey(RealNPCMod.path("spooks")))
            .create();
    public static int numSpooks() {
        return SPOOKS.size();
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
        if (lastTickSpookRan == 0)
            lastTickSpookRan = ticks;
        if (ticks - lastTickSpookRan >= currentTickTimer)
            currentSpook = Optional.empty();
        if (ticks % 20 == 0) {
//            System.out.println(numSpooks()+" "+ticks+" "+currentTickTimer);
        }
        if ((currentSpook.isEmpty() || currentSpook.get().isCancellable()) && numSpooks()>0)
            for (int i = 0; i < 4; i++) {
                var randInt = rand.nextInt(num);
                AtomicBoolean doEscape = new AtomicBoolean(false);
                SPOOKS.getHolder(randInt).ifPresent(spookRef -> {
                    var spook = spookRef.value();
                    if (spook.canRun(level)) {
                        if (spook.isInstant() && ticks - lastTickSpookRan >= currentTickTimer)
                            spook.tick(level, ticks - lastTickSpookRan);
                        else if (!spook.isInstant())
                            currentSpook = Optional.of(spook);
                        else return;
                        lastTickSpookRan = ticks;
                        currentTickTimer = rand.nextInt( 200)+50; // TODO need to figure out what we want our bound to be
                        doEscape.set(true);
                    }
                });
                if (doEscape.get())
                    break;
            }
        currentSpook.ifPresent(spookEventDef -> spookEventDef.tick(level, ticks - lastTickSpookRan));

    }
}
