package game.scene;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import game.UI.MyButton;
import game.info.GameInfo;
import game.level.LevelManager;
import game.soundmanager.SoundManager;

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
        SoundManager.getSound("lobbyBG", "/sound/lobbyBackgroundMusic.wav");
        SoundManager.getSound("button", "/sound/button.wav");

        setLayout(null);
        
        SoundManager.playSoundLoop("lobbyBG");
        
        
        // Initialize the galaxy background with 100 stars
        background = new GalaxyBackground(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, 100);
        
        // Set up animation timer to update background
        animationTimer = new Timer(16, e -> repaint()); // approximately 60 FPS
        animationTimer.start();

        gameIcon = new ImageIcon(getClass().getResource("/img/gameLogo.png")).getImage();

        // JLabel title = new MyLabel("Arkanoid-Evolution", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2 - 200, 500, 100);
        // title.setForeground(new Color(180, 160, 255));
        // add(title);
        continueButton = new MyButton("Continue", GameInfo.SCREEN_WIDTH/2, GameInfo.SCREEN_HEIGHT/2, 200, 70);
        continueButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new LevelSelect(false));
        });
        if (GameInfo.getInstance().getCurrentPlayerName() == null) {
            continueButton.setEnabled(false);
        }
        
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
                continueButton.setEnabled(true);
                GameInfo.getInstance().setCurrentPlayerName(getPlayerName.trim());
                LevelManager.getInstance().reset();
                GameManager.instance.switchTo(new BattleManager(false));
                SoundManager.stopSound("lobbyBG");
            }
        });
        add(newGameButton);

        
        add(continueButton);

        multiplayerButton = new MyButton("Multiplayer", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2 + 100,
                200, 70);
        multiplayerButton.addActionListener(e -> {
            GameManager.instance.switchTo(new BattleManager(true));
            // SoundManager.playSound("button");
            // // Prompt user for port number
            // String portInput = JOptionPane.showInputDialog(
            //     this,
            //     "Enter port number:",
            //     "Multiplayer Connection",
            //     JOptionPane.PLAIN_MESSAGE
            // );
            // if (portInput != null && !portInput.trim().isEmpty()) {
            //     try {
            //         int port = Integer.parseInt(portInput.trim());
            //         if (port > 0 && port <= 65535) {
            //             GameManager.instance.switchTo(new MultiplayerScene(port));
            //         } else {
            //             JOptionPane.showMessageDialog(
            //                 this,
            //                 "Invalid port number. Port must be between 1 and 65535.",
            //                 "Invalid Port",
            //                 JOptionPane.ERROR_MESSAGE
            //             );
            //         }
            //     } catch (NumberFormatException ex) {
            //         JOptionPane.showMessageDialog(
            //             this,
            //             "Invalid port number. Please enter a valid number.",
            //             "Invalid Port",
            //             JOptionPane.ERROR_MESSAGE
            //         );
            //     }
            // }
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

            // Mapping keybind: F12 to unlock all levels.
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F12"), "unlockAllLevels");
        getActionMap().put("unlockAllLevels", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                GameInfo.getInstance().setUnlockedLevel(10);
                System.out.println("DEBUG: UNLOCKED ALL LEVELS");
            }       
        });


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
