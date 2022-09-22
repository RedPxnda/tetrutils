package com.redpxnda.tetrutils.schematic.requirement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirement;

import java.lang.reflect.Field;


public class AdvancementRequirement implements CraftingRequirement {
    ResourceLocation key;

//    static AdvancementProgress getAdvancementProgress(ResourceLocation advancement) {
//        LocalPlayer player = Minecraft.getInstance().player;
//        if (player == null) return null;
//        ClientAdvancements mgr = player.connection.getAdvancements();
//        Advancement adv = mgr.getAdvancements().get(advancement);
//        //return mgr.progress.get(adv);
//    }

    @Override
    public boolean test(CraftingContext context) {
        return true;
    }
}
