package com.redpxnda.tetrutils.effects.potion;

import com.redpxnda.tetrutils.network.PacketHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class FreezingPotionEffect extends MobEffect {
    public static FreezingPotionEffect instance;
    public FreezingPotionEffect() {
        super(MobEffectCategory.HARMFUL, 0xeeeeee);

        addAttributeModifier(Attributes.MOVEMENT_SPEED, "c2e930ec-9683-4bd7-bc04-8e6ff6587def", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, "e9590c57-94c8-468b-a149-a41743015e2c", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "385e50b0-a0c1-4653-9be4-a375ec031d51", 0.25, AttributeModifier.Operation.ADDITION);

        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()) {
            Double x = pLivingEntity.getX();
            Double y = pLivingEntity.getY();
            Double z = pLivingEntity.getZ();
            pLivingEntity.setIsInPowderSnow(true);
            if (pLivingEntity instanceof Player) {
                //PacketHandler.INSTANCE
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
