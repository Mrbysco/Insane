package com.mrbysco.insane.capability;

import com.mrbysco.insane.config.InsaneConfig;

public class SanityCapability implements ISanity {
    public static float SANITY_MIN = InsaneConfig.COMMON.minSanity.get();
    public static float SANITY_MAX = InsaneConfig.COMMON.maxSanity.get();
    private float sanity;
    private boolean dirty;

    public SanityCapability() {
        this.sanity = SanityCapability.SANITY_MAX;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public void setSanity(float sanity) {
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
    public float getSanityMin() {
        return SANITY_MIN;
    }

    @Override
    public float getSanityMax() {
        return SANITY_MAX;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
