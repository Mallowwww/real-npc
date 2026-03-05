package com.mallowwww.realnpc.api;

import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.entity.AbstractNPC;
import net.minecraft.resources.ResourceLocation;

public abstract class ActionDef {
    public abstract ResourceLocation id();
    public abstract void tick(AbstractNPC npc, Action action);
}
