 

import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;

public class Lobby extends JPanel {
    public static Lobby instance;
    public JButton playButton;
    private Font customFont;

    public Lobby() {
        instance = this;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("assets/font/Radiant-Kingdom.ttf")).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("Serif", Font.BOLD, 48);
        }

        setLayout(null);

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 100, 200, 80);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        playButton = new MyButton("Play", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 + 100, 200, 70);
        add(playButton);

        this.setBackground(new Color(30, 20, 60));
    }
}