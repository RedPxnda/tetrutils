package com.redpxnda.tetrutils.effects;

import com.redpxnda.tetrutils.client.gui.stats.TooltipGetterTipped;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class MobEffectEffect {
    private static final ItemEffect tipped = ItemEffect.get("tetrutils:tipped");

    @OnlyIn(Dist.CLIENT)
    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(tipped, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.tipped.name", 0, 1, false, effectStatGetter, LabelGetterBasic.noLabel,
                new TooltipGetterTipped());
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if(eAttacker instanceof LivingEntity attacker){
            ItemStack heldStack = attacker.getMainHandItem();
            if(heldStack.getItem() instanceof ModularItem item){
                item.getEffectData(heldStack).levelMap.keySet().stream().forEach(itemEffect -> {
                    String id = itemEffect.getKey();
                    if(id.startsWith("tetrutils:mob_effect")) {
                        String[] sections = itemEffect.getKey().split("-");
                        String[] values = sections[2].split(",");
                        String effect = sections[1];
                        String chanceStr = (values.length > 2) ? values[2] : "1.0";
                        String lvlStr = values[0];
                        String effStr = values[1];
                        double chance = Double.parseDouble(chanceStr);
                        int lvl = Integer.parseInt(lvlStr);
                        int eff = Integer.parseInt(effStr);
                        Random rand = new Random();
                        int val = rand.nextInt(100) + 1;
                        if (chance*100>=val) {
                            Optional<Holder<MobEffect>> effectOptional = ForgeRegistries.MOB_EFFECTS.getHolder(new ResourceLocation(effect));
                            effectOptional.ifPresent(e -> {
                                MobEffect mobEffect = e.value();
                                if(id.startsWith("tetrutils:mob_effect_stacking")) {
                                    Collection<MobEffectInstance> cEffects = defender.getActiveEffects(); // getting player's effects
                                    ArrayList<MobEffectInstance> effects = new ArrayList<>(cEffects);

                                    MobEffectInstance potionEffect = PotionEffects.getPotionEffect(effects, mobEffect);

                                    int amplifier = potionEffect != null ? potionEffect.getAmplifier() : -1;
                                    int duration =  potionEffect != null ? potionEffect.getDuration() : -1;

                                    if (defender.hasEffect(mobEffect)) { // stacking the effect
                                        int dur = (id.startsWith("tetrutils:mob_effect_stacking_basic")) ? duration : eff;
                                        int amp = (amplifier < lvl-1) ? amplifier+1 : amplifier;
                                        if (potionEffect!=null) potionEffect.update(new MobEffectInstance(mobEffect, dur, amp));
                                    } else { // giving the effect initially
                                        defender.addEffect(new MobEffectInstance(mobEffect, eff, 0));
                                    }
                                } else {
                                    defender.addEffect(new MobEffectInstance(mobEffect, eff, lvl-1)); //add the mob effect without stacking logic
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
