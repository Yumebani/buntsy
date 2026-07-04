package net.sophiebun.buntsy.item.custom;

import net.minecraft.world.item.Item;
import net.sophiebun.buntsy.item.ClockworkTier;

public class ClockworkUpgradeItem extends Item {

    public final ClockworkTier CLOCKWORK_TIER;

    public ClockworkUpgradeItem(ClockworkTier tier, Properties pProperties) {
        super(pProperties);
        CLOCKWORK_TIER = tier;
    }

    public ClockworkTier getTier(){
        return CLOCKWORK_TIER;
    }
}
