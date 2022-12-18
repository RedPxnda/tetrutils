package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

public class FreezingEffect {
    private static final ItemEffect freezing = ItemEffect.get("tetrutils:freezing");

    @OnlyIn(Dist.CLIENT)
    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(freezing, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.freezing.name", 0, 30, false, effectStatGetter, LabelGetterBasic.decimalLabel,
                new TooltipGetterDecimal("tetrutils.effect.freezing.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving(); // defender
        Entity eAttacker = event.getSource().getEntity(); // attacker, entity form

        if (eAttacker instanceof LivingEntity attacker) { // attacker, living entity form
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) { //tetra modular item

                int level = item.getEffectLevel(heldStack, freezing); // tetra freezing effect level

                Collection<MobEffectInstance> cEffects = defender.getActiveEffects(); // getting player's effects
                ArrayList<MobEffectInstance> effects = new ArrayList<>(cEffects);

                MobEffectInstance effect = PotionEffects.getPotionEffect(effects, PotionEffects.FREEZING.get());

                int amplifier = effect != null ? effect.getAmplifier() : -1;
                int duration =  effect != null ? effect.getDuration() : -1;

                double eff = item.getEffectEfficiency(heldStack, freezing); // tetra freezing effect efficiency

                if (level > 0 && eff > 0 && defender.hasEffect(PotionEffects.FREEZING.get()) && amplifier < eff-1) { // stacking the effect
                    if (effect!=null) effect.update(new MobEffectInstance(PotionEffects.FREEZING.get(), duration + level*10, amplifier + 1, false, false, false));
                }

                if (level > 0 && !defender.hasEffect(PotionEffects.FREEZING.get())) {// giving the effect initially
                    defender.addEffect(new MobEffectInstance(PotionEffects.FREEZING.get(), level * 20, 0, false, false, false));
                }
            }
        }
    }
}
