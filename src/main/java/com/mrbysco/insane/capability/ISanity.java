package com.mrbysco.insane.capability;

public interface ISanity {
    float getSanity();

    void setSanity(float sanity);

    boolean isDirty();

    void setDirty(boolean dirty);

    float getSanityMax();

    float getSanityMin();
}
