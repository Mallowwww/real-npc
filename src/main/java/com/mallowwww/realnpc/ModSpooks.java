package com.mallowwww.realnpc;

import com.mallowwww.realnpc.spook.SpookEventDef;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpooks {
    public static final DeferredRegister<SpookEventDef> SPOOKS = DeferredRegister.create(ModRegistries.SPOOKS, RealNPCMod.MODID);
//    public static final DeferredHolder<SpookEventDef, ServerChatSpook> CHAT_SPOOK = SPOOKS.register("chat_spook", () -> new ServerChatSpook("This is a spooky message~"));
    public static void register(IEventBus eventBus) {
        SPOOKS.register(eventBus);
    }
}
