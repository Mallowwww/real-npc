package com.mallowwww.realnpc.network;

import com.mallowwww.realnpc.RealNPCMod;
import com.mallowwww.realnpc.spook.ClientSpookHandler;
import com.mallowwww.realnpc.spook.SpookSavedData;
import com.mallowwww.realnpc.spook.SpookState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateClientSpookStatePacket(SpookState state) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, UpdateClientSpookStatePacket> CODEC = StreamCodec.ofMember(
            UpdateClientSpookStatePacket::encode,
            UpdateClientSpookStatePacket::decode
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(UpdateClientSpookStatePacket packet, FriendlyByteBuf buf) {
        ByteBufCodecs.COMPOUND_TAG.encode(buf, packet.state.toTag());
    }
    public static UpdateClientSpookStatePacket decode(FriendlyByteBuf buf) {
        return new UpdateClientSpookStatePacket(
                SpookState.fromTag(ByteBufCodecs.COMPOUND_TAG.decode(buf))
        );
    }

    public static void handle(UpdateClientSpookStatePacket packet, IPayloadContext c) {
        ClientSpookHandler.LAST_STATE = packet.state;
    }

    public static final CustomPacketPayload.Type<UpdateClientSpookStatePacket> TYPE = new Type<>(RealNPCMod.path("update_client_spook_state"));
}
