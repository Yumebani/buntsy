package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.util.StringRepresentable;

public enum HangingStringEnding implements StringRepresentable {
    TOP,
    TOP_ENDING,
    TOP_GRAB,
    MIDDLE,
    MIDDLE_ENDING,
    GRAB
    ;

    @Override
    public String getSerializedName() {
        return this.toString();
    }
}
