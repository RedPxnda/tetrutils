package com.redpxnda.tetrutils.client.gui.stats;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.items.modular.ModularItem;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class TooltipGetterTipped implements ITooltipGetter {

    public TooltipGetterTipped() {
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        ArrayList<String> potionEffects = new ArrayList<>();
        ArrayList<ItemEffect> tetraEffects = new ArrayList<>();
        ArrayList<Integer> effectLvls = new ArrayList<>();
        ArrayList<Integer> effectEffs = new ArrayList<>();
        ArrayList<Double> effectChances = new ArrayList<>();
        ModularItem item = (ModularItem) itemStack.getItem();
        item.getEffectData(itemStack).levelMap.keySet().stream().forEach(itemEffect -> {
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
                Optional<Holder<MobEffect>> effectOptional = ForgeRegistries.MOB_EFFECTS.getHolder(new ResourceLocation(effect));
                effectOptional.ifPresent(e -> {
                    potionEffects.add(effect);
                    tetraEffects.add(itemEffect);
                    effectLvls.add(lvl);
                    effectEffs.add(eff);
                    effectChances.add(chance*100);
                });
            }
        });
        String potionEffectsString = "";
        for (int i = 0; i < potionEffects.size(); i++) {
            String stacking = (tetraEffects.get(i).getKey().startsWith("tetrutils:mob_effect_stacking")) ? "§rstacking until level §e" : "";
            potionEffectsString = potionEffectsString + "\n§e" + //base string and new line
                    effectChances.get(i) + "%§r chance for §e" +
                    I18n.get("effect." + potionEffects.get(i).split(":")[0] + "." + potionEffects.get(i).split(":")[1]) + " " + //translated effect name, eg. Slowness
                    stacking + //whether the effect is stacking or not
                    effectLvls.get(i) + //effect amplifier
                    " §rfor §e" + ((double)effectEffs.get(i))/20 + "§r seconds"; //effect duration
        }
        return I18n.get("tetrutils.effect.tipped.tooltip") + potionEffectsString;
    }
}
