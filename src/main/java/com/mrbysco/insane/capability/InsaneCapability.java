package com.mrbysco.insane.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InsaneCapability {
    @CapabilityInject(ISanity.class)
    public static final Capability<ISanity> SANITY = null;

    public static class SanityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
        private ISanity cap;

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
            if(capability == SANITY){
                if(cap == null)cap = SANITY.getDefaultInstance();
                return (LazyOptional<T>) cap;
            }
            return null;
        }

        @Override
        public CompoundNBT serializeNBT() {
            if(cap == null)return new CompoundNBT();
            return (CompoundNBT) SANITY.getStorage().writeNBT(SANITY, cap, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if(cap == null)cap = SANITY.getDefaultInstance();
            SANITY.getStorage().readNBT(SANITY, cap, null, nbt);
        }
    }
}
