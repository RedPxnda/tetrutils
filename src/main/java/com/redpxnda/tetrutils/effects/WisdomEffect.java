package com.redpxnda.tetrutils.effects;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
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


import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class WisdomEffect {
    private static final ItemEffect wisdom = ItemEffect.get("tetrutils:wisdom");

    @OnlyIn(Dist.CLIENT)
    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(wisdom, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.wisdom.name", 0, 30, false, effectStatGetter, LabelGetterBasic.decimalLabel,
                new TooltipGetterDecimal("tetrutils.effect.wisdom.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onXPGain(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer(); // the attacking player, I guess
        if (player!=null && player.getMainHandItem().getItem() instanceof ModularItem item) { //tetra modular item
            double level = item.getEffectLevel(player.getMainHandItem(), wisdom); //effect level

            event.setDroppedExperience((int) (event.getDroppedExperience()*level)); // setting the experience dropped
        }
    }
}
