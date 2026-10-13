package com.mallowwww.realnpc.tutorial;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.ModUtil;
import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.spook.ClientSpookHandler;
import com.mallowwww.realnpc.spook.SpookHandler;
import com.mallowwww.realnpc.spook.SpookState;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@EventBusSubscriber(value = Dist.CLIENT)
public class FakeTutorialHandler {
    public static TaskGoal goal = new TaskGoal("", TaskState.COMPLETE);
    private static String lastGoalCompleted = "";
    public static final List<SubTaskGoal> subTaskGoals = new ArrayList<>();

    public enum TaskState {
        COMPLETE, IN_PROGRESS, FAILED
    }
    @SubscribeEvent
    public static void playerJoin(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (!player.getUUID().equals(Minecraft.getInstance().getUser().getProfileId())) return;
            var state = ClientSpookHandler.LAST_STATE;
            if (!state.has("tutorial_goal_name")) {
                state.setString("tutorial_goal_name", "Gather Resources.");
                state.setString("tutorial_goal_state", TaskState.IN_PROGRESS.name());
                state.setInt("tutorial_subgoals", 1);
                state.setString("tutorial_subgoal_0_name", "Get Wood");
                state.setString("tutorial_subgoal_0_state", TaskState.IN_PROGRESS.name());
            }


        }
    }
    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post e) {
        var player = e.getEntity();
        var level = player.level();
        var state = ClientSpookHandler.LAST_STATE;
        if (!state.has("tutorial_goal_name")) return;
        goal = new TaskGoal(
                state.getString("tutorial_goal_name").orElse(""),
                TaskState.valueOf(state.getString("tutorial_goal_state").orElse("COMPLETED"))
        );
        int subgoals = state.getInt("tutorial_subgoals").orElse(0);
        subTaskGoals.clear();
        for (int i = 0; i < subgoals; i++) {
            var name = state.getString("tutorial_subgoal_"+i+"_name").orElse("");
            var taskState = TaskState.valueOf(state.getString("tutorial_subgoal_"+i+"_state").orElse("COMPLETED"));
            subTaskGoals.add(new SubTaskGoal(name, taskState));
            if (taskState == TaskState.COMPLETE && !(lastGoalCompleted.equals(name))) {
                lastGoalCompleted = name;
                ModUtil.scheduleTask(40, () -> {
                    // Remove the completed one 2 seconds later
                });
            }
        }
    }
    public record TaskGoal(String name, TaskState state) {}
    public record SubTaskGoal(String name, TaskState state) {}
}
