package com.mallowwww.realnpc.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber
public class NetworkHandler {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var r = event.registrar("1");
        r.playToClient(
                UpdateClientSpookStatePacket.TYPE,
                UpdateClientSpookStatePacket.CODEC,
                UpdateClientSpookStatePacket::handle
        );
    }
}
