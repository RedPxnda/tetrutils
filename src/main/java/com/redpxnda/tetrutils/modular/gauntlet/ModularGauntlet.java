package com.redpxnda.tetrutils.modular.gauntlet;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.NonNullLazy;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldRenderer;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RemoveSchematic;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;

import java.util.function.Consumer;

public class ModularGauntlet extends ItemModularHandheld {
    public final static String baseKey = "gauntlet/base";
    public final static String bladeKey = "gauntlet/blade";
    public final static String bossKey = "gauntlet/boss";

    public static final String identifier = "modular_shield";

    public ModularGauntlet() {
        super(new Properties()
                .stacksTo(1)
                .fireResistant());
        majorModuleKeys = new String[]{baseKey, bladeKey};
        minorModuleKeys = new String[]{bossKey};

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);

        NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new ModularShieldRenderer(Minecraft.getInstance()));
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("gauntlet/"));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            return getAttributeModifiersCached(itemStack);
        }

        return AttributeHelper.emptyMap;
    }
}
