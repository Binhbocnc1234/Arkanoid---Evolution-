package game.UI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import game.info.GameInfo;

public class MyButton extends JButton {

    /**
     * Default constructor for the button.
     * 
     * @param text          button text
     * @param x             x coord
     * @param y             y coord
     * @param width         button width
     * @param height        button height
     * @param isBigFont     button's font size
     * @param color         button's color
     */
    public MyButton(String text, int x, int y, int width, int height, boolean isBigFont, Color color) {
        super(text);
        if (isBigFont) {
            setFont(GameInfo.getInstance().getTitleFont());
        }
        else {
            setFont(GameInfo.getInstance().getSmallFont());
        }
        setFocusPainted(false);
        setBackground(color); // sáng hơn nền
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setBounds(x - width / 2, y - height / 2, width, height);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(new Color(255, 215, 0)); // vàng
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(color);
                    setForeground(Color.WHITE);
                }
            }
        });
    }

    /**
     * Constructor for button with small font
     */
    public MyButton(String text, int x, int y, int width, int height, Color color) {
        this(text, x, y, width, height, false, color);
    }

    /**
     * Constructor for button with small font and default color.
     */
    public MyButton(String text, int x, int y, int width, int height) {
        this(text, x, y, width, height, false, new Color(120, 80, 200));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            // restore default color
            setBackground(new Color(120, 80, 200));
            setForeground(Color.WHITE);
        } else {
            // disabled appearance
            setBackground(Color.GRAY);
            setForeground(Color.DARK_GRAY);
        }
    }
}
