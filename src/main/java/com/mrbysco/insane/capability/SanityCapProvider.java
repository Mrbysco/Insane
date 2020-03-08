package com.mrbysco.insane.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SanityCapProvider implements ICapabilityProvider {
    @CapabilityInject(ISanity.class)
    public static final Capability<ISanity> SANITY_CAPABILITY = null;

    private LazyOptional<ISanity> instance = LazyOptional.of(SANITY_CAPABILITY::getDefaultInstance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == SANITY_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }
}
