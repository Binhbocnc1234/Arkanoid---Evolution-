 

import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;

public class Lobby extends JPanel {
    public JButton playButton;

    public Lobby() {
        System.out.println("Create lobby"); //bị tạo nhiều lần

        setLayout(null);

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 100, 500, 100);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        playButton = new MyButton("Play", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 + 100, 200, 70);
        add(playButton);
        playButton.addActionListener(e -> {
            GameManager.instance.switchTo(new BattleManager());
        });

        this.setBackground(new Color(30, 20, 60));
    }
}
