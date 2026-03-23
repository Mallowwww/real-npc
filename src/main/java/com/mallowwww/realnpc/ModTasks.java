package com.mallowwww.realnpc;

import com.mallowwww.realnpc.tutorial.TutorialTask;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class ModTasks {
    public static final DeferredRegister<TutorialTask> TASKS = DeferredRegister.create(ModRegistries.TUTORIAL_TASKS, RealNPCMod.MODID);
    public static final DeferredHolder<TutorialTask, TutorialTask> GET_WOOD = TASKS.register("get_wood", () -> new TutorialTask.Builder()
            .success((level, state) -> {
                var optionalUUID = state.getString("uuid");
                if (optionalUUID.isEmpty()) return false;
                var playerUUID = optionalUUID.get();
                var uuid = UUID.fromString(playerUUID);
                var player = level.getPlayerByUUID(uuid);
                if (player == null) return false;
                if (player.hasInfiniteMaterials()) return true;
                return (player.getInventory().hasAnyMatching(item -> item.is(ItemTags.LOGS)));
            }).fail((level, state) -> false)
            .location(RealNPCMod.path("get_wood"))
            .stop((level, state) -> level.getServer().sendSystemMessage(Component.literal("worked")))
            .text("Get wood")
            .build());
    public static void register(IEventBus eventBus) {
        TASKS.register(eventBus);
    }
}
