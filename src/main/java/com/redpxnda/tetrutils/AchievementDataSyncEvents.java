package com.redpxnda.tetrutils;

import com.redpxnda.tetrutils.packet.Packets;
import com.redpxnda.tetrutils.packet.SyncAchievementsToClientPacket;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AchievementDataSyncEvents {

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                ArrayList<String> achievements = getCompletedAdvancements(player);
                Packets.sendToPlayer(new SyncAchievementsToClientPacket(achievements), player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAchievement(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            ArrayList<String> achievements = getCompletedAdvancements(player);
            Packets.sendToPlayer(new SyncAchievementsToClientPacket(achievements), player);
        }
    }

    public ArrayList<String> getCompletedAdvancements(ServerPlayer player) {
        Collection<Advancement> advancements = Objects.requireNonNull(player.getServer()).getAdvancements().getAllAdvancements();
        ArrayList<String> finishedAdv = new ArrayList<>();
        for (Advancement adv : advancements) {
            if (player.getAdvancements().getOrStartProgress(adv).isDone()) {
                finishedAdv.add(adv.getId().toString());
            }
        }
        return finishedAdv;
    }
}
