package com.redpxnda.tetrutils.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private PacketHandler() {}
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("tetrutils", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void init() {
        int index = 0;
        INSTANCE.messageBuilder(FreezingEffectPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FreezingEffectPacket::encode)
                .decoder(FreezingEffectPacket::new)
                .consumer(FreezingEffectPacket::handle).add();
    }

}
