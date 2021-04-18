package com.mrbysco.insane.capability;

import com.mrbysco.insane.config.InsaneConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SanityCapability implements ISanity {
    public static double SANITY_MIN = InsaneConfig.COMMON.minSanity.get();
    public static double SANITY_MAX = InsaneConfig.COMMON.maxSanity.get();
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
