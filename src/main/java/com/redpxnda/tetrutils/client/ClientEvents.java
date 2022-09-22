package com.redpxnda.tetrutils.client;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEvents {
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        System.out.println("mouse moved");
//        if (true)
//            event.setCanceled(true);
    }
}
