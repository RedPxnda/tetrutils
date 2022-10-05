package com.redpxnda.tetrutils.effects.potion;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Random;

public class FrenzyPotionEffect extends MobEffect {
    Random random = new Random();
    public FrenzyPotionEffect() {
        super(MobEffectCategory.HARMFUL, 0xeeeeee);
    }

    /*public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()) {
            double x = pLivingEntity.getX();
            double y = pLivingEntity.getY();
            double z = pLivingEntity.getZ();
            ServerLevel level = (ServerLevel) pLivingEntity.getLevel();
            pAmplifier = Math.min(pAmplifier, 10);
            level.sendParticles(ParticleTypes.SCRAPE, x, y+random.nextDouble(), z, pAmplifier, 0.25,0.25,0.25,0.03);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }*/

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }
}
