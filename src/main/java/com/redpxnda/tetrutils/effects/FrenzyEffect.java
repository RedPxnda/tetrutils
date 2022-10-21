package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.FreezingPotionEffect;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimal;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.ArrayList;
import java.util.Collection;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class FrenzyEffect {
    private static final ItemEffect frenzy = ItemEffect.get("tetrutils:frenzy");

    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(frenzy, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.frenzy.name", 0, 10, false, effectStatGetter, LabelGetterBasic.decimalLabel,
                new TooltipGetterDecimal("tetrutils.effect.frenzy.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving(); // defender
        Entity eAttacker = event.getSource().getEntity(); // attacker, entity form

        if (defender.hasEffect(PotionEffects.FRENZY.get())) {
            defender.removeEffect(PotionEffects.FRENZY.get());
        }

        if (eAttacker instanceof LivingEntity attacker) { // attacker, living entity form
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) { //tetra modular item

                int level = item.getEffectLevel(heldStack, frenzy); // tetra freezing effect level

                Collection<MobEffectInstance> cEffects = attacker.getActiveEffects(); // getting player's effects
                ArrayList<MobEffectInstance> effects = new ArrayList<>(cEffects);

                MobEffectInstance effect = PotionEffects.getPotionEffect(effects, PotionEffects.FRENZY.get());

                int amplifier = effect != null ? effect.getAmplifier() : -1;

                double eff = item.getEffectEfficiency(heldStack, frenzy); // tetra freezing effect efficiency
                int duration = (int) Math.max(eff*2-amplifier*2, 1)*20;

                if (level > 0 && eff > 0 && attacker.hasEffect(PotionEffects.FRENZY.get())) { // stacking the effect
                    attacker.removeEffect(PotionEffects.FRENZY.get());
                    attacker.addEffect(new MobEffectInstance(PotionEffects.FRENZY.get(), duration, (int) Math.min(amplifier + 1, eff-1), false, false, true));
                }
                float damage = event.getAmount();
                event.setAmount(damage + (float) amplifier*level);
                ServerLevel serverLevel = (ServerLevel) defender.getLevel();
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHER_WART_BLOCK.defaultBlockState()), defender.getX(), defender.getY(), defender.getZ(), amplifier*20, 0.5,0.5,0.5,0.03);

                if (level > 0 && !attacker.hasEffect(PotionEffects.FRENZY.get())) {// giving the effect initially
                    attacker.addEffect(new MobEffectInstance(PotionEffects.FRENZY.get(), duration, 0, false, false, false));
                }
            }
        }
    }
}
