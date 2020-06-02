package com.mrbysco.insane.capability;

public interface ISanity {
    double getSanity();

    void setSanity(double sanity);

    boolean isDirty();

    void setDirty(boolean dirty);

    double getSanityMax();

    double getSanityMin();
}
