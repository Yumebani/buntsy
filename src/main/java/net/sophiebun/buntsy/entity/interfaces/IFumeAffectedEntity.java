package net.sophiebun.buntsy.entity.interfaces;

public interface IFumeAffectedEntity {

    void addFume(int fumeType, int level, int duration);
    void tickFumes();
}
