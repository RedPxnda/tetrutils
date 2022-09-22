package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.AntiKBPotionEffect;
import com.redpxnda.tetrutils.effects.potion.FreezingPotionEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class AntiKBEffect {
    private static final ItemEffect antikb = ItemEffect.get("tetrutils:kb_reduction");

    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {
        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) eAttacker;
            ItemStack heldStack = attacker.getMainHandItem();

            if (heldStack.getItem() instanceof ModularItem) {
                ModularItem item = (ModularItem) heldStack.getItem();

                int level = item.getEffectLevel(heldStack, antikb);
                if (level > 0) {
                    defender.addEffect(new MobEffectInstance(AntiKBPotionEffect.instance, 2, level, false, false, false));
                }
            }
        }
    }
}
