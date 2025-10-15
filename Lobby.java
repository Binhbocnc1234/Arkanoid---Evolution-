 

import java.awt.*;
import javax.swing.*;

public class Lobby extends JPanel {
    public Button playButton;
    private Font customFont;
    public Lobby() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("assets/font/Radiant-Kingdom.ttf")).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("Serif", Font.BOLD, 48);
        }

        setLayout(null);

        JLabel title = new JLabel("Arkanoid-Evolution", SwingConstants.CENTER);
        title.setFont(customFont);
        title.setForeground(new Color(180, 160, 255));
        title.setBounds(0, 80, 800, 80);
        add(title);

        playButton = new Button("Play");
        playButton.setBounds(300, 300, 200, 70);
        add(playButton);

        this.setBackground(new Color(30, 20, 60));
    }
}