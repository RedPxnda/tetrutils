package com.redpxnda.tetrutils.packet;

import com.redpxnda.tetrutils.client.ClientAchievementData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SyncAchievementsToClientPacket {
    private ArrayList<String> achievements = new ArrayList<>();

    public SyncAchievementsToClientPacket(ArrayList<String> achievements) {
        this.achievements = achievements;
    }

    public SyncAchievementsToClientPacket(FriendlyByteBuf buffer) {
        int length = buffer.readInt();
        if (length > 0) {
            for (int i=0;i<length;i++) {
                String value = buffer.readUtf();
                achievements.remove(value);
                achievements.add(value);
            }
        }

    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(achievements.size());
        for (String achievement : achievements) {
            buffer.writeUtf(achievement);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientAchievementData.set(achievements);
        });
        return true;
    }
}
