package com.mallowwww.realnpc;

import com.mallowwww.realnpc.tutorial.TutorialTask;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class ModTasks {
    public static final DeferredRegister<TutorialTask> TASKS = DeferredRegister.create(ModRegistries.TUTORIAL_TASKS, RealNPCMod.MODID);
    public static final DeferredHolder<TutorialTask, TutorialTask> GET_WOOD = TASKS.register("get_wood", () -> getItemWithTagTask(ItemTags.LOGS, "get_wood", "Get wood"));
    public static final DeferredHolder<TutorialTask, TutorialTask> GET_DIRT = TASKS.register("get_sand", () -> getItemTask(ResourceLocation.parse("minecraft:sand"), "get_sand", "Get sand"));
    public static final DeferredHolder<TutorialTask, TutorialTask> DIE = TASKS.register("die", () -> new TutorialTask.Builder().text("Die").success((level, state) -> {
        var uuid = UUID.fromString(state.getString("uuid").get());
        System.out.println(uuid);
        var player = level.getPlayerByUUID(uuid);
        return (player == null || player.isDeadOrDying());
    }).build());

    public static void register(IEventBus eventBus) {
        TASKS.register(eventBus);
    }
    public static TutorialTask getItemWithTagTask(TagKey<Item> tag, String path, String text) {
        return new TutorialTask.Builder()
                .success((level, state) -> {
                    var optionalUUID = state.getString("uuid");
                    if (optionalUUID.isEmpty()) return false;
                    var playerUUID = optionalUUID.get();
                    var uuid = UUID.fromString(playerUUID);
                    var player = level.getPlayerByUUID(uuid);
                    if (player == null) return false;
                    if (player.hasInfiniteMaterials()) return true;
                    return (player.getInventory().hasAnyMatching(item -> item.is(tag)));
                }).fail((level, state) -> false)
                .location(RealNPCMod.path(path))
                .stop((level, state) -> {})
                .start((level, state) -> {})
                .text(text)
                .build();
    }
    public static TutorialTask getItemTask(ResourceLocation item, String path, String text) {
        return new TutorialTask.Builder()
                .success((level, state) -> {
                    var optionalUUID = state.getString("uuid");
                    if (optionalUUID.isEmpty()) return false;
                    var playerUUID = optionalUUID.get();
                    var uuid = UUID.fromString(playerUUID);
                    var player = level.getPlayerByUUID(uuid);
                    if (player == null) return false;
                    if (player.hasInfiniteMaterials()) return true;
                    return (player.getInventory().hasAnyMatching(i -> i.getItemHolder().is(item)));
                }).fail((level, state) -> false)
                .location(RealNPCMod.path(path))
                .stop((level, state) -> {})
                .start((level, state) -> {})
                .text(text)
                .build();
    }
}
