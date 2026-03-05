package com.mallowwww.realnpc.api;

import com.mallowwww.realnpc.entity.AbstractNPC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class ActionState {
    public abstract void tick(AbstractNPC npc);
    public abstract boolean done();
    public List<ResourceLocation> cancellableBy() {
        return List.of();
    }
}
