package com.mallowwww.realnpc.render;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.spook.SpookHandler;
import com.mallowwww.realnpc.tutorial.FakeTutorialHandler;
import com.mallowwww.realnpc.tutorial.TutorialData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.HashMap;

@EventBusSubscriber(value = Dist.CLIENT)
public class GUIHandler {
    public static class UIData {
        public static final int xPadding = 4;
        public static final int yPadding = 4;
    }
    private static final HashMap<TutorialData.TaskStatus, Integer> COLORS = Util.make(new HashMap<>(), (map) -> {
        map.put(TutorialData.TaskStatus.IN_PROGRESS, 0xFFFFFFFF);
        map.put(TutorialData.TaskStatus.COMPLETED, 0xFF00FF00);
        map.put(TutorialData.TaskStatus.FAILED, 0xFFFF0000);
    });
    public static final LayeredDraw.Layer FAKE_TUTORIAL_LAYER = (guiGraphics, deltaTracker) -> {
        var minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        var uuid = minecraft.player.getUUID();
        var data = FakeTutorialHandler.DATA.get(uuid);
        var tasks = data.getActiveTasks();
        guiGraphics.drawString(minecraft.font, Component.literal(data.getActiveGoal()).withStyle(ChatFormatting.BOLD), UIData.xPadding, UIData.yPadding, 0xFFFFFFFF);
        guiGraphics.fill(UIData.xPadding, 10 + UIData.yPadding, 150 + UIData.xPadding, 11 + UIData.yPadding, 0xFFFFFFFF);
        for (int i = 0; i < tasks.size(); i++) {
            var task = ModRegistries.TUTORIAL_TASKS.get(tasks.get(i).getFirst());
            if (task == null) return;
            guiGraphics.drawString(minecraft.font, Component.literal(task.text(SpookHandler.STATES.get(uuid))), UIData.xPadding, 13 + i * 8 + UIData.yPadding, COLORS.get(tasks.get(i).getSecond()));
        }

    };
    public static final ResourceLocation FAKE_TUTORIAL = RealNPCMod.path("fake_tutorial");

    @SubscribeEvent
    public static void registerGUILayers(RegisterGuiLayersEvent e) {
        e.registerAboveAll(FAKE_TUTORIAL, FAKE_TUTORIAL_LAYER);
    }
}
