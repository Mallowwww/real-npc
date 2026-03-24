package com.mallowwww.realnpc.tutorial;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.ModTasks;
import com.mallowwww.realnpc.ModUtil;
import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.spook.SpookHandler;
import com.mallowwww.realnpc.spook.SpookState;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.UUID;
@EventBusSubscriber
public class FakeTutorialHandler {
    private static int lastTickFinishedGoal = 0;
    public static final HashMap<UUID, TutorialData> DATA = new HashMap<>();
    @SubscribeEvent
    public static void playerJoin(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (!DATA.containsKey(player.getUUID()))
                DATA.put(player.getUUID(), Util.make(new TutorialData(), data -> {
                    if (Minecraft.getInstance().getConnection() == null)  {
                        var goal = player.getServer().registryAccess().registryOrThrow(ModRegistries.TUTORIAL_GOALS_KEY).get(ResourceLocation.parse("realnpc:gather_resources"));
                        data.setActiveGoal(goal);
                    } else {
                        var goal = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(ModRegistries.TUTORIAL_GOALS_KEY).get(ResourceLocation.parse("realnpc:gather_resources"));
                        data.setActiveGoal(goal);
                    }
                }));
        }
    }
    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post e) {
        var player = e.getEntity();
        var level = player.level();
        var data = DATA.get(player.getUUID());
        var state = SpookHandler.STATES.getOrDefault(player.getUUID(), new SpookState());
        if (!data.getActiveTasks().isEmpty() && data.getActiveTasks().stream().allMatch(t -> t.getSecond() != TutorialData.TaskStatus.IN_PROGRESS)) {
//            var currentTicks = level.getServer().getTickCount();
            var currentTicks = player.tickCount;
            if (currentTicks - lastTickFinishedGoal > 80) {
                lastTickFinishedGoal = currentTicks;
                ModUtil.scheduleTask(40, () -> {
                    data.clearTasks();
                    var currentGoal = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(ModRegistries.TUTORIAL_GOALS_KEY).get(data.getActiveGoal());
                    if (currentGoal == null) return;
                    if (currentGoal.next().equals(RealNPCMod.path("none")))
                        return;
                    var nextGoal = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(ModRegistries.TUTORIAL_GOALS_KEY).get(currentGoal.next());
                    data.setActiveGoal(nextGoal);

                });
            }

        }
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
