package scene;
 
import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;
import level.LevelManager;
import soundmanager.SoundManager;

public class Lobby extends JPanel {
    private GalaxyBackground background;
    public MyButton playButton;
    private Timer animationTimer;
    private MyButton newGameButton;
    private MyButton continueButton;
    private MyButton multiplayerButton;
    private MyButton highScoresButton;
    
    /**
     * Initialize the Lobby scene.
     */
    public Lobby() {
        SoundManager.getSound("button", "/assets/sound/button.wav");

        setLayout(null);
        
        // Initialize the galaxy background with 100 stars
        background = new GalaxyBackground(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, 100);
        
        // Set up animation timer to update background
        animationTimer = new Timer(16, e -> repaint()); // approximately 60 FPS
        animationTimer.start();

        JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 200, 500, 100);
        title.setForeground(new Color(180, 160, 255));
        add(title);

        newGameButton = new MyButton("New Game", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 100, 200, 70);
        newGameButton.addActionListener(e -> {
            SoundManager.playSound("button");
            String getPlayerName = JOptionPane.showInputDialog(
                this,
                "Enter your name:",
                "New Game",
                JOptionPane.PLAIN_MESSAGE
            );
            if (getPlayerName != null && !getPlayerName.trim().isEmpty()) {
                GameInfo.getInstance().setCurrentPlayerName(getPlayerName.trim());
                LevelManager.getInstance().reset();
                GameManager.instance.switchTo(new BattleManager(false));
            }
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

        highScoresButton = new MyButton("High Scores", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2 + 200,
                 200, 70);
        highScoresButton.addActionListener(e -> {
            SoundManager.playSound("button");
            animationTimer.stop();
            GameManager.instance.switchTo(new LeaderBoard());
        });
        add(highScoresButton);

        setOpaque(true);
        setBackground(new Color(30, 20, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.update(g);
    }
    
}
