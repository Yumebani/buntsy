package net.sophiebun.buntsy.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.List;

public class ModToolTiers {

    public static final Tier SILKY = TierSortingRegistry.registerTier(
            new ForgeTier( 3,1371, 10f,0f,25,
                    ModTags.Blocks.NEEDS_SIlKY_TOOL, () -> Ingredient.of(ModItems.SILKY_CRYSTAL.get())),
                    new ResourceLocation(BuntsyMod.MODID,"silky"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
}
