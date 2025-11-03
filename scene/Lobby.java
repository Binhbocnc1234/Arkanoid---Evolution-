package scene;
 
import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;
import level.LevelManager;
import soundmanager.SoundManager;

public class Lobby extends JPanel implements IDisposable {
    private final GalaxyBackground background;
    private Timer animationTimer;
    private final MyButton newGameButton;
    private final MyButton continueButton;
    private final MyButton multiplayerButton;
    private final MyButton highScoresButton;
    private final Image gameIcon;
    
    /**
     * Initialize the Lobby scene.
     */
    public Lobby() {
        SoundManager.getSound("lobbyBG", "/assets/sound/lobbyBackgroundMusic.wav");
        SoundManager.getSound("button", "/assets/sound/button.wav");

        setLayout(null);
        SoundManager.playSoundLoop("lobbyBG");
        
        // Initialize the galaxy background with 100 stars
        background = new GalaxyBackground(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, 100);
        
        // Set up animation timer to update background
        animationTimer = new Timer(16, e -> repaint()); // approximately 60 FPS
        animationTimer.start();

        gameIcon = new ImageIcon("assets/img/gameLogo.png").getImage();

        // JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 200, 500, 100);
        // title.setForeground(new Color(180, 160, 255));
        // add(title);

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

        g.drawImage(gameIcon, (GameInfo.SCREEN_WIDTH - 600) / 2, 25, 600, 150, null);
    }
    
    @Override
    public void dispose() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
        SoundManager.stopSound("lobbyBG");
    }
    
}
