package ru.nsu.fit.g13205.zharkova.model;

/**
 * Created by Yana on 22.02.16.
 */
public class Properties {

    public static final Properties DEFAULT_SETTINGS = new Properties(DefaultSetting.DEFAULT_LIVE_BEGIN, DefaultSetting.DEFAULT_LIVE_END, DefaultSetting.DEFAULT_BIRTH_BEGIN,
            DefaultSetting.DEFAULT_BIRTH_END, DefaultSetting.DEFAULT_FST_IMPACT, DefaultSetting.DEFAULT_SND_IMPACT);
    private double liveBegin;
    private double liveEnd;
    private double birthBegin;
    private double birthEnd;
    private double fstImpact;
    private double sndImpact;

    public Properties(double liveBegin, double liveEnd, double birthBegin, double birthEnd, double fstImpact, double sndImpact) {
        this.liveBegin = liveBegin;
        this.liveEnd = liveEnd;
        this.birthBegin = birthBegin;
        this.birthEnd = birthEnd;
        this.fstImpact = fstImpact;
        this.sndImpact = sndImpact;
    }

    public double getLiveBegin() {
        return liveBegin;
    }

    public double getLiveEnd() {
        return liveEnd;
    }

    public double getBirthBegin() {
        return birthBegin;
    }

    public double getSndImpact() {
        return sndImpact;
    }

    public double getFstImpact() {
        return fstImpact;
    }

    public double getBirthEnd() {
        return birthEnd;
    }
}
