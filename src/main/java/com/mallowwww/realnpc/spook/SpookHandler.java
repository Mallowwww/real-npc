package com.mallowwww.realnpc.spook;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.UUID;
@EventBusSubscriber
public class SpookHandler {
    public static final HashMap<UUID, SpookState> STATES = new HashMap<>();

    @SubscribeEvent
    public static void playerJoin(EntityJoinLevelEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        STATES.computeIfAbsent(player.getUUID(), (uuid) -> defaultState(player));
    }
    public static SpookState defaultState(Player player) {
        var state = new SpookState();
        state.setString("player", player.getName().getString());
        state.setString("uuid", player.getStringUUID());
        return state;
    }
}
