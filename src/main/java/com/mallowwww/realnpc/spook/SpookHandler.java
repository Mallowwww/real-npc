package com.mallowwww.realnpc.spook;

import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.UUID;
@EventBusSubscriber
public class SpookHandler {
    public static SpookSavedData DATA = SpookSavedData.create();
    @SubscribeEvent
    public static void playerJoin(EntityJoinLevelEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        DATA.STATES.computeIfAbsent(player.getUUID(), (uuid) -> defaultState(player));
    }
    @SubscribeEvent
    public static void worldStart(LevelEvent.Load e) {
        var level = e.getLevel();
        if (level.dimensionType().effectsLocation().equals(ResourceLocation.parse("minecraft:overworld"))) {

        }
    }
    public static SpookState defaultState(Player player) {
        var state = new SpookState();
        state.setString("player", player.getName().getString());
        state.setString("uuid", player.getStringUUID());
        return state;
    }
}
