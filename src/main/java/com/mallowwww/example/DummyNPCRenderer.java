package com.mallowwww.example;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DummyNPCRenderer extends EntityRenderer<DummyNPC> {
    public DummyNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(DummyNPC entity) {
        return null;
    }
}
