package com.mallowwww.realnpc.render;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.tutorial.FakeTutorialHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.ArrayList;
import java.util.HashMap;

@EventBusSubscriber(value = Dist.CLIENT)
public class GUIHandler {
    public static class UIData {
        public static final int xPadding = 4;
        public static final int yPadding = 4;
    }
    private static final HashMap<FakeTutorialHandler.TaskState, Integer> COLORS = Util.make(new HashMap<>(), (map) -> {
        map.put(FakeTutorialHandler.TaskState.IN_PROGRESS, 0xFFFFFFFF);
        map.put(FakeTutorialHandler.TaskState.COMPLETE, 0xFF00FF00);
        map.put(FakeTutorialHandler.TaskState.FAILED, 0xFFFF0000);
    });
    public static final LayeredDraw.Layer FAKE_TUTORIAL_LAYER = (guiGraphics, deltaTracker) -> {
        var minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        var uuid = minecraft.player.getUUID();
        var data = new ArrayList<>(FakeTutorialHandler.subTaskGoals);
        var currentGoal = FakeTutorialHandler.goal;
        guiGraphics.drawString(minecraft.font, Component.literal(currentGoal.name()).withStyle(ChatFormatting.BOLD), UIData.xPadding, UIData.yPadding, 0xFFFFFFFF);
        guiGraphics.fill(UIData.xPadding, 10 + UIData.yPadding, 150 + UIData.xPadding, 11 + UIData.yPadding, 0xFFFFFFFF);
        for (int i = 0; i < data.size(); i++) {
            var task = data.get(i);
            System.out.println(task.name());
            guiGraphics.drawString(minecraft.font, Component.literal(task.name()), UIData.xPadding, 13 + i * 8 + UIData.yPadding, COLORS.get(data.get(i).state()));
        }

    };
    public static final ResourceLocation FAKE_TUTORIAL = RealNPCMod.path("fake_tutorial");

    @SubscribeEvent
    public static void registerGUILayers(RegisterGuiLayersEvent e) {
        e.registerAboveAll(FAKE_TUTORIAL, FAKE_TUTORIAL_LAYER);
    }
}
