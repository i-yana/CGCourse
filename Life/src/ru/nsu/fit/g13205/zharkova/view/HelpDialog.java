package ru.nsu.fit.g13205.zharkova.view;

import javax.swing.*;

/**
 * Created by Yana on 28.02.16.
 */
public class HelpDialog {

    private static final String text = "                                                                  Справка Жизни\n\n" +
            "Чтобы создать новое поле выберите \"File\" > \"New...\".\n" +
            "Задайте параметры поля - ширину и высоту. \n" +
            "Чтобы изменить параметры поля, размер клетки и ширину линии выберите \"File\" > \"Configure grid\".\n" +
            "Нарисуйте живые клетки, проведя мышью по игровому полю.\n" +
            "Чтобы вернуть клетку в состояние \"мертвая\" выберите режим XOR в настройках поля или на панели тулбара.\n" +
            "Очистка поля. \"Edit\" > \"Clear\" - все клетки \"мертвые\".\n" +
            "Отображение Impact. Поставить галочку в настройках поля или выбрать на панели тулбара.\n" +
            "Просмотр состояния поля. Выберите команду \"Edit\" > \"Next\" - следующий шаг, \n\"Edit\" > \"Play\" для смены состояний раз в 1 секунду.\n" +
            "";

    public HelpDialog(GameView parent){
        JOptionPane.showMessageDialog(parent, text, "Life", JOptionPane.INFORMATION_MESSAGE);
    }
}
