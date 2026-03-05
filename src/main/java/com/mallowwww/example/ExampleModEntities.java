package com.mallowwww.example;

import com.mallowwww.realnpc.RealNPCMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExampleModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RealNPCMod.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<DummyNPC>> DUMMY_NPC_TYPE = REGISTER.register("dummy_npc", () -> EntityType.Builder.<DummyNPC>of(DummyNPC::new,MobCategory.MONSTER)

            .sized(0.6F, 1.95F)
            .eyeHeight(1.74F)
            .passengerAttachments(2.0125F)
            .ridingOffset(-0.7F)
            .clientTrackingRange(8)

            .build("dummy_npc"));
    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
