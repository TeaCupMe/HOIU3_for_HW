package utils;

import javax.swing.*;
import java.awt.*;

public class EmojiWindow {
    public static void main(String[] args) {
        // Создаем и настраиваем окно
        JFrame frame = new JFrame("Окно с эмодзи");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(3, 1)); // 3 строки, 1 столбец

        // Создаем метки с эмодзи
        JTextArea label1 = new JTextArea("😊 Улыбка",1,  SwingConstants.CENTER);
        JTextArea label2 = new JTextArea("🐱 Кот",1, SwingConstants.CENTER);
        JTextArea label3 = new JTextArea("\uD83E\uDDD9 Ракета",1, SwingConstants.CENTER);

        // Увеличиваем шрифт для лучшего отображения
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 24);
        label1.setFont(emojiFont);
        label2.setFont(emojiFont);
        label3.setFont(emojiFont);

        // Добавляем метки в окно
        frame.add(label1);
        frame.add(label2);
        frame.add(label3);

        // Центрируем окно и делаем видимым
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
