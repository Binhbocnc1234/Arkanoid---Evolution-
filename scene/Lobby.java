package scene;
 
import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;
import soundmanager.SoundManager;

public class Lobby extends JPanel {
    private GalaxyBackground background;
    public MyButton playButton;
    private Timer animationTimer;
    
    public Lobby() {
        SoundManager.getSound("button", "/assets/sound/button.wav");
        System.out.println("Create lobby"); //bị tạo nhiều lần

        setLayout(null);
        
        // Initialize the galaxy background with 100 stars
        background = new GalaxyBackground(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, 100);
        
        // Set up animation timer to update background
        animationTimer = new Timer(16, e -> repaint()); // approximately 60 FPS
        animationTimer.start();

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.CAMPAIGN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2 - 200,
                500, 100);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        playButton = new MyButton("Play", GameInfo.CAMPAIGN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2, 200, 70);
        add(playButton);
        playButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new BattleManager(false));
        });

        MyButton multiplayerButton = new MyButton("Multiplayer", GameInfo.CAMPAIGN_WIDTH / 2,
                GameInfo.SCREEN_HEIGHT / 2 + 100,
                200, 70);
        add(multiplayerButton);
        multiplayerButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new BattleManager(true));
        });

        setOpaque(true);
        setBackground(new Color(30, 20, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.update(g);
    }
    
}
