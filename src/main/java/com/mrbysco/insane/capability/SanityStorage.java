package com.mrbysco.insane.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SanityStorage implements Capability.IStorage<Sanity> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<Sanity> capability, Sanity instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("sanity", instance.getSanity());
        return null;
    }

    @Override
    public void readNBT(Capability<Sanity> capability, Sanity instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setSanity(tag.getFloat("sanity"));
    }
}
