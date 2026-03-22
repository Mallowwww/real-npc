package com.mallowwww.realnpc.mixin;

import com.mallowwww.util.WorldConsoleTextHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.resources.NeoForgeSplashHooks;
import org.apache.commons.codec.digest.Md5Crypt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.screens.LevelLoadingScreen.renderChunks;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin extends Screen {
    @Unique
    @Final
    private static final String username = Minecraft.getInstance().getUser().getName();
    @Unique
    @Final
    private static final String fakeIP = Md5Crypt.apr1Crypt(username).substring(0, 8);
    @Unique
    @Final
    private static final Map<Character, Integer> COLORS = Util.make(new HashMap<>(), m -> {
        m.put('r', 0xFFFF0000);
        m.put('g', 0xFF00FF00);
        m.put('b', 0xFF0000FF);
        m.put('c', 0xFF00FFFF);
        m.put('m', 0xFFFF00FF);
        m.put('y', 0xFFFFFF00);
        m.put('k', 0xFF000000);
        m.put('w', 0xFFFFFFFF);
    });
    @Unique
    private static final RandomSource real_npc$RANDOM = RandomSource.create();
    @Unique
    private static final int NUM_LINES = 20;
    @Unique
    private static final List<String> real_npc$SPLASHES = NeoForgeSplashHooks.loadSplashes(Minecraft.getInstance().getResourceManager());
    @Shadow
    private static final long NARRATION_DELAY_MS = 2000L;
    @Unique
    private final List<Component> real_npc$lineList = new ArrayList<>();
    @Unique
    private final String world = "test";
    @Unique
    private final WorldConsoleTextHandler textHandler = new WorldConsoleTextHandler();
    @Final
    @Shadow
    private StoringChunkProgressListener progressListener;
    @Shadow
    private long lastNarration = -1L;
    @Shadow
    private boolean done;
    private LevelLoadingScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ci.cancel();
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        long time = Util.getMillis();
        if (time - this.lastNarration > 2000L) {
            this.lastNarration = time;
            this.triggerImmediateNarration(true);
        }


        int midX = this.width / 2;
        int midY = this.height / 2;
//        renderChunks(guiGraphics, this.progressListener, midX, midY, 2, 0);
        int l = this.progressListener.getDiameter() + 9 + 2;
//        var lastComponent = real_npc$lineList.isEmpty() ? Component.empty() : real_npc$lineList.getFirst();
//        var newComponent = this.getFormattedProgress();
//        if (!newComponent.getString().equals(lastComponent.getString()))
//            real_npc$lineList.addFirst(newComponent);

        if (real_npc$RANDOM.nextFloat() > .93f) {
            var nextComponent = textHandler.next();
//            real_npc$lineList.addFirst(Component.literal(real_npc$SPLASHES.get(real_npc$RANDOM.nextInt(real_npc$SPLASHES.size()))));
            for (var x : nextComponent)
                if (!x.isEmpty())
                    real_npc$lineList.addLast(format(x));
            if (nextComponent.length == 0) {
                real_npc$lineList.addLast(getFormattedProgress());
            }
        }
        if (real_npc$lineList.size() > NUM_LINES)
            real_npc$lineList.removeFirst();
//        guiGraphics.drawCenteredString(this.font, this.getFormattedProgress(), j, k - l, 16777215);
        for (int i = 0; i < real_npc$lineList.size(); i++) {
            var component = real_npc$lineList.get(i);
            guiGraphics.drawString(this.font, component, 0, i * 8, 16777215);
        }
    }
    @Shadow
    private Component getFormattedProgress() {
        return Component.translatable("loading.progress", Mth.clamp(this.progressListener.getProgress(), 0, 100));
    }
    @Unique
    public Component format(String s) {
        var comp = Component.empty();
        StringBuilder buildStr = new StringBuilder();
        var currentColor = 0xFFFFFFFF;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '$') {
                buildStr.append(world);
            } else if (s.charAt(i) == '\\') {
                buildStr.append(username);
            } else if (s.charAt(i) == '%') {
                var colChar = s.charAt(i+1);
                comp.append(Component.literal(buildStr.toString()).withColor(currentColor));
                buildStr = new StringBuilder();
                currentColor = COLORS.get(colChar);
                i++;
            } else
                buildStr.append(s.charAt(i));
        }
        if (!buildStr.isEmpty())
            comp.append(Component.literal(buildStr.toString()).withColor(currentColor));
        return comp;
    }
}
