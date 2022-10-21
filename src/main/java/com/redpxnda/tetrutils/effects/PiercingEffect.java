package com.redpxnda.tetrutils.effects;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class PiercingEffect {
    private static final ItemEffect piercing = ItemEffect.get("tetrutils:piercing_attack");

    public static void init(){
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(piercing, 1);
        final GuiStatBar effectBar = new GuiStatBar(0, 0, barLength, "tetrutils.effect.piercing.name", 0, 30, false, effectStatGetter, LabelGetterBasic.decimalLabel,
                new TooltipGetterDecimal("tetrutils.effect.piercing.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onEntityDamage(LivingDamageEvent event) {
        //event.getEntityLiving().getLevel().getEntities(null, AABB.of(new BoundingBox()));

        LivingEntity defender = event.getEntityLiving();
        Entity eAttacker = event.getSource().getEntity();
        if (eAttacker instanceof Player attacker) {
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) {
                ArrayList<Entity> entities = new ArrayList<>();
                Entity entity = getPlayerHitResult(attacker).get(0);
                System.out.println(entities + " <-- entities");
            }
        }
    }
    private ArrayList<Entity> getPlayerHitResult(LivingEntity player) {
        float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();
        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float)Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float)Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = 1000.0;
        Vec3 endVec = startPos.add((double)additionX * d0, (double)additionY * d0, (double)additionZ * d0);
        AABB startEndBox = new AABB(startPos, endVec);
        ArrayList<Entity> entities = new ArrayList<>();
        for(Entity entity : player.level.getEntities(player, startEndBox, (val) -> true)) {
            System.out.println(entity);
            AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D) {
                    entities.add(entity);
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entities.add(entity);
                            startPos = vec31;
                        }
                    } else {
                        entities.add(entity);
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }
        return entities;
    }
}
