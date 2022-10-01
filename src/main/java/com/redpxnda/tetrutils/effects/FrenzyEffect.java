package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.FreezingPotionEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class FrenzyEffect {
    private static final ItemEffect frenzy = ItemEffect.get("tetrutils:frenzy");

    public void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof LivingEntity attacker) {
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) {
                int level = item.getEffectLevel(heldStack, frenzy);
                if (level > 0) {
                    float damage = event.getAmount();
                    event.setAmount(damage + level);
                }
            }
        }
    }
}
