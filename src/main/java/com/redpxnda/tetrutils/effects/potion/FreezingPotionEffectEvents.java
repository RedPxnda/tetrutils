package com.redpxnda.tetrutils.effects.potion;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FreezingPotionEffectEvents {

    @SubscribeEvent
    public static void onMobEffectStart(final PotionEvent.PotionAddedEvent event) {
        if(!event.getEntityLiving().level.isClientSide()
                && event.getPotionEffect() != null
                && event.getPotionEffect().getEffect() == PotionEffects.FREEZING.get()) {
        }
    }
}
