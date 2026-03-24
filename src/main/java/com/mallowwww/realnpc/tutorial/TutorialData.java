package com.mallowwww.realnpc.tutorial;

import com.mallowwww.realnpc.RealNPCMod;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TutorialData {
    public enum TaskStatus {
        IN_PROGRESS,
        COMPLETED,
        FAILED;
        public static final Codec<TaskStatus> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("status").forGetter(TaskStatus::name)
        ).apply(instance, TaskStatus::valueOf));
    }
    private final List<Pair<ResourceLocation, TaskStatus>> active_tasks;
    private ResourceLocation active_goal;
    public static final Codec<TutorialData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.pair(ResourceLocation.CODEC, TaskStatus.CODEC)).fieldOf("active_tasks").forGetter(TutorialData::getActiveTasks),
            ResourceLocation.CODEC.fieldOf("active_goal").forGetter(TutorialData::getActiveGoal)
    ).apply(instance, TutorialData::new));
    public TutorialData() {
        active_tasks = new ArrayList<>();
//        addTask("Get Wood");
        active_goal = RealNPCMod.path("gather_resources");

    }
    protected TutorialData(List<Pair<ResourceLocation, TaskStatus>> tasks, ResourceLocation goal) {
        active_tasks = tasks;
        active_goal = goal;
    }
    public List<Pair<ResourceLocation, TaskStatus>> getActiveTasks() {
        return List.copyOf(active_tasks);
    }
    public ResourceLocation getActiveGoal() {
        return active_goal;
    }
    public void setActiveGoal(TutorialGoal goal) {
        this.active_goal = goal.location();
        active_tasks.clear();
        active_tasks.addAll(goal.tasks().stream().map(task -> new Pair<>(task, TaskStatus.IN_PROGRESS)).collect(Collectors.toSet()));
    }
    public void clearTasks() {
        active_tasks.clear();
    }
    public void addTask(ResourceLocation task) {
        active_tasks.addLast(Pair.of(task, TaskStatus.IN_PROGRESS));
    }
    public boolean markTaskCompleted(ResourceLocation task) {
        return markTaskStatus(task, TaskStatus.COMPLETED);
    }
    public boolean markTaskFailed(ResourceLocation task) {
        return markTaskStatus(task, TaskStatus.FAILED);
    }
    protected boolean markTaskStatus(ResourceLocation task, TaskStatus status) {
        for (int i = 0; i < active_tasks.size(); i++) {
            var checkTask = active_tasks.get(i).getFirst();
            if (checkTask.equals(task)) {
                active_tasks.set(i, Pair.of(task, status));
                return true;
            }

        }
        return false;
    }
    public boolean killTask(String task) {
        for (int i = 0; i < active_tasks.size(); i++) {
            var checkTask = active_tasks.get(i).getFirst();
            if (checkTask.equals(task)) {
                active_tasks.remove(i--);
            }

        }
        return false;
    }

}
