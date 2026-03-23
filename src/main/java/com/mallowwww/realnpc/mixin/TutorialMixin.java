package com.mallowwww.realnpc.mixin;

import net.minecraft.client.tutorial.Tutorial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tutorial.class)
public class TutorialMixin {
    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private static void start(CallbackInfo ci) {
        ci.cancel();
    }
}
