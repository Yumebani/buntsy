package net.sophiebun.buntsy.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.List;

public class ModToolTiers {

    public static final Tier SILKY = new SimpleTier(
            ModTags.Blocks.NEEDS_SIlKY_TOOL,
            1371,
            10f,
            0f,
            25,
            () -> Ingredient.of(ModItems.SILKY_CRYSTAL.get()));
}
