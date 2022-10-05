package com.redpxnda.tetrutils.effects.potion;

import com.redpxnda.tetrutils.Tetrutils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class PotionEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Tetrutils.MOD_ID);
    public static final RegistryObject<MobEffect> FREEZING = MOB_EFFECTS.register("freezing", FreezingPotionEffect::new);
    public static final RegistryObject<MobEffect> ANTIKB = MOB_EFFECTS.register("kb_res", AntiKBPotionEffect::new);
    public static final RegistryObject<MobEffect> FRENZY = MOB_EFFECTS.register("frenzy", FrenzyPotionEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    public static MobEffectInstance getPotionEffect(ArrayList<MobEffectInstance> effects, MobEffect effectType) {
        for (var effect : effects) if (effect.getEffect().equals(effectType)) return effect;
        return null;
    }
}
