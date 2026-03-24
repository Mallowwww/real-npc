package com.mallowwww.realnpc.tutorial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TutorialGoal {
    private final ResourceLocation location;
    private final ResourceLocation next;
    private final String name;
    private final List<ResourceLocation> tasks;
    public static final Codec<TutorialGoal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(TutorialGoal::location),
            ResourceLocation.CODEC.fieldOf("next").forGetter(TutorialGoal::next),
            Codec.STRING.fieldOf("name").forGetter(TutorialGoal::name),
            Codec.list(ResourceLocation.CODEC).fieldOf("tasks").forGetter(TutorialGoal::tasks)
    ).apply(instance, TutorialGoal::new));
    public TutorialGoal(ResourceLocation location, ResourceLocation next, String name, List<ResourceLocation> tasks) {
        this.location = location;
        this.next = next;
        this.name = name;
        this.tasks = tasks;
    }
    public ResourceLocation location() { return location; };
    public ResourceLocation next() { return next; };
    public String name() { return name; };
    public List<ResourceLocation> tasks() { return tasks; }
}
