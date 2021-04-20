package com.mrbysco.insane.api.capability;

public interface ISanity {
    double getSanity();

    void setSanity(double sanity);

    boolean isDirty();

    void setDirty(boolean dirty);

    double getSanityMax();

    void setSanityMax(double max);

    double getSanityMin();

    void setSanityMin(double min);
}
