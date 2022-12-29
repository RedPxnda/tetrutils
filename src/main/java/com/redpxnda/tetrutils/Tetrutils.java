package com.redpxnda.tetrutils;

import com.mojang.datafixers.types.templates.Sum;
import com.mojang.logging.LogUtils;
import com.redpxnda.tetrutils.craftingeffect.outcome.StackingMaterialOutcome;
import com.redpxnda.tetrutils.effects.*;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import com.redpxnda.tetrutils.modular.Registry;
import com.redpxnda.tetrutils.packet.Packets;
import com.redpxnda.tetrutils.schematic.requirement.AdvancementRequirement;
import com.redpxnda.tetrutils.schematic.requirement.CurioRequirement;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

import se.mickelus.tetra.craftingeffect.CraftingEffectRegistry;
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
        MinecraftForge.EVENT_BUS.register(new WisdomEffect());
        MinecraftForge.EVENT_BUS.register(new PiercingEffect());
        MinecraftForge.EVENT_BUS.register(new MobEffectEffect());
        MinecraftForge.EVENT_BUS.register(new SummonEntityEffect());

        CraftingRequirementDeserializer.registerSupplier("tetrutils:advancement", AdvancementRequirement.class);
        if(ModList.get().isLoaded("curios")) {
            CraftingRequirementDeserializer.registerSupplier("tetrutils:curio", CurioRequirement.class);

            Registry.init(FMLJavaModLoadingContext.get().getModEventBus());
        }

        //CraftingEffectRegistry.registerEffectType("tetra:test", StackingMaterialOutcome.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        event.enqueueWork(Packets::init);
    }
}
