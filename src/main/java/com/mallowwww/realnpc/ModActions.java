package com.mallowwww.realnpc;

import com.mallowwww.realnpc.api.Action;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModActions {
    public static final DeferredRegister<Action> ACTIONS = DeferredRegister.create(ModRegistries.ACTIONS_RESOURCE_KEY, RealNPCMod.MODID);
    public static final DeferredHolder<Action, Action> IDLE_ACTION = ACTIONS.register("idle", () -> new Action.Builder().idle().build());
    public static void register(IEventBus eventBus) {
        ACTIONS.register(eventBus);
    }
}
