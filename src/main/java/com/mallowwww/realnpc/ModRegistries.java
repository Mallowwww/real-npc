package com.mallowwww.realnpc;

import com.mallowwww.realnpc.api.Action;
import com.mallowwww.realnpc.api.ActionDef;
import com.mallowwww.realnpc.api.ActionState;
import com.mallowwww.realnpc.api.ActionStateDef;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber
public class ModRegistries {
    public static final ResourceKey<Registry<ActionDef>> ACTIONS_RESOURCE_KEY = ResourceKey.createRegistryKey(RealNPCMod.path("actions"));
    public static final Registry<ActionDef> ACTIONS = new MappedRegistry<>(ACTIONS_RESOURCE_KEY, Lifecycle.stable());
    public static final ResourceKey<Registry<ActionStateDef>> ACTION_STATES_RESOURCE_KEY = ResourceKey.createRegistryKey(RealNPCMod.path("action_states"));
    public static final Registry<ActionStateDef> ACTION_STATES = new MappedRegistry<>(ACTION_STATES_RESOURCE_KEY, Lifecycle.stable());
    @SubscribeEvent
    public static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(ACTIONS);
        event.register(ACTION_STATES);
    }
}
