package com.mrbysco.insane.api.capability;

import com.mrbysco.insane.config.InsaneConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SanityCapability implements ISanity {
    public double sanityMin;
    public double sanityMax;
    private double sanity;
    private boolean dirty;

    public SanityCapability() {
        this.sanityMin = InsaneConfig.COMMON.minSanity.get();
        this.sanityMax = InsaneConfig.COMMON.maxSanity.get();
        this.sanity = this.sanityMax;
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
    public void setSanityMin(double min) {
        this.sanityMin = min;
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
}
