package com.redpxnda.tetrutils.schematic.requirement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirement;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;


public class CurioRequirement implements CraftingRequirement {
    ResourceLocation item;

    @Override
    public boolean test(CraftingContext context) {
        //context.player
        if (context.player != null) {
            Optional<SlotResult> curio = CuriosApi.getCuriosHelper().findFirstCurio(context.player, ForgeRegistries.ITEMS.getValue(item));
            return curio.isPresent();
        }
        return false;
    }
}
