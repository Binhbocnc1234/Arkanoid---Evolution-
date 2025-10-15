package UI;

import info.*;
import java.awt.*;
import javax.swing.*;

public class MyLabel extends JLabel{
    public MyLabel(String text, int x, int y, int width, int height) {
        super(text, SwingConstants.CENTER);
        setFont(GameInfo.getInstance().getFont());
        setForeground(new Color(180, 160, 255));
        setBounds(x - width / 2, y - width / 2, width, height);
    }
    
}
