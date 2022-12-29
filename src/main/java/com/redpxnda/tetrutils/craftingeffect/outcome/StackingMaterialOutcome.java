package com.redpxnda.tetrutils.craftingeffect.outcome;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.craftingeffect.outcome.CraftingEffectOutcome;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class StackingMaterialOutcome implements CraftingEffectOutcome {

    @Override
    public boolean apply(ItemStack itemStack, String slot, boolean isReplacing, Player player, ItemStack[] preMaterials, Map<ToolAction, Integer> tools,
                         Level level, BlockPos pos, BlockState blockState, boolean consumeResources, ItemStack[] postMaterials) {
        if (consumeResources && !preMaterials[0].isEmpty()) {
            preMaterials[0].setCount(preMaterials[0].getCount()*itemStack.getCount());
            return true;
        }
        return false;
    }
}
