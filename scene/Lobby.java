package scene;
 

import UI.*;
import info.GameInfo;
import level.LevelManager;
import soundmanager.SoundManager;

import java.awt.*;
import javax.swing.*;



public class Lobby extends JPanel {
    private MyButton newGameButton;
    private MyButton continueButton;
    private MyButton multiplayerButton;
    
    /**
     * Initialize the Lobby scene.
     */
    public Lobby() {
        SoundManager.getSound("button", "/assets/sound/button.wav");

        setLayout(null);

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 200, 500, 100);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        newGameButton = new MyButton("New Game", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 100, 200, 70);
        newGameButton.addActionListener(e -> {
            LevelManager.getInstance().reset();
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new BattleManager(false));
        });
        add(newGameButton);

        continueButton = new MyButton("Continue", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2, 200, 70);
        continueButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new LevelSelect(false));
        });
        add(continueButton);

        multiplayerButton = new MyButton("Multiplayer", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2 + 100,
                200, 70);
        multiplayerButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new LevelSelect(true));
        });
        add(multiplayerButton);

        this.setBackground(new Color(30, 20, 60));

    }
}
