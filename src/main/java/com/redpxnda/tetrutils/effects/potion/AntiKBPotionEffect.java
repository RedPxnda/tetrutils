package com.redpxnda.tetrutils.effects.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class AntiKBPotionEffect extends MobEffect {
    public static AntiKBPotionEffect instance;
    public AntiKBPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xeeeeee);

        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "385e50b0-a0c1-4653-9be4-a375ec031d51", 0.05D, AttributeModifier.Operation.ADDITION);

        instance = this;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }
}
