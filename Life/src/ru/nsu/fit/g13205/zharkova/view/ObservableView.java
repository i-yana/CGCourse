package ru.nsu.fit.g13205.zharkova.view;

/**
 * Created by Yana on 21.02.16.
 */
interface ObservableView {

    void notifyAboutGridChanges();

    void notifyAboutUpdate();

    void notifyAboutPropertiesChanges();
}
