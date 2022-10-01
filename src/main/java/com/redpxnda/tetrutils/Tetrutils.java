package com.redpxnda.tetrutils;

import com.mojang.logging.LogUtils;
import com.redpxnda.tetrutils.effects.AntiKBEffect;
import com.redpxnda.tetrutils.effects.FreezingEffect;
import com.redpxnda.tetrutils.effects.FrenzyEffect;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import com.redpxnda.tetrutils.packet.Packets;
import com.redpxnda.tetrutils.schematic.requirement.AdvancementRequirement;
import com.redpxnda.tetrutils.schematic.requirement.CurioRequirement;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirementDeserializer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tetrutils")
public class Tetrutils {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "tetrutils";

    public Tetrutils() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        PotionEffects.register(eventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AchievementDataSyncEvents());
        MinecraftForge.EVENT_BUS.register(new FreezingEffect());
        MinecraftForge.EVENT_BUS.register(new AntiKBEffect());
        MinecraftForge.EVENT_BUS.register(new FrenzyEffect());

        CraftingRequirementDeserializer.registerSupplier("tetrutils:advancement", AdvancementRequirement.class);
        CraftingRequirementDeserializer.registerSupplier("tetrutils:curio", CurioRequirement.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        event.enqueueWork(Packets::init);
    }
}
