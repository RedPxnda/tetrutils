package com.redpxnda.tetrutils.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class FreezingEffectPacket {
    public final int mode;
    public FreezingEffectPacket(int mode) {
        this.mode = mode;
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.mode);
    }
    public FreezingEffectPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
           if (mode == 1) {
               success.set(true);
           } else if (mode == 0) {
               success.set(true);
           }
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
