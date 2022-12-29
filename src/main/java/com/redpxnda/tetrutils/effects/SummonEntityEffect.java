package com.redpxnda.tetrutils.effects;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.redpxnda.tetrutils.client.gui.stats.TooltipGetterTipped;
import com.redpxnda.tetrutils.effects.potion.PotionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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

public class SummonEntityEffect {
    /*private static final ItemEffect tipped = ItemEffect.get("tetrutils:tipped");

    @OnlyIn(Dist.CLIENT)
    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(tipped, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.tipped.name", 0, 1, false, effectStatGetter, LabelGetterBasic.noLabel,
                new TooltipGetterTipped());
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }*/

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack heldStack = event.getPlayer().getMainHandItem();
        if(heldStack.getItem() instanceof ModularItem item){
            item.getEffectData(heldStack).levelMap.keySet().stream().forEach(itemEffect -> {
                String id = itemEffect.getKey();
                LivingEntity origin = event.getPlayer();
                if(id.startsWith("tetrutils:summon_entity_rclick")) {
                    try { summonEntities(itemEffect, origin, origin); } catch (CommandSyntaxException e) { throw new RuntimeException(e); }
                }
            });
        }
    }
    @SubscribeEvent
    public void onLivingAttack(LivingDamageEvent event) {
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof Player player) {
            ItemStack heldStack = player.getMainHandItem();
            if(heldStack.getItem() instanceof ModularItem item){
                item.getEffectData(heldStack).levelMap.keySet().stream().forEach(itemEffect -> {
                    String id = itemEffect.getKey();
                    LivingEntity origin = event.getEntityLiving();
                    if(id.startsWith("tetrutils:summon_entity_attack")) {
                        try { summonEntities(itemEffect, origin, player); } catch (CommandSyntaxException e) { throw new RuntimeException(e); }
                    }
                });
            }
        }
    }
    void summonEntities(ItemEffect itemEffect, LivingEntity origin, LivingEntity owner) throws CommandSyntaxException {
        String[] sections = itemEffect.getKey().split("/");
        String[] values = sections[2].split("\\|");
        String entity = sections[1];
        String spreadStr = (values.length > 3) ? values[3] : "0.1";
        String accountForYStr = (values.length > 4) ? values[4] : "true";
        String dataStr = (values.length > 5) ? values[5] : "{}";
        String lvlStr = values[0];
        String effStr = values[1];
        String countStr = values[2];
        float spread = (spreadStr.equals("auto")) ? -1 : Float.parseFloat(spreadStr);
        double horzVelo = Double.parseDouble(lvlStr);
        double vertVelo = Double.parseDouble(effStr);
        boolean accountForY = Boolean.parseBoolean(accountForYStr);
        int count = Integer.parseInt(countStr);
        CompoundTag nbtData = TagParser.parseTag(dataStr);
        Optional<Holder<EntityType<?>>> entityOptional = ForgeRegistries.ENTITIES.getHolder(new ResourceLocation(entity));
        entityOptional.ifPresent(e -> {
            //double yMult = (accountForY) ? origin.getLookAngle().y : 0;
            EntityType<?> entityType = e.value();
            final float arc = (spread>-1) ? spread : (float) Math.PI * 2.0F/Math.max(1.0F, count);
            for (int i = 0; i < count; i++) {
                float angle = arc * (i-count/2f) + (float) Math.toRadians(origin.getYRot()+90+spread*28);
                float x = (float) (Math.cos(angle));
                float z = (float) (Math.sin(angle));
                Entity mob = entityType.create(origin.level);
                mob.moveTo(origin.getX(),origin.getY()+1.62,origin.getZ());
                CompoundTag newNbt = mob.serializeNBT().merge(nbtData);
                mob.deserializeNBT(newNbt);
                Vec3 motion = new Vec3(x, 0, z);
                mob.setDeltaMovement(motion.add(0,vertVelo,0).multiply(horzVelo,1,horzVelo));
                origin.level.addFreshEntity(mob);
            }
        });
    }
}
