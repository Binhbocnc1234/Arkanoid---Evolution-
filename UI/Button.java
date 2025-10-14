package UI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class Button extends JButton {
    private static Font customFont;

    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Radiant-Kingdom.ttf")).deriveFont(32f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("Serif", Font.BOLD, 32);
        }
    }

    public Button(String text) {
        super(text);
        setFont(customFont);
        setFocusPainted(false);
        setBackground(new Color(120, 80, 200)); // sáng hơn nền
        setForeground(Color.WHITE);
        setBorderPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(255, 215, 0)); // vàng
                setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(120, 80, 200));
                setForeground(Color.WHITE);
            }
        });
    }
}
