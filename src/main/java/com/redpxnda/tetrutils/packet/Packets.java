package com.redpxnda.tetrutils.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Packets {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("tetrutils", "messages"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        INSTANCE.messageBuilder(SyncAchievementsToClientPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAchievementsToClientPacket::new)
                .encoder(SyncAchievementsToClientPacket::toBytes)
                .consumer(SyncAchievementsToClientPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
