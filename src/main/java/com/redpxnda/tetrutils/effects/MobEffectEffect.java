package com.redpxnda.tetrutils.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class MobEffectEffect {

    @SubscribeEvent
    public void onLivingAttackEvent(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving(); // defender
        Entity eAttacker = event.getSource().getEntity(); // attacker, entity form

        if (eAttacker instanceof LivingEntity attacker) { // attacker, living entity form
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) { //tetra modular item
                for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                    ResourceLocation mobEffectID = ForgeRegistries.MOB_EFFECTS.getKey(mobEffect);
                    ItemEffect tetraEffectID = ItemEffect.get("tetrutils:mob_effect-" + mobEffectID);
                    int lvl = item.getEffectLevel(heldStack, tetraEffectID);
                    int eff = (int) item.getEffectEfficiency(heldStack, tetraEffectID);
                    if (lvl > 0 && eff > 0) {
                        defender.addEffect(new MobEffectInstance(mobEffect, eff, lvl));
                        break;
                    }
                }
            }
        }
    }
}
