package com.mallowwww.realnpc.tutorial;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.ModTasks;
import com.mallowwww.realnpc.spook.SpookHandler;
import com.mallowwww.realnpc.spook.SpookState;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.UUID;
@EventBusSubscriber
public class FakeTutorialHandler {
    public static final HashMap<UUID, TutorialData> DATA = new HashMap<>();
    @SubscribeEvent
    public static void playerJoin(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof Player player) {
            DATA.put(player.getUUID(), Util.make(new TutorialData(), data -> {
                data.addTask(ModTasks.GET_WOOD.getId());
            }));
        }
    }
    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post e) {
        var player = e.getEntity();
        var level = player.level();
        var data = DATA.get(player.getUUID());
        var state = SpookHandler.STATES.getOrDefault(player.getUUID(), new SpookState());
        data.getActiveTasks().forEach(taskPair -> {
            var status = taskPair.getSecond();
            if (status != TutorialData.TaskStatus.IN_PROGRESS) return;
            var task = ModRegistries.TUTORIAL_TASKS.get(taskPair.getFirst());
            if (task == null) return;
            if (task.failCondition(level, state)) {
                task.stop(level, state);
                data.markTaskFailed(task.location());
            }
            if (task.successCondition(level, state)) {
                task.stop(level, state);
                data.markTaskCompleted(task.location());
            }
        });
    }
}
