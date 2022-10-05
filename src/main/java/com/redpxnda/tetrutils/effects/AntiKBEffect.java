package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.effects.potion.AntiKBPotionEffect;
import com.redpxnda.tetrutils.effects.potion.FreezingPotionEffect;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class AntiKBEffect {
    private static final ItemEffect antikb = ItemEffect.get("tetrutils:kb_reduction");

    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(antikb, 5);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.anti_kb.name", 0, 100, false, effectStatGetter, LabelGetterBasic.percentageLabel,
                new TooltipGetterPercentage("tetrutils.effect.anti_kb.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingDamage(LivingAttackEvent event) {
        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof LivingEntity attacker) {
            ItemStack heldStack = attacker.getMainHandItem();

            if (heldStack.getItem() instanceof ModularItem item) {

                int level = item.getEffectLevel(heldStack, antikb);
                if (level > 0) {
                    defender.addEffect(new MobEffectInstance(PotionEffects.ANTIKB.get(), 5, level, false, false, false));
                }
            }
        }
    }
}
