package com.mrbysco.insane.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SanityStorage implements Capability.IStorage<ISanity> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISanity> capability, ISanity instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putDouble("sanity", instance.getSanity());
        tag.putDouble("sanityMax", instance.getSanityMax());
        tag.putDouble("sanityMin", instance.getSanityMin());

        return tag;
    }

    @Override
    public void readNBT(Capability<ISanity> capability, ISanity instance, Direction side, INBT nbt) {
        CompoundNBT tag = ((CompoundNBT)nbt);
        double sanity = tag.getDouble("sanity");
        double sanityMax = tag.getDouble("sanityMax");
        double sanityMin = tag.getDouble("sanityMin");

        instance.setSanity(sanity);
        instance.setSanityMax(sanityMax);
        instance.setSanityMin(sanityMin);
    }
}
