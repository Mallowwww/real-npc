package com.mallowwww.realnpc;

import com.mallowwww.realnpc.spook.SpookEventDef;
import com.mallowwww.realnpc.spook.SpookState;
import com.mallowwww.realnpc.tutorial.TutorialTask;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber
public class ModRegistries {
    private static int lastTickSpookRan = 0;
    private static int currentTickTimer = 100;
    private static final SpookState state = new SpookState();
    private static final List<SpookEventDef> runningSpooks = new ArrayList<>();

    public static final Registry<SpookEventDef> SPOOKS = new RegistryBuilder<SpookEventDef>(ResourceKey.createRegistryKey(RealNPCMod.path("spooks")))
            .create();
    public static final Registry<TutorialTask> TUTORIAL_TASKS = new RegistryBuilder<TutorialTask>(ResourceKey.createRegistryKey(RealNPCMod.path("tutorial_tasks"))).create();
    public static int numSpooks() {
        return SPOOKS.size();
    }
    @SubscribeEvent
    public static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(SPOOKS); event.register(TUTORIAL_TASKS);
    }
    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        var level = event.getLevel();
        var num = numSpooks();
        var rand = level.random;
        var ticks = (int)level.getGameTime();
        if (lastTickSpookRan == 0)
            lastTickSpookRan = ticks;
        var canDoSpook = ticks - lastTickSpookRan >= currentTickTimer;
        if (ticks % 20 == 0) {
//            System.out.println(numSpooks()+" "+ticks+" "+currentTickTimer);
        }
        for (int i = 0; i < runningSpooks.size(); i++)
            if (runningSpooks.get(i).isDone(state))
                runningSpooks.remove(i--);
        if ((canDoSpook && runningSpooks.stream().allMatch(SpookEventDef::isCancellable)) && numSpooks()>0) {
            runningSpooks.clear();
            for (int i = 0; i < 4; i++) {
                var randInt = rand.nextInt(num);
                AtomicBoolean doEscape = new AtomicBoolean(false);
                SPOOKS.getHolder(randInt).ifPresent(spookRef -> {
                    var spook = spookRef.value();
                    if (spook.canRun(state) && runningSpooks.stream().noneMatch(s -> s.equals(spook))) {
                        spook.tick(level, state);
                        if (!spook.isDone(state))
                            runningSpooks.add(spook);
                        else return;
                        lastTickSpookRan = ticks;
                        currentTickTimer = rand.nextInt(200) + 50; // TODO need to figure out what we want our bound to be
                        doEscape.set(true);
                    }
                });
                if (doEscape.get())
                    break;
            }
        }
        runningSpooks.forEach(s -> s.tick(level, state));

    }
}
