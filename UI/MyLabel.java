package UI;

import info.*;
import java.awt.*;
import javax.swing.*;

public class MyLabel extends JLabel{
    public MyLabel(String text, int x, int y, int width, int fontSize) {
        super(text, SwingConstants.CENTER);
        // Set color of Font
        setForeground(new Color(180, 160, 255));
        int height = (int) (fontSize * 1.25f);
        setBounds(x - width / 2, y - height / 2, width, height);
        setFont(GameInfo.getInstance().getFont().deriveFont(fontSize));
    }
    
}
