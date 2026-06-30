package net.sophiebun.buntsy.blocks.custom.hanging_block;

import net.minecraft.util.StringRepresentable;

public enum HangingStringEnding implements StringRepresentable {
    TOP,
    TOP_ENDING,
    TOP_GRAB,
    MIDDLE,
    MIDDLE_ENDING,
    GRAB;


    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        switch (this){
            case TOP -> {return "top";}
            case TOP_GRAB -> {return "top_grab";}
            case TOP_ENDING -> {return "top_ending";}
            case MIDDLE -> {return "middle";}
            case MIDDLE_ENDING -> {return "middle_ending";}
            default -> {return "grab";}
        }
    }
}
