package ru.nsu.g13205.zharkova;

import javax.swing.*;

/**
 * Created by Yana on 16.03.16.
 */
public class HelpDialog {
    private static final String text = "                                                                  Справка Filter\n\n" +
            "Чтобы открыть новое изображение выберите \"File\" > \"Open...\".\n" +
            "Нажмите кнопку Select и выберите область для редактирования. \n" +
            "Выбранная часть изображения отобразится во второй области.\n" +
            "Редактируйте изображение, используя фильтры из меню \"Filter\".\n" +
            "Чтобы перенести изображение из одной области в другую используйте опции из меню \"Edit\".\n" +
            "Режим крупных пикселей отображает картинку в крупных пикселях, размер которых можно задать в \"Edit\" > \"Pixel Size...\".\n" +
            "Сохраните ваш результат в файл \"File\" > \"Save...\".\n";

    public HelpDialog(MainView parent){
        JOptionPane.showMessageDialog(parent, text, "Life", JOptionPane.INFORMATION_MESSAGE);
    }
}
