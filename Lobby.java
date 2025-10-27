 

import UI.*;
import info.GameInfo;
import soundmanager.SoundManager;

import java.awt.*;
import javax.swing.*;

public class Lobby extends JPanel {
    public MyButton playButton;
    
    public Lobby() {
        SoundManager.getSound("button", "/assets/sound/button.wav");
        System.out.println("Create lobby"); //bị tạo nhiều lần

        setLayout(null);

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.CAMPAIN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 200, 500, 100);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        playButton = new MyButton("Play", GameInfo.CAMPAIN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2, 200, 70);
        add(playButton);
        playButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new BattleManager(false));
        });

        MyButton multiplayerButton = new MyButton("Multiplayer", GameInfo.CAMPAIN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2 + 100,
                200, 70);
        add(multiplayerButton);
        multiplayerButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new BattleManager(true));
        });

        this.setBackground(new Color(30, 20, 60));
    }
}
