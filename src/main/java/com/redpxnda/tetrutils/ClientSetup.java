package com.redpxnda.tetrutils;

import com.redpxnda.tetrutils.effects.AntiKBEffect;
import com.redpxnda.tetrutils.effects.FreezingEffect;
import com.redpxnda.tetrutils.effects.FrenzyEffect;
import com.redpxnda.tetrutils.effects.WisdomEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "tetrutils", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        FrenzyEffect.init();
        FreezingEffect.init();
        AntiKBEffect.init();
        WisdomEffect.init();
    }
}
