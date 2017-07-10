package ru.nsu.fit.g13205.zharkova;

/**
 * Created by Yana on 25.03.16.
 */
public interface ObservablePanel {

    void addListener(Observer listener);
    void notifyAboutCordsChange(Cord cord, Double z);
}
