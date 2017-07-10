package ru.nsu.fit.g13205.zharkova.raytracing.view;

/**
 * Created by Yana on 15.05.16.
 */
public interface WorldListener {
    void traceStarted();

    void pixelTraced();

    void traceFinished(float[][] red, float[][] green, float[][] blue);
}
