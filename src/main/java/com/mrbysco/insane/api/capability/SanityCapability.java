package com.mrbysco.insane.api.capability;

import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.handler.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SanityCapability implements ISanity, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
	public double sanityMin;
	public double sanityMax;
	private double sanity;
	private boolean dirty;

	public SanityCapability() {
		this.sanityMin = 0;
		this.sanityMax = InsaneConfig.COMMON.maxSanity.get();
		this.sanity = this.sanityMax;
	}

	@Override
	public double getSanity() {
		return sanity;
	}

	@Override
	public void setSanity(double sanity) {
		if (sanity > getSanityMax()) {
			this.sanity = getSanityMax();
			this.dirty = true;
			return;
		}
		if (sanity < getSanityMin()) {
			this.sanity = getSanityMin();
			this.dirty = true;
			return;
		}
		this.sanity = sanityRounded(sanity);
		this.dirty = true;
	}

	public double sanityRounded(double sanity) {
		BigDecimal bd = BigDecimal.valueOf(sanity);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public double getSanityMin() {
		return sanityMin;
	}

	@Override
	public double getSanityMax() {
		return sanityMax;
	}

	@Override
	public void setSanityMax(double max) {
		this.sanityMax = max;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public CompoundTag serializeNBT() {
		return writeNBT(this);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		readNBT(this, nbt);
	}

	public static CompoundTag writeNBT(ISanity instance) {
		if (instance == null) {
			return null;
		}
		CompoundTag nbt = new CompoundTag();
		nbt.putDouble("sanity", instance.getSanity());
		nbt.putDouble("sanityMax", instance.getSanityMax());

		return nbt;
	}

	public static void readNBT(ISanity instance, CompoundTag tag) {
		double sanity = tag.getDouble("sanity");
		double sanityMax = tag.getDouble("sanityMax");

		instance.setSanity(sanity);
		instance.setSanityMax(sanityMax);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CapabilityHandler.SANITY_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
	}

	public final Capability<ISanity> getCapability() {
		return CapabilityHandler.SANITY_CAPABILITY;
	}
}
