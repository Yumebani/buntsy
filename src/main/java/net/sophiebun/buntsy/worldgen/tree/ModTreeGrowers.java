package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.sophiebun.buntsy.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {

    public static final TreeGrower BRAVOT_TREE_GROWER = new TreeGrower(
            "bravot_tree",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.BRAVOT_KEY),
            Optional.empty()
    );

    public static final TreeGrower GENTLIT_TREE_GROWER = new TreeGrower(
            "gentlit_tree",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.GENTLIT_KEY),
            Optional.empty()
    );

    public static final TreeGrower MALVOR_TREE_GROWER = new TreeGrower(
            "malvor_tree",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.MALVOR_KEY),
            Optional.empty()
    );

    public static final TreeGrower ORIGAMI_PALM_TREE_GROWER = new TreeGrower(
            "origami_palm_tree",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.ORIGAMI_PALM_KEY),
            Optional.empty()
    );
}
