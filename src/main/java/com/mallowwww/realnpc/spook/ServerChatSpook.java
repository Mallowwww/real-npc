package com.mallowwww.realnpc.spook;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ServerChatSpook implements SpookEventDef{
    private final String message;
    public ServerChatSpook(String message) {
        this.message = message;
    }
    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public boolean canRun(Level level) {
        return true;
    }

    @Override
    public int tickLength() {
        return 1;
    }

    @Override
    public void tick(Level level, int ticksRan) {
        if (!level.isClientSide())
            level.players().forEach(player -> {
                player.displayClientMessage(Component.literal(message), false);
            });
//            level.getServer().sendSystemMessage(Component.literal(message));
    }
}
