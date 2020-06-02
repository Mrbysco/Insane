package com.mrbysco.insane.capability;

import com.mrbysco.insane.config.InsaneConfig;

public class SanityCapability implements ISanity {
    public static double SANITY_MIN = InsaneConfig.SERVER.minSanity.get().doubleValue();
    public static double SANITY_MAX = InsaneConfig.SERVER.maxSanity.get().doubleValue();
    private double sanity;
    private boolean dirty;

    public SanityCapability() {
        this.sanity = SanityCapability.SANITY_MAX;
    }

    @Override
    public double getSanity() {
        return sanity;
    }

    @Override
    public void setSanity(double sanity) {
        if (sanity > getSanityMax()){
            this.sanity = getSanityMax();
            this.dirty = true;
            return;
        }
        if (sanity < getSanityMin()) {
            this.sanity = getSanityMin();
            this.dirty = true;
            return;
        }
        this.sanity = sanity;
        this.dirty = true;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public double getSanityMin() {
        return SANITY_MIN;
    }

    @Override
    public double getSanityMax() {
        return SANITY_MAX;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
