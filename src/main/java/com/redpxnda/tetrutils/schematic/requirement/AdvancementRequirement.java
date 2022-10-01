package com.redpxnda.tetrutils.schematic.requirement;

import com.redpxnda.tetrutils.client.ClientAchievementData;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirement;

import java.lang.reflect.Field;


public class AdvancementRequirement implements CraftingRequirement {
    String id;

    @Override
    public boolean test(CraftingContext context) {
        return ClientAchievementData.getAchievements().contains(id);
    }
}
