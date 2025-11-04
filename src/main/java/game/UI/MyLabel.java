package game.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import game.info.GameInfo;

public class MyLabel extends JLabel{
    public MyLabel(String text, int x, int y, int width, int fontSize) {
        super(text, SwingConstants.CENTER);
        // Set color of Font
        setForeground(new Color(180, 160, 255));
        
        // Sử dụng smallFont cho text nhỏ hơn 30, titleFont cho text lớn hơn
        Font baseFont = (fontSize <= 30) ? 
            GameInfo.getInstance().getSmallFont() : 
            GameInfo.getInstance().getTitleFont();
            
        // Tính toán height dựa trên fontSize thực tế
        setFont(baseFont.deriveFont((float)fontSize));
        FontMetrics metrics = getFontMetrics(getFont());
        int height = metrics.getHeight();
        
        // Đặt bounds dựa trên kích thước thực tế của text
        setBounds(x - width / 2, y - height / 2, width, height);
    }
    
}
