package UI;

import info.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyButton extends JButton {
    public MyButton(String text, int x, int y, int width, int height, boolean isBigFont) {
        super(text);
        if (isBigFont) {
            setFont(GameInfo.getInstance().getTitleFont());
        }
        else {
            setFont(GameInfo.getInstance().getSmallFont());
        }
        setFocusPainted(false);
        setBackground(new Color(120, 80, 200)); // sáng hơn nền
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setBounds(x - width / 2, y - height / 2, width, height);
        
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

    public MyButton(String text, int x, int y, int width, int height) {
        this(text, x, y, width, height, false);
    }
}
