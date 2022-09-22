package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.FreezingPotionEffect;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class FreezingEffect {
    private static final ItemEffect freezing = ItemEffect.get("tetrutils:freezing");

    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) eAttacker;
            ItemStack heldStack = attacker.getMainHandItem();

            if (heldStack.getItem() instanceof ModularItem) {
                ModularItem item = (ModularItem) heldStack.getItem();

                int level = item.getEffectLevel(heldStack, freezing);
                if (level > 0) {
                    defender.addEffect(new MobEffectInstance(FreezingPotionEffect.instance, level*20, 1, false, false, false));
                }
            }
        }
    }
}
