package com.redpxnda.tetrutils.modular;

import com.redpxnda.tetrutils.modular.curio.ModularNecklace;
import com.redpxnda.tetrutils.modular.curio.ModularRing;
import com.redpxnda.tetrutils.modular.gauntlet.ModularGauntlet;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "tetrutils");
    public static final RegistryObject<Item> MODULAR_RING = ITEMS.register("modular_ring", ModularRing::new);
    public static final RegistryObject<Item> MODULAR_NECKLACE = ITEMS.register("modular_necklace", ModularNecklace::new);
    public static final RegistryObject<Item> MODULAR_GAUNTLET = ITEMS.register("modular_gauntlet", ModularGauntlet::new);

    public static void init(IEventBus bus){
        bus.register(Registry.class);
        //ITEMS.register(bus);
    }
}
