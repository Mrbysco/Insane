package com.mrbysco.insane.api.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SanityCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ISanity.class)
    public static final Capability<ISanity> SANITY_CAPABILITY = null;

    private LazyOptional<ISanity> instance;
    private ISanity sanity;

    public SanityCapProvider() {
        this.sanity = SANITY_CAPABILITY.getDefaultInstance();
        this.instance = LazyOptional.of(() -> sanity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return SANITY_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return SANITY_CAPABILITY.writeNBT(sanity, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        SANITY_CAPABILITY.readNBT(sanity, null, nbt);
    }
}
